package dao;

import model.DatabaseConnection;
import model.OvertimeEntry;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OvertimeDAO {

    // Save an overtime entry
    public boolean saveOvertime(OvertimeEntry entry) throws SQLException {
        String sql = "INSERT INTO overtime_entries (employee_id, period_start, period_type, year, month, hours, created_at) VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, entry.getEmployeeId());
            stmt.setDate(2, new java.sql.Date(entry.getPeriodStart().getTime()));
            stmt.setString(3, entry.getPeriodType());
            stmt.setInt(4, entry.getYear());
            stmt.setInt(5, entry.getMonth());
            stmt.setDouble(6, entry.getHours());
            return stmt.executeUpdate() > 0;
        }
    }

    // Simple query to get entries for an employee in a month/year
    public List<OvertimeEntry> getOvertimeForEmployee(int empId, int year, int month) throws SQLException {
        String sql = "SELECT id, employee_id, period_start, period_type, year, month, hours, created_at FROM overtime_entries WHERE employee_id = ? AND year = ? AND month = ? ORDER BY period_start";
        List<OvertimeEntry> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, empId);
            stmt.setInt(2, year);
            stmt.setInt(3, month);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    OvertimeEntry e = new OvertimeEntry();
                    e.setId(rs.getInt("id"));
                    e.setEmployeeId(rs.getInt("employee_id"));
                    e.setPeriodStart(rs.getDate("period_start"));
                    e.setPeriodType(rs.getString("period_type"));
                    e.setYear(rs.getInt("year"));
                    e.setMonth(rs.getInt("month"));
                    e.setHours(rs.getDouble("hours"));
                    e.setCreatedAt(rs.getTimestamp("created_at"));
                    list.add(e);
                }
            }
        }
        return list;
    }

    // Compute week start dates for a month according to rule:
    // - If 1st of month is Mon-Fri, first week starts on 1st.
    // - If 1st is Saturday or Sunday, first week starts next Monday.
    // Then subsequent weeks start every 7 days (Monday).
    public List<Date> getWeekStartsForMonth(int year, int month) {
        List<Date> starts = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);

        int dow = cal.get(Calendar.DAY_OF_WEEK); // 1=Sun ... 7=Sat
        if (dow == Calendar.SATURDAY || dow == Calendar.SUNDAY) {
            // move to next Monday
            int add = (dow == Calendar.SATURDAY) ? 2 : 1;
            cal.add(Calendar.DAY_OF_MONTH, add);
        }
        // first start
        while (cal.get(Calendar.MONTH) == month - 1) {
            starts.add(cal.getTime());
            cal.add(Calendar.DAY_OF_MONTH, 7);
        }
        return starts;
    }

}
