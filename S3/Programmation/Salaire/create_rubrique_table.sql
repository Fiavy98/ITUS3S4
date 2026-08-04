-- SQL to create the 'rubrique' table.
-- Adjust types and AUTOINCREMENT syntax to match your DB (SQLite/H2/MySQL/Postgres).
-- Example written to be compatible with SQLite/H2 (INTEGER PRIMARY KEY AUTOINCREMENT).
CREATE TABLE rubrique (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    libelle VARCHAR2(100) NOT NULL,
    code VARCHAR2(50),
    salaire_brut NUMBER(12,2),
    cnaps NUMBER(5,2),
    ostie NUMBER(5,2),
    bonus NUMBER(12,2),
    type VARCHAR2(20),
    mode VARCHAR2(20),
    employe_id NUMBER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
SELECT * FROM rubrique;



DROP TABLE IF EXISTS rubrique;


-- If you use a DB that supports foreign keys and have an employee table,
-- uncomment and adjust the FK line below:
-- ALTER TABLE rubrique ADD CONSTRAINT fk_employee FOREIGN KEY (employee_id) REFERENCES employee(id);
