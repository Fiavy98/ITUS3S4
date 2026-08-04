-- Création de la base de données MySQL pour PuissanceElectriqueDB

CREATE DATABASE IF NOT EXISTS PuissanceElectriqueDB;

USE PuissanceElectriqueDB;

-- Suppression des tables si elles existent
DROP TABLE IF EXISTS Utilisations;
DROP TABLE IF EXISTS Configs;
DROP TABLE IF EXISTS Materiels;

-- Création de la table Materiels
CREATE TABLE Materiels (
    Id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    Nom VARCHAR(100) NOT NULL UNIQUE,
    Puissance_W DECIMAL(10, 2) NOT NULL CHECK (Puissance_W >= 0)
);

-- Création de la table Configs
CREATE TABLE Configs (
    Id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    HeureDebut DECIMAL(4, 2) NOT NULL,
    HeureFin DECIMAL(4, 2) NOT NULL,
    Puissance DECIMAL(6, 4) NOT NULL,
    CHECK (
        HeureDebut >= 0
        AND HeureDebut <= 24
        AND HeureFin >= 0
        AND HeureFin <= 24
    ),
    CHECK (Puissance >= 0)
);

-- Insertion des données dans Materiels (seulement si elles n'existent pas)
INSERT INTO Materiels (Nom, Puissance_W)
SELECT v.Nom, v.Puissance_W
FROM (
    SELECT 'tv' AS Nom, 1.00 AS Puissance_W
    UNION ALL
    SELECT 'pc', 50.00
    UNION ALL
    SELECT 'clim', 1500.00
    UNION ALL
    SELECT 'frigo', 80.00
    UNION ALL
    SELECT 'four', 2000.00
    UNION ALL
    SELECT 'fer a repasser', 1500.00
) AS v
WHERE NOT EXISTS (
    SELECT 1 FROM Materiels m WHERE m.Nom = v.Nom
);

-- Insertion des données dans Configs (seulement si elles n'existent pas)
INSERT INTO Configs (HeureDebut, HeureFin, Puissance)
SELECT v.HeureDebut, v.HeureFin, v.Puissance
FROM (
    SELECT 6.00 AS HeureDebut, 17.00 AS HeureFin, 0.4000 AS Puissance
    UNION ALL
    SELECT 17.00, 19.00, 0.2000
    UNION ALL
    SELECT 19.00, 6.00, 0.0000
) AS v
WHERE NOT EXISTS (
    SELECT 1 FROM Configs c
    WHERE c.HeureDebut = v.HeureDebut
    AND c.HeureFin = v.HeureFin
);atelier_bois_mada.docx