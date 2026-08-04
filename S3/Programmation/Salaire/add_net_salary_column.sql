-- Add a net_salary column to salary_history if not present.
-- Adjust type depending on your RDBMS. For Oracle use NUMBER, for others use DECIMAL or REAL.

-- Oracle example:
-- ALTER TABLE salary_history ADD (net_salary NUMBER(15,2));

-- Generic SQL (Postgres/MySQL):
-- ALTER TABLE salary_history ADD COLUMN net_salary DOUBLE;

/*
 Run the appropriate command for your database.



*/
-- Increase precision for employee salary
ALTER TABLE emp MODIFY (sal NUMBER(18,2));

-- Increase precision for salary_history
ALTER TABLE salary_history MODIFY (salary NUMBER(18,2));
ALTER TABLE salary_history MODIFY (net_salary NUMBER(18,2));

-- Increase precision for rubrique
ALTER TABLE rubrique MODIFY (salaire_brut NUMBER(18,2));
ALTER TABLE rubrique MODIFY (bonus NUMBER(18,2));
ALTER TABLE rubrique MODIFY (cnaps NUMBER(12,2));
ALTER TABLE rubrique MODIFY (osti NUMBER(12,2));