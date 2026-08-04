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

CREATE TABLE Voiture_route(
    id SERIAL PRIMARY KEY,
    id_voiture INT REFERENCES voiture(id),
    code_route VARCHAR(20) NOT NULL
);

---===================================ORACLE===========================================---

-- Table VILLE
CREATE TABLE ville (
    id   NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nom  VARCHAR2(100) NOT NULL
);

-- Table ROUTE
CREATE TABLE route (
    id          NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    rn          VARCHAR2(50) NOT NULL,
    IdVille_depart   NUMBER NOT NULL,
    idVille_arriver  NUMBER NOT NULL,
    longueur_km          NUMBER(6,2)   -- distance en kilomètres
    CONSTRAINT fk_route_depart FOREIGN KEY (idVille_depart) REFERENCES ville(id),
    CONSTRAINT fk_route_arrivee FOREIGN KEY (idVille_arriver) REFERENCES ville(id)

);

-- Table ROUTE_VILLE
CREATE TABLE route_ville (
    id          NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_route    NUMBER NOT NULL,
    id_ville    NUMBER NOT NULL,
    position_km NUMBER(6,2),
    CONSTRAINT fk_route FOREIGN KEY (id_route) REFERENCES route(id),
    CONSTRAINT fk_ville FOREIGN KEY (id_ville) REFERENCES ville(id)
);

-- Table TROU
CREATE TABLE trou (
    id           NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_route     NUMBER NOT NULL,
    position_km  NUMBER(6,2),
    profondeur   NUMBER(5,2), -- en cm
    largeur      NUMBER(5,2), -- en cm
    etat         VARCHAR2(50),
    CONSTRAINT fk_trou_route FOREIGN KEY (id_route) REFERENCES route(id)
);

