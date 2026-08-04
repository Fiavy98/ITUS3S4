package db;

import java.sql.*;
import java.util.Vector;
import javax.swing.JOptionPane;

public class From_hystorique {
    public static void modify(int empno, String nom, java.util.Date date, double sal) {
        String emp = "SELECT ename, sal FROM emp WHERE empno = ?";
        String updateEmp = "UPDATE emp SET ename=?, sal=? WHERE empno=?";
        String insertHist = "INSERT INTO history_sal(empno, nom, dt, sal) VALUES (?, ?, ?, ?)";
        String triSal = "SELECT sal FROM history_sal WHERE empno=? ORDER BY dt DESC FETCH FIRST 2 ROWS ONLY";
    
        try (Connection conn = OracleConnection.getConnection()) {
    
            String oldName = null;
            double oldSal = 0;

            try (PreparedStatement ps = conn.prepareStatement(emp)) {
                ps.setInt(1, empno);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        oldName = rs.getString("ename");
                        oldSal = rs.getDouble("sal");
                    } else {
                        JOptionPane.showMessageDialog(null, "⚠️ Employé introuvable !");
                        return;
                    }
                }
            }
    
            boolean nameChanged = !oldName.equalsIgnoreCase(nom);
            boolean salChanged = Double.compare(oldSal, sal) != 0;
    
            //  Vérifier les conditions sur les anciens salaires
            double sommeDeuxDerniers = 0;
            int nbHistorique = 0;
    
            try (PreparedStatement psHist = conn.prepareStatement(triSal)) {
                psHist.setInt(1, empno);
                try (ResultSet rs = psHist.executeQuery()) {
                    while (rs.next()) {
                        sommeDeuxDerniers += rs.getDouble("sal");
                        nbHistorique++;
                    }
                }
            }
    
            // Vérification des règles
            if (nbHistorique >= 2 && sommeDeuxDerniers >= sal) {
                JOptionPane.showMessageDialog(null, "Modification echoue !");
                return;
            }
    
            if (nbHistorique == 1 && sal > (oldSal * 2)) {
                JOptionPane.showMessageDialog(null, " Modification echoue !");
                return;
            }
    
            // Mise a jour
            try (PreparedStatement psUpdate = conn.prepareStatement(updateEmp)) {
                psUpdate.setString(1, nom);
                psUpdate.setDouble(2, sal);
                psUpdate.setInt(3, empno);
    
                int lignes = psUpdate.executeUpdate();
                if (lignes == 0) {
                    JOptionPane.showMessageDialog(null, "⚠️ L’employé n'existe pas !");
                    return;
                } else {
                    System.out.println("Mise à jour réussie !");
                }
            }
    
            // INSERTION
            if (salChanged) {
                try (PreparedStatement psHistInsert = conn.prepareStatement(insertHist)) {
                    psHistInsert.setInt(1, empno);
                    psHistInsert.setString(2, nom);
                    psHistInsert.setDate(3, new java.sql.Date(date.getTime()));
                    psHistInsert.setDouble(4, sal);
                    psHistInsert.executeUpdate();
                    System.out.println("Historique ajoute");
                }
            } else if (nameChanged) {
                System.out.println("Nom changé sans modification de salaire → pas d’historique ajouté.");
            } else {
                System.out.println("Aucun changement détecté.");
            }
    
        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL : " + e.getMessage());
        }
    }
    
    
    public static Vector<Vector<Object>> hyst_sal(java.util.Date date) {
        String sql = "SELECT h.empno, h.nom, h.dt, h.sal " +
        "FROM history_sal h " +
        "WHERE h.dt = ( " +
        "    SELECT MAX(h2.dt) " +
        "    FROM history_sal h2 " +
        "    WHERE h2.empno = h.empno " +
        "      AND h2.dt <= ? " +
        ") " +
        "ORDER BY h.empno";

        Vector<Vector<Object>> data = new Vector<>();
    
        try (Connection conn = OracleConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setDate(1, new java.sql.Date(date.getTime()));
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Vector<Object> row = new Vector<>();
                    row.add(rs.getInt("empno"));
                    row.add(rs.getString("nom"));
                    row.add(rs.getDate("dt"));
                    row.add(rs.getDouble("sal"));
                    data.add(row);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return data;
    }
    
    public static Vector<String> getCol() {
        Vector<String> colNames = new Vector<>();
        colNames.add("EMPNO");
        colNames.add("ENAME");
        colNames.add("DATE");
        colNames.add("SAL");
        return colNames;
    }
    
}
