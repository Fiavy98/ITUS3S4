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

-- Insert sample data
INSERT INTO tranche (nom, H_depart, H_arrive) VALUES
('matin', '06:00:00', '17:00:00'),
('avant soir', '17:00:00', '19:00:00'),
('soir', '19:00:00', '06:00:00');

INSERT INTO pannSolaire (nom, puissance, batterie) VALUES
('Panel 15W', 15, 100),
('Panel 50W', 50, 300),
('Panel 100W', 100, 600),
('Panel 150W', 150, 1000),
('Panel 200W', 200, 1200);