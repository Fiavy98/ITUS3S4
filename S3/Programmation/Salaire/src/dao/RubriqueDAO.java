package dao;

import model.DatabaseConnection;
import model.SalaryHistory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import model.Rubrique;

public class RubriqueDAO {


    public boolean insertRubrique(int empId,
            String libelle,
            String code,
            double salaireBrut,
            double cnaps,
            double bonus,
            String type,
            String mode) throws SQLException {

    // Note: the rubrique table in this schema does not have an 'osti' column.
    String sql = "INSERT INTO rubrique (employee_id, libelle, code, salaire_brut, cnaps, bonus, type_rub, mode_rub, created_at) "
        +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, empId);
            stmt.setString(2, libelle);
            stmt.setString(3, code);
            stmt.setDouble(4, salaireBrut);
            stmt.setDouble(5, cnaps);
            stmt.setDouble(6, bonus);
            stmt.setString(7, type);
            stmt.setString(8, mode);

            return stmt.executeUpdate() > 0;
        }
    }
    public boolean addPayslipAndHistory(int empId,
        String libelle,
        String code,
        double salaireBrut,
        double cnapsValue,
        double bonus,
        String type,
        String mode,
        boolean cnapsIsPercentage,
        double overtimeHours) throws SQLException {

        // Compute payroll including overtime so we persist the real CNAPS and net salary
        dao.PayrollCalculator.PayrollResult res = dao.PayrollCalculator.computePayslipWithOvertime(
            salaireBrut, cnapsValue, 0.0, cnapsIsPercentage, false, bonus, mode, overtimeHours);

        // Persist rubrique (store the computed CNAPS amount)
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            String insertRub = "INSERT INTO rubrique (employee_id, libelle, code, salaire_brut, cnaps, bonus, type_rub, mode_rub, created_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";

            try (PreparedStatement stmt = conn.prepareStatement(insertRub)) {
                stmt.setInt(1, empId);
                stmt.setString(2, libelle);
                stmt.setString(3, code);
                stmt.setDouble(4, salaireBrut);
                stmt.setDouble(5, res.cnaps);
                stmt.setDouble(6, bonus);
                stmt.setString(7, type);
                stmt.setString(8, mode);
                stmt.executeUpdate();
            }

            SalaryHistory history = new SalaryHistory();
            history.setEmpno(empId);
            history.setSalary(salaireBrut);
            history.setNetSalary(res.netSalary);
            history.setStartDate(new java.util.Date());
            history.setEndDate(null);

            SalaryHistoryDAO histDao = new SalaryHistoryDAO();
            histDao.addSalaryHistory(history);

            conn.commit();
            return true;
        } catch (SQLException ex) {
            throw ex;
        }
    }


    public Rubrique getLatestRubrique(int empId) throws SQLException {
    String sql = "SELECT id, employee_id, libelle, code, salaire_brut, cnaps, bonus, type_rub, mode_rub, created_at " +
                     "FROM (SELECT * FROM rubrique WHERE employee_id = ? ORDER BY created_at DESC) WHERE ROWNUM = 1";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, empId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Rubrique r = new Rubrique();
                    r.setId(rs.getInt("id"));
                    r.setEmployeeId(rs.getInt("employee_id"));
                    r.setLibelle(rs.getString("libelle"));
                    r.setCode(rs.getString("code"));
                    r.setSalaireBrut(rs.getDouble("salaire_brut"));
                    r.setCnaps(rs.getDouble("cnaps"));
                    // OSTI column not present in this schema/table; default to 0 if needed elsewhere.
                    r.setBonus(rs.getDouble("bonus"));
                    r.setType(rs.getString("type_rub"));
                    r.setMode(rs.getString("mode_rub"));
                    r.setCreatedAt(rs.getTimestamp("created_at"));
                    return r;
                }
            }
        }
        return null;
    }

}
