CREATE DATABASE stock;

\c stock;

CREATE TYPE methode_valuation_enum AS ENUM ('FIFO', 'LIFO', 'CUMP');

CREATE TYPE type_mouvement_enum AS ENUM ('ENTREE', 'SORTIE');


CREATE TABLE produits (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(150) NOT NULL,
    methode_valuation methode_valuation_enum NOT NULL,
    prix_vente_defaut NUMERIC(12,2),
    unite VARCHAR(50) NOT NULL
);

CREATE TABLE mouvements (
    id SERIAL PRIMARY KEY,

    produit_id INTEGER NOT NULL,
    
    type type_mouvement_enum NOT NULL,
    
    quantite INTEGER ,
    quantite_restante INTEGER,

    prix_unitaire NUMERIC(12,2) NOT NULL,

    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_produit
        FOREIGN KEY (produit_id)
        REFERENCES produits(id)
        ON DELETE CASCADE
);


