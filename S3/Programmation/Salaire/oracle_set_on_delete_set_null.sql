-- Script to change FK on salary_history.empno to ON DELETE SET NULL
-- Usage: run as the owner of the tables (e.g., SCOTT) in SQL*Plus or SQL Developer.
-- This script:
-- 1) shows diagnostic queries to inspect current constraints and nullability
-- 2) makes empno nullable (if needed)
-- 3) drops the existing FK referencing EMP (if any)
-- 4) recreates the FK with ON DELETE SET NULL
-- IMPORTANT: backup the schema before running in production.

PROMPT --- Diagnostic: current user
SELECT USER FROM DUAL;

PROMPT --- Check tables existence
SELECT owner, table_name FROM all_tables WHERE table_name IN ('EMP','SALARY_HISTORY','RUBRIQUE');

PROMPT --- Check empno column nullability
SELECT column_name, nullable FROM user_tab_columns WHERE table_name = 'SALARY_HISTORY' AND column_name = 'EMPNO';

PROMPT --- Find FK constraint name(s) on salary_history that reference EMP
SELECT uc.constraint_name, ucc.column_name, pk.table_name AS referenced_table
FROM user_constraints uc
JOIN user_cons_columns ucc ON uc.constraint_name = ucc.constraint_name
LEFT JOIN user_constraints pk ON uc.r_constraint_name = pk.constraint_name
WHERE uc.table_name = 'SALARY_HISTORY' AND uc.constraint_type = 'R' AND pk.table_name = 'EMP';

PROMPT --- Make empno nullable (if it's currently NOT NULL)
ALTER TABLE salary_history MODIFY (empno NULL);

PROMPT --- Drop existing FK constraint(s) that reference EMP (will drop only those found above)
DECLARE
  v_count INTEGER := 0;
BEGIN
  FOR r IN (
    SELECT uc.constraint_name
    FROM user_constraints uc
    LEFT JOIN user_constraints pk ON uc.r_constraint_name = pk.constraint_name
    WHERE uc.table_name = 'SALARY_HISTORY' AND uc.constraint_type = 'R' AND pk.table_name = 'EMP'
  ) LOOP
    BEGIN
      EXECUTE IMMEDIATE 'ALTER TABLE salary_history DROP CONSTRAINT ' || r.constraint_name;
      v_count := v_count + 1;
    EXCEPTION WHEN OTHERS THEN
      DBMS_OUTPUT.PUT_LINE('Failed to drop constraint ' || r.constraint_name || ': ' || SQLERRM);
    END;
  END LOOP;
  DBMS_OUTPUT.PUT_LINE('Dropped ' || v_count || ' constraint(s) referencing EMP on SALARY_HISTORY.');
END;
/

PROMPT --- Recreate FK with ON DELETE SET NULL
ALTER TABLE salary_history
  ADD CONSTRAINT fk_salary_history_emp FOREIGN KEY (empno)
  REFERENCES emp(empno)
  ON DELETE SET NULL;

PROMPT --- Verify new FK
SELECT uc.constraint_name, uc.constraint_type, pk.table_name as referenced_table
FROM user_constraints uc
LEFT JOIN user_constraints pk ON uc.r_constraint_name = pk.constraint_name
WHERE uc.table_name = 'SALARY_HISTORY' AND uc.constraint_type = 'R';

PROMPT --- Verify empno nullability and columns
SELECT column_name, data_type, data_precision, data_scale, nullable
FROM user_tab_columns
WHERE table_name = 'SALARY_HISTORY' AND column_name = 'EMPNO';

PROMPT --- Done. Please test by deleting an employee and checking salary_history.empno becomes NULL.
