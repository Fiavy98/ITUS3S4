-- Active: 1772894277678@@127.0.0.1@1433@akoho
CREATE DATABASE PuissanceElectriqueDB;

END;
GO

USE PuissanceElectriqueDB;
GO

IF OBJECT_ID(N'dbo.Utilisations', N'U') IS NOT NULL
DROP TABLE dbo.Utilisations;

IF OBJECT_ID(N'dbo.Configs', N'U') IS NOT NULL
DROP TABLE dbo.Configs;

IF OBJECT_ID(N'dbo.Materiels', N'U') IS NOT NULL
DROP TABLE dbo.Materiels;
GO

CREATE TABLE dbo.Materiels (
    Id INT IDENTITY(1, 1) NOT NULL CONSTRAINT PK_Materiels PRIMARY KEY,
    Nom NVARCHAR(100) NOT NULL CONSTRAINT UQ_Materiels_Nom UNIQUE,
    Puissance_W DECIMAL(10, 2) NOT NULL CONSTRAINT CK_Materiels_Puissance CHECK (Puissance_W >= 0)
);
GO

CREATE TABLE dbo.Configs (
    Id INT IDENTITY(1, 1) NOT NULL CONSTRAINT PK_Configs PRIMARY KEY,
    HeureDebut DECIMAL(4, 2) NOT NULL,
    HeureFin DECIMAL(4, 2) NOT NULL,
    Puissance DECIMAL(6, 4) NOT NULL,
    CONSTRAINT CK_Configs_Heures CHECK (
        HeureDebut >= 0
        AND HeureDebut <= 24
        AND HeureFin >= 0
        AND HeureFin <= 24
    ),
    CONSTRAINT CK_Configs_Puissance CHECK (Puissance >= 0)
);
GO

INSERT INTO
    dbo.Materiels (Nom, Puissance_W)
SELECT v.Nom, v.Puissance_W
FROM (
        VALUES (N'tv', 1), (N'pc', 50), (N'clim', 1500), (N'frigo', 80), (N'four', 2000), (N'fer a repasser', 1500)
    ) AS v (Nom, Puissance_W)
WHERE
    NOT EXISTS (
        SELECT 1
        FROM dbo.Materiels m
        WHERE
            m.Nom = v.Nom
    );
GO

INSERT INTO
    dbo.Configs (
        HeureDebut,
        HeureFin,
        Puissance
    )
SELECT v.HeureDebut, v.HeureFin, v.Puissance
FROM (
        VALUES (6, 17, 0.4), (17, 19, 0.2), (19, 6, 0.0)
    ) AS v (
        HeureDebut, HeureFin, Puissance
    )
WHERE
    NOT EXISTS (
        SELECT 1
        FROM dbo.Configs c
        WHERE
            c.HeureDebut = v.HeureDebut
            AND c.HeureFin = v.HeureFin
    );
GO