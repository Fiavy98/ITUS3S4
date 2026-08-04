package db;

import java.sql.*;
import java.util.Vector;

import javax.print.DocFlavor.STRING;

public class From_dept {
    public static Vector<String> lsDept() {
        String sql = "SELECT DNAME FROM DEPT ORDER BY DEPTNO";
        Vector<String> noms = new Vector<>();

        try (Connection conn = OracleConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                noms.add(rs.getString("DNAME"));
            }

        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL : " + e.getMessage());
        }

        return noms;
    }

    public static int getIdDept(String dept) {
        String sql = "SELECT DEPTNO FROM DEPT WHERE DNAME = ?";
        int id = -1; 
    
        try (Connection conn = OracleConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
            pstmt.setString(1, dept); 
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    id = rs.getInt("DEPTNO");
                }
            }
    
        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL : " + e.getMessage());
        }
    
        return id;
    }
    
    
}
