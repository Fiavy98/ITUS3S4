---POSTGRES---
CREATE DATABASE voyage;
\c voyage;
CREATE TABLE type(
    id SERIAL PRIMARY KEY,
    nom VARCHAR(20)
);

CREATE TABLE voiture(
    id SERIAL PRIMARY KEY,
    id_type INT REFERENCES type(id),
    vMax DECIMAL(10,2),
    vMin DECIMAL(10,2),
    logueur DECIMAL(10,2),
    largeur DECIMAL(10,2)
);

CREATE TABLE status(
    id SERIAL PRIMARY KEY,
    nom VARCHAR(20)
);

CREATE TABLE voiture_route(
    id SERIAL PRIMARY KEY,
    id_voiture INT REFERENCES voiture(id),
    code_route VARCHAR(20) NOT NULL,
    id_status INT REFERENCES type(id),
    duree TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


---===================================ORACLE===========================================---
