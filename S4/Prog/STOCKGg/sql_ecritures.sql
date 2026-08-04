-- ============================================================
-- SQL SCRIPT: ACCOUNTING ENTRIES TABLES (PostgreSQL)
-- ============================================================
-- This script creates tables for accounting entry generation
-- to track stock movements in accounting records

-- Drop existing tables and views if they exist (for clean migration)
DROP VIEW IF EXISTS v_ecriture_par_mouvement CASCADE;
DROP VIEW IF EXISTS v_ecriture_summary CASCADE;
DROP VIEW IF EXISTS v_ecriture_complete CASCADE;
DROP TABLE IF EXISTS ecriture_fils CASCADE;
DROP TABLE IF EXISTS ecriture_mere CASCADE;

-- ============ ECRITURE MERE (Accounting Entry Header) ============
CREATE TABLE ecriture_mere (
    id              SERIAL PRIMARY KEY,
    date_ecriture   DATE NOT NULL,
    libelle         VARCHAR(255) NOT NULL,
    journal         VARCHAR(50) NOT NULL DEFAULT 'Stock',
    mouvement_id    INT REFERENCES mouvement(id) ON DELETE CASCADE,
    created_at      TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_ecriture_mere_date ON ecriture_mere(date_ecriture);
CREATE INDEX idx_ecriture_mere_journal ON ecriture_mere(journal);
CREATE INDEX idx_ecriture_mere_mouvement ON ecriture_mere(mouvement_id);

-- ============ ECRITURE FILS (Accounting Entry Details) ============
CREATE TABLE ecriture_fils (
    id                  SERIAL PRIMARY KEY,
    ecriture_mere_id    INT NOT NULL REFERENCES ecriture_mere(id) ON DELETE CASCADE,
    numero_compte       VARCHAR(10) NOT NULL,
    libelle             VARCHAR(255) NOT NULL,
    debit               DECIMAL(15,4) DEFAULT 0,
    credit              DECIMAL(15,4) DEFAULT 0,
    mouvement_id        INT REFERENCES mouvement(id) ON DELETE CASCADE,
    created_at          TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_ecriture_fils_mere ON ecriture_fils(ecriture_mere_id);
CREATE INDEX idx_ecriture_fils_compte ON ecriture_fils(numero_compte);
CREATE INDEX idx_ecriture_fils_mouvement ON ecriture_fils(mouvement_id);

-- ============ VIEW: COMPLETE ACCOUNTING ENTRIES ============
-- This view shows complete accounting entries with all detail lines
CREATE OR REPLACE VIEW v_ecriture_complete AS
SELECT
    em.id AS ecriture_mere_id,
    em.date_ecriture,
    em.libelle AS libelle_mere,
    em.journal,
    em.mouvement_id,
    ef.id AS ecriture_fils_id,
    ef.numero_compte,
    ef.libelle AS libelle_fils,
    ef.debit,
    ef.credit,
    (ef.debit - ef.credit) AS difference,
    em.created_at
FROM ecriture_mere em
LEFT JOIN ecriture_fils ef ON em.id = ef.ecriture_mere_id
ORDER BY em.date_ecriture DESC, em.id, ef.id;

-- ============ VIEW: ACCOUNTING ENTRY SUMMARY ============
-- This view shows aggregated accounting entries by date and journal
CREATE OR REPLACE VIEW v_ecriture_summary AS
SELECT
    em.date_ecriture,
    em.journal,
    COUNT(DISTINCT em.id) AS nb_ecritures,
    SUM(ef.debit) AS total_debit,
    SUM(ef.credit) AS total_credit,
    (SUM(ef.debit) - SUM(ef.credit)) AS difference
FROM ecriture_mere em
LEFT JOIN ecriture_fils ef ON em.id = ef.ecriture_mere_id
GROUP BY em.date_ecriture, em.journal
ORDER BY em.date_ecriture DESC;

-- ============ VIEW: ACCOUNTING ENTRIES BY MOVEMENT ============
-- Links accounting entries back to stock movements
CREATE OR REPLACE VIEW v_ecriture_par_mouvement AS
SELECT
    m.id AS mouvement_id,
    m.date_mouvement,
    a.code AS article_code,
    m.type_mouvement,
    m.quantite,
    m.valeur_mouvement,
    em.id AS ecriture_mere_id,
    COUNT(ef.id) AS nb_lignes_ecriture,
    SUM(ef.debit) AS total_debit,
    SUM(ef.credit) AS total_credit,
    CASE 
        WHEN SUM(ef.debit) = SUM(ef.credit) THEN 'Équilibrée'
        ELSE 'Déséquilibrée'
    END AS etat_ecriture
FROM mouvement m
LEFT JOIN article a ON m.article_id = a.id
LEFT JOIN ecriture_mere em ON m.id = em.mouvement_id
LEFT JOIN ecriture_fils ef ON em.id = ef.ecriture_mere_id
GROUP BY m.id, m.date_mouvement, a.code, m.type_mouvement, m.quantite, m.valeur_mouvement, em.id
ORDER BY m.date_mouvement DESC;
