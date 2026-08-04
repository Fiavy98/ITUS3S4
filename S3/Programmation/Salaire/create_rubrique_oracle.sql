-- CREATE TABLE rubrique for Oracle (clean, corrected)
-- Run this as the schema owner (e.g., SCOTT) or adjust OWNER.TABLE in FK.

CREATE TABLE rubrique (
  id           NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  libelle      VARCHAR2(100) NOT NULL,
  code         VARCHAR2(50),
  salaire_brut NUMBER(12,2),
  cnaps        NUMBER(7,2),
  osti         NUMBER(7,2),
  bonus        NUMBER(12,2),
  type_rub     VARCHAR2(20), -- 'gain' or 'retenu'
  mode_rub     VARCHAR2(20), -- 'fixe' or 'calculer'
  employee_id  NUMBER,
  created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- If you have an 'emp' table with empno as PK in the same schema, add FK:
-- ALTER TABLE rubrique ADD CONSTRAINT fk_rub_emp FOREIGN KEY (employee_id) REFERENCES emp(empno);

-- Notes:
-- 1) Execute this as the owner schema (SCOTT in your DatabaseConnection), otherwise prefix with OWNER.rubrique or connect as the owner.
-- 2) If your Oracle version does not support IDENTITY, create a SEQUENCE and BEFORE INSERT trigger instead.
-- Example SEQUENCE + trigger (old Oracle):
-- CREATE SEQUENCE rubrique_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
-- CREATE OR REPLACE TRIGGER trg_rubrique_id
-- BEFORE INSERT ON rubrique
-- FOR EACH ROW
-- BEGIN
--   IF :new.id IS NULL THEN
--     SELECT rubrique_seq.NEXTVAL INTO :new.id FROM dual;
--   END IF;
-- END;
-- /

-- How to run from Windows cmd (SQL*Plus):
-- sqlplus scott/tiger@localhost:1521/XE
-- @c:\Users\Eddy\Pictures\exam1\create_rubrique_oracle.sql

