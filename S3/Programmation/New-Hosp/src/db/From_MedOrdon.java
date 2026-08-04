package db;

import java.sql.*;
import java.util.Vector;

public class From_MedOrdon {
    public static String insOrd(java.util.Date dt, int nbJr, String id_medecin, String id_societe){
        String insert = "INSERT INTO MED_ORDONNANCE " +
                        "(DATY, NB_JOURS,IDMEDECIN, SOCIETEPRISEENCHARGE) " +
                        "VALUES (?, ?, ?, ?)";

        String id=null;
    
        try (Connection conn = OracleConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(insert,new String[]{"ID"})) {
    
                ps.setDate(1, new java.sql.Date(dt.getTime()));
                ps.setInt(2, nbJr);
                ps.setString(3, id_medecin);
                ps.setString(4, id_societe);
    
                int lignes = ps.executeUpdate();
    
                if (lignes > 0) {
                    System.out.println("✔ Ordonnance insérée !");

                        // Recupere id
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            id = rs.getString(1);
                        }
                    }
                }else {
                    System.out.println("⚠ Aucun enregistrement inséré !");
                }
           }
        }catch (SQLException e) {
            System.out.println("❌ Erreur SQL : " + e.getMessage());
        }

        return id;
    }
    
    public static Vector<Vector<Object>> lsOrdonnanceInterneFait() throws SQLException {
        String sql = "SELECT * FROM MED_ORDONNANCE_LIBELLE ORDER BY DATY";
    
        Vector<Vector<Object>> data = new Vector<>();
    
        try (Connection conn = OracleConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
    
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("ID"));
                row.add(rs.getDate("DATY"));
                row.add(rs.getString("MEDECIN"));
                data.add(row);
            }
        }
    
        return data;
    }

        public static Vector<String> ColoneOrdonnance() {
        Vector<String> colNames = new Vector<>();
        colNames.add("IDORDONNANCE");
        colNames.add("DATE");
        colNames.add("MEDECIN");
        return colNames;
    }

}

