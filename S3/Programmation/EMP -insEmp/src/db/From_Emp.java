package db;

import java.sql.*;
import java.util.Vector;

public class From_Emp {

    //Inserer Employer
    public static void insEmp(int empno, String ename, String job, double sal, java.util.Date hiredate, int deptno) {
        String verif = "SELECT COUNT(*) FROM EMP WHERE EMPNO = ?";
        String insert = "INSERT INTO EMP (EMPNO, ENAME, JOB, SAL, HIREDATE, DEPTNO) VALUES (?, ?, ?, ?, ?, ?)";
    
        try (Connection conn = OracleConnection.getConnection()) {
    
            // Vérifier si l'EMPNO existe déjà
            try (PreparedStatement getStmt = conn.prepareStatement(verif)) {
                getStmt.setInt(1, empno);
    
                try (ResultSet rs = getStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        System.out.println("⚠️ L’employé avec EMPNO " + empno + " existe déjà !");
                        return;
                    }
                }
            }
    
            // Insérer un nouvel employé
            try (PreparedStatement instmt = conn.prepareStatement(insert)) {
                instmt.setInt(1, empno);
                instmt.setString(2, ename);
                instmt.setString(3, job);
                instmt.setDouble(4, sal);
                instmt.setDate(5, new java.sql.Date(hiredate.getTime()));
                instmt.setInt(6, deptno);
    
                int lignes = instmt.executeUpdate();
    
                if (lignes > 0) {
                    System.out.println("✅ Employé inséré avec succès !");
                } else {
                    System.out.println("⚠️ Aucun enregistrement inséré !");
                }
            }
    
        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL : " + e.getMessage());
        }
    }
    
    
    // Méthode qui retourne toutes les lignes de EMP sous forme de Vector<Vector<Object>>
    public static Vector<Vector<Object>> lsEmp() throws SQLException {
        String sql = "SELECT empno, ename, job, sal, deptno FROM emp";
        Vector<Vector<Object>> data = new Vector<>();

        try (Connection conn = OracleConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("empno"));
                row.add(rs.getString("ename"));
                row.add(rs.getString("job"));
                row.add(rs.getDouble("sal"));
                row.add(rs.getInt("deptno"));
                data.add(row);
            }
        }
        return data;
    }

    // Méthode pour récupérer les noms de colonnes
    public static Vector<String> getColumnNames() {
        Vector<String> colNames = new Vector<>();
        colNames.add("EMPNO");
        colNames.add("ENAME");
        colNames.add("JOB");
        colNames.add("SAL");
        colNames.add("DEPTNO");
        return colNames;
    }


}
