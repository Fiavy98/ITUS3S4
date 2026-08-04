package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class From_Medecin {
    public static void insMedecin(String nom,String prenom, int idUser) {
        String insert = "INSERT INTO MED_MEDECIN(NOM,PRENOM,MY_USER) VALUES (?,?,?)";
    
        try (Connection conn = OracleConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(insert)) {
    
                ps.setString(1, nom);
                ps.setString(2, prenom);
                ps.setInt(3, idUser);
    
                int lignes = ps.executeUpdate();
    
                if (lignes > 0) {
                    System.out.println("Médecin inséré !");
                } else {
                    System.out.println("⚠️ Aucun enregistrement inséré !");
                }
    
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL : " + e.getMessage());
        }
   
    }


    public static String getIdMedecin(int idUser){
        String idMed = null;
        String query = "SELECT m.ID FROM MED_MEDECIN m  JOIN USR u ON m.MY_USER = u.ID WHERE u.ID = ?";
    
        try (Connection conn = OracleConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
    
            ps.setInt(1, idUser);
    
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    idMed = rs.getString("ID");
                } else {
                    System.out.println("⚠️ Aucun médecin trouvé pour l'utilisateur : " + idUser);
                }
            }
    
        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL : " + e.getMessage());
        }
    
        return idMed;
    }
    




}
