CREATE DATABASE solar_db;
GO

USE solar_db;
GO

CREATE TABLE tranche (
    id INT IDENTITY(1,1) PRIMARY KEY,
    nom VARCHAR(50),
    H_depart TIME,
    H_arrive TIME
);

CREATE TABLE materielle (
    id INT IDENTITY(1,1) PRIMARY KEY,
    nom VARCHAR(100),
    heure FLOAT,
    idTranche INT,
    watt FLOAT,
    FOREIGN KEY (idTranche) REFERENCES tranche(id)
);

CREATE TABLE pannSolaire (
    id INT IDENTITY(1,1) PRIMARY KEY,
    nom VARCHAR(100),
    puissance FLOAT,
    batterie FLOAT
);

CREATE TABLE destination (
    id INT IDENTITY(1,1) PRIMARY KEY,
    idMaterielle INT,
    idPanSolaire INT,
    FOREIGN KEY (idMaterielle) REFERENCES materielle(id),
    FOREIGN KEY (idPanSolaire) REFERENCES pannSolaire(id)
);

-- Executer : docker exec -it <container> /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P "password"