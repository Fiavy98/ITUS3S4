-- Oracle: add net_salary column to salary_history
ALTER TABLE salary_history ADD (net_salary NUMBER(15,2));

-- After running, verify with:
-- SELECT column_name, data_type FROM user_tab_columns WHERE table_name = 'SALARY_HISTORY';
