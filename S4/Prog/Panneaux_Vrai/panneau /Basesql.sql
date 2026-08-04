CREATE DATABASE solar_db;
USE solar_db;

CREATE TABLE tranche (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(50),
    H_depart TIME,
    H_arrive TIME
);

CREATE TABLE materielle (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100),
    heure FLOAT,
    idTranche INT,
    watt FLOAT,
    FOREIGN KEY (idTranche) REFERENCES tranche(id)
);

CREATE TABLE pannSolaire (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100),
    puissance FLOAT,
    batterie FLOAT
);

CREATE TABLE destination (
    id INT AUTO_INCREMENT PRIMARY KEY,
    idMaterielle INT,
    idPanSolaire INT,
    FOREIGN KEY (idMaterielle) REFERENCES materielle(id),
    FOREIGN KEY (idPanSolaire) REFERENCES pannSolaire(id)
);