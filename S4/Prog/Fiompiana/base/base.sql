CREATE DATABASE IF NOT EXISTS gestion_poulets;
USE gestion_poulets;

CREATE TABLE race (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(50) NOT NULL
);


CREATE TABLE lot (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(50) NOT NULL,
    date_entree DATE NOT NULL,
    nb_akoho_initial INT NOT NULL,
    nb_lahy INT NOT NULL DEFAULT 0,
    nb_vavy INT NOT NULL DEFAULT 0,
    race_id INT NOT NULL,
    age_semaine INT NOT NULL,
    FOREIGN KEY (race_id) REFERENCES race(id)
);


CREATE TABLE variationPoids (
    id INT AUTO_INCREMENT PRIMARY KEY,
    semaine INT NOT NULL,
    race_id INT NOT NULL,
    poids_g DECIMAL(10,2) NOT NULL, 
    sakafo_g DECIMAL(10,2) NOT NULL,  
    FOREIGN KEY (race_id) REFERENCES race(id)
);

-- 4️⃣ Table PrixRace
--la colonne prixV_kg_poulet est en g pas en kg
CREATE TABLE prixRace (
    id INT AUTO_INCREMENT PRIMARY KEY,
    race_id INT NOT NULL,
    prixU_sakafo_g DECIMAL(10,2) NOT NULL, 
    prixV_kg_poulet DECIMAL(10,2) NOT NULL,  -- -- Ici on vend au g ,pas au kg
    prixV_atody DECIMAL(10,2) NOT NULL,     
    prix_achat_poussin DECIMAL(10,2) NOT NULL,
    nbJr_fohy INT,
    max_atody_par_poule INT NOT NULL DEFAULT 0,
    FOREIGN KEY (race_id) REFERENCES race(id)
);


-- 5️⃣ Table VisiteLot
-- Pour suivre mortalité et œufs
CREATE TABLE visiteLot (
    id INT AUTO_INCREMENT PRIMARY KEY,
    lot_id INT NOT NULL,
    date_visite DATE NOT NULL,
    lahy_maty INT DEFAULT 0,
    vavy_maty INT DEFAULT 0,
    nb_atody INT DEFAULT 0,
    simba INT DEFAULT 0,          -- œufs détruits lors de la visite
    FOREIGN KEY (lot_id) REFERENCES lot(id)
);

-- 6️⃣ Table stockAtody : enregistre le stock d'œufs restant lors d'un transfert
CREATE TABLE stockAtody (
    id INT AUTO_INCREMENT PRIMARY KEY,
    lot_id INT NOT NULL,
    race_id INT NOT NULL,
    nombre INT NOT NULL,
    nbJr_fohy INT,
    date DATE NOT NULL,
    FOREIGN KEY (lot_id) REFERENCES lot(id),
    FOREIGN KEY (race_id) REFERENCES race(id)
);

-- 7️⃣ Table incubation : garde trace des œufs en incubation
CREATE TABLE incubation (
    id INT AUTO_INCREMENT PRIMARY KEY,
    lot_source INT NOT NULL,
    race_id INT NOT NULL,
    nb_oeufs INT NOT NULL,
    date_debut DATE NOT NULL,
    date_eclosion_prevue DATE NOT NULL,
    etat ENUM('incubating','hatched','failed') NOT NULL DEFAULT 'incubating',
    nb_oeufs_hatched INT DEFAULT 0,
    nb_oeufs_failed INT DEFAULT 0,
    pct_lahy INT DEFAULT 0,
    pct_vavy INT DEFAULT 0,
    FOREIGN KEY (lot_source) REFERENCES lot(id),
    FOREIGN KEY (race_id) REFERENCES race(id)
);

INSERT INTO variationPoids (semaine, race_id, poids_g, sakafo_g) VALUES 
(0,1,50,0),
(1,1,20,75),
(2,1,25,80),
(3,1,30,100),
(4,1,40,150),
(5,1,80,170),
(6,1,85,190),
(7,1,100,200),
(8,1,100,250),
(9,1,90 ,270),
(10,1,140,290),
(11,1,200,300),
(12,1,220,370),
(13,1,265,390),
(14,1,285,350),
(15,1,300,300),
(16,1,350,450),
(17,1,400,500),
(18,1,420,400),
(19,1,430,500),
(20,1,500,500),
(21,1,530,650),
(22,1,600,600),
(23,1,400,750),
(24,1,100,750),
(25,1,0,600);