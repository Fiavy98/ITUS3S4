-- ============================================================
-- SCRIPT COMPLET DE CRÉATION DE LA BASE DE DONNÉES
-- ============================================================

-- Activer l'extension pour les UUID si nécessaire (optionnel)
-- CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ============ MODULE (pour généralisation future) ============
DROP TABLE IF EXISTS module CASCADE;
drop table if exists categorie cascade;
drop table if exists article cascade;
drop table if exists lot cascade;
drop table if exists mouvement cascade;
drop table if exists mouvement_lot_source cascade;
DROP VIEW IF EXISTS v_mouvement_detail CASCADE;
DROP VIEW IF EXISTS v_etat_stock_general CASCADE;
CREATE TABLE module (
    id          SERIAL PRIMARY KEY,
    code        VARCHAR(50) UNIQUE NOT NULL,
    libelle     VARCHAR(100) NOT NULL,
    description TEXT,
    actif       BOOLEAN DEFAULT TRUE,
    created_at  TIMESTAMP DEFAULT NOW()
);

-- Insertion du module stock par défaut
INSERT INTO module (code, libelle, description) VALUES 
('STOCK', 'Gestion de stock', 'Module principal de gestion de stock avec FIFO/LIFO/CUMP');

-- ============ CATÉGORIES (hiérarchiques) ============
CREATE TABLE categorie (
    id                  SERIAL PRIMARY KEY,
    module_id           INT REFERENCES module(id),
    code                VARCHAR(50) UNIQUE NOT NULL,
    libelle             VARCHAR(150) NOT NULL,
    categorie_parent_id INT REFERENCES categorie(id),
    description         TEXT,
    created_at          TIMESTAMP DEFAULT NOW()
);

-- ============ ARTICLE ============
CREATE TABLE article (
    id                      SERIAL PRIMARY KEY,
    categorie_id            INT REFERENCES categorie(id),
    code                    VARCHAR(50) UNIQUE NOT NULL,
    unite_mesure            VARCHAR(20) DEFAULT 'unité',
    methode_gestion         VARCHAR(10) DEFAULT 'CUMP' CHECK (methode_gestion IN ('FIFO','LIFO','CUMP')),
    stock_actuel            DECIMAL(15,4) DEFAULT 0,
    valeur_stock_actuelle   DECIMAL(15,4) DEFAULT 0,
    cump_actuel             DECIMAL(15,4) DEFAULT 0,
    stock_reserve           DECIMAL(15,4) DEFAULT 0,
    actif                   BOOLEAN DEFAULT TRUE,
    created_at              TIMESTAMP DEFAULT NOW(),
    updated_at              TIMESTAMP DEFAULT NOW()
);

-- ============ LOT ============
CREATE TABLE lot (
    id                    SERIAL PRIMARY KEY,
    article_id            INT NOT NULL REFERENCES article(id),
    mouvement_entree_id   INT,
    date_entree           DATE NOT NULL,
    quantite_initiale     DECIMAL(15,4) NOT NULL,
    quantite_restante     DECIMAL(15,4) NOT NULL,
    prix_unitaire         DECIMAL(15,4) NOT NULL,
    valeur_restante       DECIMAL(15,4) GENERATED ALWAYS AS 
                          (quantite_restante * prix_unitaire) STORED,
    epuise                BOOLEAN DEFAULT FALSE,
    source_reference      VARCHAR(100),
    source_type           VARCHAR(50),
    source_document       VARCHAR(200),
    created_at            TIMESTAMP DEFAULT NOW()
);
CREATE INDEX idx_lot_article_date ON lot(article_id, date_entree, epuise);

-- ============ MOUVEMENT ============
CREATE TABLE mouvement (
    id                      SERIAL PRIMARY KEY,
    article_id              INT NOT NULL REFERENCES article(id),
    date_mouvement          DATE NOT NULL,
    type_mouvement          VARCHAR(10) NOT NULL CHECK (type_mouvement IN ('ENTREE','SORTIE','AJUSTEMENT')),
    quantite                DECIMAL(15,4) NOT NULL,
    prix_unitaire_calcule   DECIMAL(15,4),
    valeur_mouvement        DECIMAL(15,4),
    stock_qte_apres         DECIMAL(15,4),
    valeur_stock_apres      DECIMAL(15,4),
    cump_apres              DECIMAL(15,4),
    methode_valorisation    VARCHAR(10) CHECK (methode_valorisation IN ('FIFO','LIFO','CUMP')),
    source_reference        VARCHAR(100),
    source_type             VARCHAR(50),
    source_tiers            VARCHAR(200),
    motif                   TEXT,
    created_at              TIMESTAMP DEFAULT NOW()
);
CREATE INDEX idx_mouvement_article_date ON mouvement(article_id, date_mouvement);

-- ============ MOUVEMENT_LOT_SOURCE (traçabilité fine) ============
CREATE TABLE mouvement_lot_source (
    id                      SERIAL PRIMARY KEY,
    mouvement_id            INT NOT NULL REFERENCES mouvement(id) ON DELETE CASCADE,
    lot_id                  INT NOT NULL REFERENCES lot(id),
    quantite_prelevee       DECIMAL(15,4) NOT NULL,
    prix_unitaire_lot       DECIMAL(15,4) NOT NULL,
    valeur_prelevee         DECIMAL(15,4) GENERATED ALWAYS AS
                            (quantite_prelevee * prix_unitaire_lot) STORED,
    source_lot_reference    VARCHAR(100),
    source_lot_type         VARCHAR(50),
    ordre_consommation      INT DEFAULT 1,
    created_at              TIMESTAMP DEFAULT NOW()
);
CREATE INDEX idx_mls_mouvement ON mouvement_lot_source(mouvement_id);
CREATE INDEX idx_mls_lot ON mouvement_lot_source(lot_id);

-- ============ VUE ÉTAT STOCK GÉNÉRAL ============
CREATE OR REPLACE VIEW v_etat_stock_general AS
SELECT
    a.id AS article_id,
    a.code,
    c.libelle AS categorie,
    a.unite_mesure,
    a.stock_actuel,
    a.stock_reserve,
    (a.stock_actuel - a.stock_reserve) AS stock_disponible,
    a.cump_actuel,
    a.valeur_stock_actuelle,
    COUNT(l.id) FILTER (WHERE NOT l.epuise) AS nb_lots_actifs,
    MAX(m.date_mouvement) AS dernier_mouvement
FROM article a
LEFT JOIN categorie c ON a.categorie_id = c.id
LEFT JOIN lot l ON l.article_id = a.id
LEFT JOIN mouvement m ON m.article_id = a.id
WHERE a.actif = TRUE
GROUP BY a.id, a.code, c.libelle,
         a.unite_mesure,
         a.stock_actuel, a.stock_reserve, a.cump_actuel, a.valeur_stock_actuelle;

-- ============ VUE DÉTAIL MOUVEMENTS AVEC SOURCE ============
CREATE OR REPLACE VIEW v_mouvement_detail AS
SELECT
    m.id AS mouvement_id,
    m.article_id AS article_id,
    m.date_mouvement,
    a.code AS article_code,
    c.libelle AS categorie_libelle,
    m.type_mouvement,
    CASE
        WHEN m.type_mouvement = 'SORTIE' THEN -m.quantite
        ELSE m.quantite
    END AS quantite_signe,
    m.quantite,
    m.prix_unitaire_calcule AS pu,
    m.valeur_mouvement,
    (m.stock_qte_apres - CASE
        WHEN m.type_mouvement = 'SORTIE' THEN -m.quantite
        ELSE m.quantite
    END) AS stock_qte_avant,
    m.stock_qte_apres,
    (m.valeur_stock_apres - m.valeur_mouvement) AS valeur_stock_avant,
    m.valeur_stock_apres AS valeur_stock_cumulee,
    m.valeur_stock_apres,
    m.cump_apres,
    m.methode_valorisation,
    m.source_reference,
    m.source_type,
    m.source_tiers,
    COALESCE(
        (SELECT STRING_AGG(
            'Lot#' || mls.lot_id || ' (' || mls.quantite_prelevee || 'u @ ' ||
            mls.prix_unitaire_lot || ' / src:' || COALESCE(mls.source_lot_reference,'?') || ')',
            ' | ' ORDER BY mls.ordre_consommation
        )
        FROM mouvement_lot_source mls
        WHERE mls.mouvement_id = m.id),
        'Aucun lot source (entrée ou CUMP)'
    ) AS detail_lots_source
FROM mouvement m
JOIN article a ON m.article_id = a.id
LEFT JOIN categorie c ON a.categorie_id = c.id;

-- ============ TRIGGER DE MISE À JOUR updated_at ============
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_article_updated_at
    BEFORE UPDATE ON article
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();
