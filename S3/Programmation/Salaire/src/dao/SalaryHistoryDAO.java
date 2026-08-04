package dao;

import model.SalaryHistory;
import model.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SalaryHistoryDAO {

    //  RowMapper 
    public interface RowMapper<T> {
        T map(ResultSet rs) throws SQLException;
    }

    private <T> List<T> executeQuery(String sql, RowMapper<T> mapper, Object... params) throws SQLException {
        List<T> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    stmt.setObject(i + 1, params[i]);
                }
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapper.map(rs));
                }
            }
        }
        return list;
    }

    // Méthode générique pour INSERT / UPDATE / DELETE
    private boolean executeUpdate(String sql, Object... params) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    stmt.setObject(i + 1, params[i]);
                }
            }

            return stmt.executeUpdate() > 0;
        }
    }


    private SalaryHistory mapSalaryHistoryWithNet(ResultSet rs) throws SQLException {
        return new SalaryHistory(
                rs.getInt("empno"),
                rs.getDouble("salary"),
                rs.getDouble("net_salary"),
                rs.getDate("start_date"),
                rs.getDate("end_date"),
                rs.getString("ename")
        );
    }

    private SalaryHistory mapSalaryHistoryWithoutNet(ResultSet rs) throws SQLException {
        // fallback when net_salary column is not present in the DB
        return new SalaryHistory(
                rs.getInt("empno"),
                rs.getDouble("salary"),
                rs.getDate("start_date"),
                rs.getDate("end_date"),
                rs.getString("ename")
        );
    }


    public List<SalaryHistory> getSalaryHistoryByDate(java.util.Date filterDate) throws SQLException {
      
        String sqlWithNet = "SELECT sh.empno, sh.salary, sh.net_salary, sh.start_date, sh.end_date, e.ename " +
                "FROM salary_history sh " +
                "JOIN emp e ON sh.empno = e.empno " +
                "WHERE sh.start_date = (SELECT MAX(s2.start_date) FROM salary_history s2 " +
                "    WHERE s2.empno = sh.empno AND s2.start_date <= ? AND (s2.end_date IS NULL OR s2.end_date >= ?)) " +
                "AND (? BETWEEN sh.start_date AND NVL(sh.end_date, TO_DATE('9999-12-31','YYYY-MM-DD'))) " +
                "ORDER BY sh.empno";
        // Fallback query without net_salary
        String sqlWithoutNet = "SELECT sh.empno, sh.salary, sh.start_date, sh.end_date, e.ename " +
                "FROM salary_history sh " +
                "JOIN emp e ON sh.empno = e.empno " +
                "WHERE sh.start_date = (SELECT MAX(s2.start_date) FROM salary_history s2 " +
                "    WHERE s2.empno = sh.empno AND s2.start_date <= ? AND (s2.end_date IS NULL OR s2.end_date >= ?)) " +
                "AND (? BETWEEN sh.start_date AND NVL(sh.end_date, TO_DATE('9999-12-31','YYYY-MM-DD'))) " +
                "ORDER BY sh.empno";

        java.sql.Date sqlDate = new java.sql.Date(filterDate.getTime());
        try {
            return executeQuery(sqlWithNet, this::mapSalaryHistoryWithNet, sqlDate, sqlDate, sqlDate);
        } catch (SQLException ex) {
            return executeQuery(sqlWithoutNet, this::mapSalaryHistoryWithoutNet, sqlDate, sqlDate, sqlDate);
        }
    }


    public List<SalaryHistory> getCompleteSalaryHistory(int empno) throws SQLException {
        String sqlWithNet = "SELECT sh.empno, sh.salary, sh.net_salary, sh.start_date, sh.end_date, e.ename " +
                "FROM salary_history sh " +
                "JOIN emp e ON sh.empno = e.empno " +
                "WHERE sh.empno = ? " +
                "ORDER BY sh.start_date";

        String sqlWithoutNet = "SELECT sh.empno, sh.salary, sh.start_date, sh.end_date, e.ename " +
                "FROM salary_history sh " +
                "JOIN emp e ON sh.empno = e.empno " +
                "WHERE sh.empno = ? " +
                "ORDER BY sh.start_date";

        try {
            return executeQuery(sqlWithNet, this::mapSalaryHistoryWithNet, empno);
        } catch (SQLException ex) {
            return executeQuery(sqlWithoutNet, this::mapSalaryHistoryWithoutNet, empno);
        }
    }


    public boolean addSalaryHistory(SalaryHistory history) throws SQLException {
        return addSalaryHistory(history, true);
    }

   public boolean addSalaryHistory(SalaryHistory history, boolean autoClosePrevious) throws SQLException {
    try (Connection conn = DatabaseConnection.getConnection()) {
        conn.setAutoCommit(false);

        
        if (autoClosePrevious) {
            java.sql.Date prevEndDate = new java.sql.Date(history.getStartDate().getTime() - 24*60*60*1000);
            String updateSql = "UPDATE salary_history SET end_date = ? WHERE empno = ? AND end_date IS NULL";
            try (PreparedStatement stmt = conn.prepareStatement(updateSql)) {
                stmt.setDate(1, prevEndDate);
                stmt.setInt(2, history.getEmpno());
                stmt.executeUpdate();
            }
        }

   
        String insertSqlWithNet = "INSERT INTO salary_history (empno, salary, net_salary, start_date, end_date) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertSqlWithNet)) {
            stmt.setInt(1, history.getEmpno());
            stmt.setDouble(2, history.getSalary());
            stmt.setDouble(3, history.getNetSalary());
            stmt.setDate(4, new java.sql.Date(history.getStartDate().getTime()));
            stmt.setDate(5, history.getEndDate() != null ?
                    new java.sql.Date(history.getEndDate().getTime()) : null);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            // If the database doesn't have net_salary column, retry without it
            String insertSqlNoNet = "INSERT INTO salary_history (empno, salary, start_date, end_date) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt2 = conn.prepareStatement(insertSqlNoNet)) {
                stmt2.setInt(1, history.getEmpno());
                stmt2.setDouble(2, history.getSalary());
                stmt2.setDate(3, new java.sql.Date(history.getStartDate().getTime()));
                stmt2.setDate(4, history.getEndDate() != null ?
                        new java.sql.Date(history.getEndDate().getTime()) : null);
                stmt2.executeUpdate();
            }
        }

        conn.commit();
        return true;
    } catch (SQLException e) {
        throw e; 
    }
}

 
    public boolean updateCurrentSalaryEndDate(int empno, java.util.Date endDate) throws SQLException {
        String sql = "UPDATE salary_history SET end_date = ? WHERE empno = ? AND end_date IS NULL";
        return executeUpdate(sql, new java.sql.Date(endDate.getTime()), empno);
    }


    public double getCurrentSalary(int empno) throws SQLException {
        String sql = "SELECT salary FROM salary_history WHERE empno = ? AND end_date IS NULL";
        List<Double> result = executeQuery(sql, rs -> rs.getDouble("salary"), empno);
        return result.isEmpty() ? 0 : result.get(0);
    }

    /**
     * Update the net_salary for the active salary_history row for the given employee covering the given date.
     * If no matching row is found, the update does nothing and returns false.
     */
    public boolean updateNetForEmployeeAtDate(int empno, java.util.Date date, double netSalary) throws SQLException {
        String updateSql = "UPDATE salary_history SET net_salary = ? WHERE empno = ? AND start_date = (" +
                "SELECT MAX(s2.start_date) FROM salary_history s2 WHERE s2.empno = ? AND s2.start_date <= ? AND (s2.end_date IS NULL OR s2.end_date >= ?))";

        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateSql)) {
            stmt.setDouble(1, netSalary);
            stmt.setInt(2, empno);
            stmt.setInt(3, empno);
            stmt.setDate(4, sqlDate);
            stmt.setDate(5, sqlDate);
            return stmt.executeUpdate() > 0;
        }
    }
}
