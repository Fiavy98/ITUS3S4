CREATE DATABASE test_sig;
\c test_sig
CREATE EXTENSION postgis;

-- Créer la table villes
CREATE TABLE villes (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(50),
    geom GEOMETRY(Point, 4326) -- WGS84
);

-- Insérer quelques villes de Madagascar
INSERT INTO villes (nom, geom) VALUES
('Antananarivo', ST_SetSRID(ST_MakePoint(47.5164, -18.8792), 4326)),
('Toamasina', ST_SetSRID(ST_MakePoint(49.3833, -18.1667), 4326)),
('Fianarantsoa', ST_SetSRID(ST_MakePoint(47.0833, -21.4333), 4326)),
('Mahajanga', ST_SetSRID(ST_MakePoint(46.3167, -15.7167), 4326)),
('Toliara', ST_SetSRID(ST_MakePoint(43.6333, -23.3500), 4326));
