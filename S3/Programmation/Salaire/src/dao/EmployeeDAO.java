package dao;

import model.Employee;
import model.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    
    public interface RowMapper<T> {
        T map(ResultSet rs) throws SQLException;
    }

    //  Méthode pour exécuter une requête (SELECT)
    private <T> List<T> executeQuery(String sql, RowMapper<T> mapper, List<Object> params) throws SQLException {
        List<T> results = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Ajout manuel des paramètres
            if (params != null) {
                for (int i = 0; i < params.size(); i++) {
                    stmt.setObject(i + 1, params.get(i));
                }
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapper.map(rs));
                }
            }
        }

        return results;
    }

    //  Méthode pour exécuter (INSERT, UPDATE, DELETE)
    private boolean executeUpdate(String sql, List<Object> params) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (params != null) {
                for (int i = 0; i < params.size(); i++) {
                    stmt.setObject(i + 1, params.get(i));
                }
            }

            return stmt.executeUpdate() > 0;
        }
    }

    private Employee mapEmployee(ResultSet rs) throws SQLException {
        return new Employee(
                rs.getInt("empno"),
                rs.getString("ename"),
                rs.getString("job"),
                rs.getDouble("sal"),
                rs.getDate("hiredate"),
                rs.getInt("deptno")
        );
    }


    public List<Employee> getAllEmployees() throws SQLException {
        String sql = "SELECT * FROM emp ORDER BY empno";
        return executeQuery(sql, new RowMapper<Employee>() {
            public Employee map(ResultSet rs) throws SQLException {
                return mapEmployee(rs);
            }
        }, null);
    }


    public boolean addEmployee(Employee e) throws SQLException {
        String sql = "INSERT INTO emp (empno, ename, job, sal, hiredate, deptno) VALUES (?, ?, ?, ?, ?, ?)";

        List<Object> params = new ArrayList<>();
        params.add(e.getEmpno());
        params.add(e.getEname());
        params.add(e.getJob());
        params.add(e.getSal());
        params.add(new java.sql.Date(e.getHiredate().getTime()));
        params.add(e.getDeptno());

        boolean added = executeUpdate(sql, params);
        if (added) addInitialSalaryHistory(e);
        return added;
    }


    private void addInitialSalaryHistory(Employee e) throws SQLException {
        String insertWithNet = "INSERT INTO salary_history (empno, salary, net_salary, start_date, end_date) VALUES (?, ?, ?, ?, NULL)";
        List<Object> paramsWithNet = List.of(e.getEmpno(), e.getSal(), e.getSal(), new java.sql.Date(e.getHiredate().getTime()));
        try {
            executeUpdate(insertWithNet, paramsWithNet);
        } catch (SQLException ex) {
            // fallback for DB without net_salary column
            String insertNoNet = "INSERT INTO salary_history (empno, salary, start_date, end_date) VALUES (?, ?, ?, NULL)";
            List<Object> params = List.of(e.getEmpno(), e.getSal(), new java.sql.Date(e.getHiredate().getTime()));
            executeUpdate(insertNoNet, params);
        }
    }


    public boolean updateEmployeeSalary(int empno, double newSalary) throws SQLException {
        String sql = "UPDATE emp SET sal = ? WHERE empno = ?";
        return executeUpdate(sql, List.of(newSalary, empno));
    }

    public boolean updateEmployee(model.Employee e) throws SQLException {
        String sql = "UPDATE emp SET ename = ?, job = ?, hiredate = ?, deptno = ? WHERE empno = ?";

        List<Object> params = new ArrayList<>();
        params.add(e.getEname());
        params.add(e.getJob());
        params.add(new java.sql.Date(e.getHiredate().getTime()));
        params.add(e.getDeptno());
        params.add(e.getEmpno());

        return executeUpdate(sql, params);
    }


    public boolean deleteEmployee(int empno) throws SQLException {
    // If your DB schema allows empno to be set to NULL and you want to keep the history,
    // you can use an UPDATE to nullify the foreign key instead. Many schemas prefer
    // to keep history and set empno = NULL (in that case make the column nullable and
    // use an FK with ON DELETE SET NULL). If that's not done, the UPDATE below fails
    // with ORA-01407. A safe alternative is to delete salary_history rows for the
    // removed employee.
    String deleteSalaryHistory = "DELETE FROM salary_history WHERE empno = ?";
    String nullifyRubrique = "UPDATE rubrique SET employee_id = NULL WHERE employee_id = ?";
        String delEmp = "DELETE FROM emp WHERE empno = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
             try (PreparedStatement stmt1 = conn.prepareStatement(deleteSalaryHistory);
                 PreparedStatement stmt2 = conn.prepareStatement(nullifyRubrique);
                 PreparedStatement stmt3 = conn.prepareStatement(delEmp)) {

             // Remove salary history rows for this employee (avoid ORA-01407 on NOT NULL empno)
             stmt1.setInt(1, empno);
             stmt1.executeUpdate();

                stmt2.setInt(1, empno);
                stmt2.executeUpdate();

                stmt3.setInt(1, empno);
                int affected = stmt3.executeUpdate();

                conn.commit();
                return affected > 0;
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }
        }
    }


    public List<String> getDistinctJobs() throws SQLException {
        String sql = "SELECT DISTINCT job FROM emp ORDER BY job";
        return executeQuery(sql, rs -> rs.getString("job"), null);
    }


    public List<Integer> getDistinctDeptnos() throws SQLException {
        String sql = "SELECT DISTINCT deptno FROM emp ORDER BY deptno";
        return executeQuery(sql, rs -> rs.getInt("deptno"), null);
    }

    public Employee getEmployeeById(int empno) throws SQLException {
        String sql = "SELECT * FROM emp WHERE empno = ?";
        List<Employee> list = executeQuery(sql, this::mapEmployee, List.of(empno));
        return list.isEmpty() ? null : list.get(0);
    }

    public boolean isEmployeeExists(int empno) throws SQLException {
        String sql = "SELECT 1 FROM emp WHERE empno = ?";
        List<Integer> list = executeQuery(sql, rs -> 1, List.of(empno));
        return !list.isEmpty();
    }
}
