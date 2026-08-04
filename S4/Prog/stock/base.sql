CREATE DATABASE stock;

\c stock;

CREATE TYPE methode_valuation_enum AS ENUM ('FIFO', 'LIFO', 'CUMP');

CREATE TYPE type_mouvement_enum AS ENUM ('ENTREE', 'SORTIE');


CREATE TABLE produits (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(150) NOT NULL,
    methodeValuation methode_valuation_enum NOT NULL,
    prixVenteDefaut NUMERIC(12,2),
    unite VARCHAR(50) NOT NULL
);

CREATE TABLE mouvements (
    id SERIAL PRIMARY KEY,
    produitId INTEGER NOT NULL,
    type type_mouvement_enum NOT NULL,
    quantite INTEGER,
    quantiteRestante INTEGER,
    prixUnitaire NUMERIC(12,2) NOT NULL,
    date TIMESTAMP,

    FOREIGN KEY (produitId)
    REFERENCES produits(id)
    ON DELETE CASCADE

);

CREATE TABLE historique_mouvements(
    id SERIAL PRIMARY KEY,
    mouvementId INTEGER,
    stockQte INTEGER,
    valeurStock NUMERIC(12,2),
    cump NUMERIC(12,2),
    methode VARCHAR(10),

    FOREIGN KEY (mouvementId)
    REFERENCES mouvements(id)
    ON DELETE CASCADE
);

CREATE VIEW v_etat_stock AS
SELECT
    m.date,
    p.nom AS produit,
    m.type,
    h.methode,
    m.prixUnitaire,
    (m.quantite * m.prixUnitaire) AS valeur,
    h.stockQte,
    h.valeurStock,
    h.cump
FROM mouvements m
JOIN produits p ON p.id=m.produitId
JOIN historique_mouvements h ON h.mouvementId=m.id

ORDER BY m.date;