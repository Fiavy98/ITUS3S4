package db;

import java.sql.*;

public class From_MedOrdon {
    public static String insOrd(java.util.Date dt, int nbJr, String obs, String id_medecin, String id_societe){
        String insert = "INSERT INTO MED_ORDONNANCE " +
                        "(DATY, NB_JOURS, OBSERVATION, IDMEDECIN, SOCIETEPRISEENCHARGE) " +
                        "VALUES (?, ?, ?, ?, ?)";

        String id=null;
    
        try (Connection conn = OracleConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(insert,new String[]{"ID"})) {
    
                ps.setDate(1, new java.sql.Date(dt.getTime()));
                ps.setInt(2, nbJr);
                ps.setString(3, obs);
                ps.setString(4, id_medecin);
                ps.setString(5, id_societe);
    
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
    

}

