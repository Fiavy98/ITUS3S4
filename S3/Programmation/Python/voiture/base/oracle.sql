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
    largeur      NUMBER(5,2), -- en cm
    etat         VARCHAR2(50),
    CONSTRAINT fk_trou_route FOREIGN KEY (id_route) REFERENCES route(id)
);


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