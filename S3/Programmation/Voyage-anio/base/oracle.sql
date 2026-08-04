Connect tsinjo/maintso
-- Table VILLE
CREATE TABLE ville (
    id   NUMBER PRIMARY KEY, 
    nom  VARCHAR2(100) NOT NULL
);

-- Table ROUTE
CREATE TABLE route (
    id NUMBER PRIMARY KEY,
    rn VARCHAR2(50) NOT NULL,
    IdVille_depart NUMBER NOT NULL,
    idVille_arriver NUMBER NOT NULL,
    longueur_km NUMBER(6,2),   
    CONSTRAINT fk_route_depart FOREIGN KEY (idVille_depart) REFERENCES ville(id),
    CONSTRAINT fk_route_arrivee FOREIGN KEY (idVille_arriver) REFERENCES ville(id)
);

-- Table ROUTE_VILLE
CREATE TABLE route_ville (
    id NUMBER PRIMARY KEY,
    id_route    NUMBER NOT NULL,
    id_ville    NUMBER NOT NULL,
    position_km NUMBER(6,2),
    CONSTRAINT fk_route FOREIGN KEY (id_route) REFERENCES route(id),
    CONSTRAINT fk_ville FOREIGN KEY (id_ville) REFERENCES ville(id)
);

-- Table TROU
CREATE TABLE trou (
    id           NUMBER PRIMARY KEY,
    id_route     NUMBER NOT NULL,
    position_km  NUMBER(6,2),
    profondeur   NUMBER(5,2), -- en cm
    surface      NUMBER(5,2), -- en m2
    CONSTRAINT fk_trou_route FOREIGN KEY (id_route) REFERENCES route(id)
);

ALTER TABLE trou ADD status VARCHAR2(20) DEFAULT 'non_repare';
ALTER TABLE trou ADD nom VARCHAR2(20);

CREATE TABLE Type_route(
    id NUMBER PRIMARY KEY,
    nom VARCHAR2(50) NOT NULL
);

-- Table REPARER

CREATE TABLE reparer(
    id NUMBER PRIMARY KEY,
    id_trou NUMBER NOT NULL,
    id_route NUMBER NOT NULL,
    id_type_route NUMBER NOT NULL,
    date_reparation DATE,
    description VARCHAR2(255),
    CONSTRAINT fk_reparer_trou FOREIGN KEY (id_trou) REFERENCES trou(id),
    CONSTRAINT fk_reparer_type_route FOREIGN KEY (id_type_route) REFERENCES type_route(id)
);

CREATE TABLE Prix_reparation(
    id NUMBER PRIMARY KEY,
    id_reparer NUMBER NOT NULL,
    prix NUMBER(10,2),
    CONSTRAINT fk_prix_reparer FOREIGN KEY (id_reparer) REFERENCES reparer(id)
);

CREATE TABLE trou_repare (
    id NUMBER PRIMARY KEY,
    id_trou NUMBER NOT NULL,
    date_reparation DATE NOT NULL,
    id_reparer NUMBER, -- optionnel si tu veux relier à la table reparer
    CONSTRAINT fk_trou_repare_trou FOREIGN KEY (id_trou) REFERENCES trou(id)
);


CREATE TABLE intervroute (
    id NUMBER PRIMARY KEY,
    rn VARCHAR2(50) NOT NULL,        
    depart_km NUMBER(6,2) NOT NULL,    
    arrive_km NUMBER(6,2) NOT NULL,    
    pluie NUMBER(4,2) NOT NULL,     
    CONSTRAINT chk_km_interval CHECK (arrive_km > depart_km)
);
ALTER TABLE intervroute ADD nom VARCHAR2(100);

--================SEQUENCE============
CREATE SEQUENCE seq_ville
START WITH 1
INCREMENT BY 1
NOCACHE;

CREATE OR REPLACE TRIGGER trg_ville_id
BEFORE INSERT ON ville
FOR EACH ROW
BEGIN
  IF :NEW.id IS NULL THEN
    SELECT seq_ville.NEXTVAL INTO :NEW.id FROM dual;
  END IF;
END;
/

--route
CREATE SEQUENCE seq_route
START WITH 1
INCREMENT BY 1
NOCACHE;

CREATE OR REPLACE TRIGGER trg_route_id
BEFORE INSERT ON ville
FOR EACH ROW
BEGIN
  IF :NEW.id IS NULL THEN
    SELECT seq_route.NEXTVAL INTO :NEW.id FROM dual;
  END IF;
END;
/

--route ville
CREATE SEQUENCE seq_routeVile
START WITH 1
INCREMENT BY 1
NOCACHE;

CREATE OR REPLACE TRIGGER trg_route_id
BEFORE INSERT ON route_ville
FOR EACH ROW
BEGIN
  IF :NEW.id IS NULL THEN
    SELECT seq_routeVile.NEXTVAL INTO :NEW.id FROM dual;
  END IF;
END;
/

--Trou
CREATE SEQUENCE seq_trou
START WITH 1
INCREMENT BY 1
NOCACHE;

CREATE OR REPLACE TRIGGER trg_route_id
BEFORE INSERT ON route_trou
FOR EACH ROW
BEGIN
  IF :NEW.id IS NULL THEN
    SELECT seq_trou.NEXTVAL INTO :NEW.id FROM dual;
  END IF;
END;
/

--Reparer
CREATE SEQUENCE seq_reparer
START WITH 1
INCREMENT BY 1 
NOCACHE;
CREATE OR REPLACE TRIGGER trg_reparer_id
BEFORE INSERT ON reparer
FOR EACH ROW
BEGIN
  IF :NEW.id IS NULL THEN
    SELECT seq_reparer.NEXTVAL INTO :NEW.id FROM dual;
  END IF;
END;
/ 

--Prix_reparation
CREATE SEQUENCE seq_prix_reparation
START WITH 1
INCREMENT BY 1
NOCACHE;
CREATE OR REPLACE TRIGGER trg_prix_reparation_id
BEFORE INSERT ON prix_reparation
FOR EACH ROW
BEGIN
  IF :NEW.id IS NULL THEN
    SELECT seq_prix_reparation.NEXTVAL INTO :NEW.id FROM dual;
  END IF;
END;
/

COMMIT;

-- Trou réparé
CREATE SEQUENCE seq_trou_repare
START WITH 1
INCREMENT BY 1
NOCACHE;  

CREATE OR REPLACE TRIGGER trg_trou_repare_id
BEFORE INSERT ON trou_repare
FOR EACH ROW
BEGIN
  IF :NEW.id IS NULL THEN
    SELECT seq_trou_repare.NEXTVAL INTO :NEW.id FROM dual;
  END IF;
END;
/
COMMIT;

-- Intervroute
CREATE SEQUENCE intervroute_seq
START WITH 1
INCREMENT BY 1
NOCACHE;

CREATE OR REPLACE TRIGGER trg_intervroute_id
BEFORE INSERT ON intervroute
FOR EACH ROW
BEGIN
  IF :NEW.id IS NULL THEN
    SELECT intervroute_seq.NEXTVAL INTO :NEW.id FROM dual;
  END IF;
END;
/
COMMIT;
--===================================ORACLE===========================================---

