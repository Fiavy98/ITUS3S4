package db;

import java.sql.*;
public class From_client {
    public static String getOrInsertClientId(String adresse, String idSociete) {

        String sqlVerif = "SELECT ID FROM CLIENT WHERE ADRESSE = ? AND IDSOCIETECONDITION = ?";
        String sqlInsert = "INSERT INTO CLIENT (ADRESSE, IDSOCIETECONDITION,ID) VALUES (?, ?, ?)";
    
        try (Connection conn = OracleConnection.getConnection()) {

            try (PreparedStatement ps = conn.prepareStatement(sqlVerif)) {
    
                ps.setString(1, adresse);
                ps.setString(2, idSociete);
    
                ResultSet rs = ps.executeQuery();

                //if exist
                if (rs.next()) {
                    String idClient = rs.getString("ID");
                    System.out.println("✔ Client existe");
                    return idClient;
                }
            }
    
            //not exist
            String newId = "CLI" + String.format("%05d", getNextClientSequence());

            // insert
            try (PreparedStatement psInsert = conn.prepareStatement(sqlInsert)) {
    
                psInsert.setString(1, adresse);
                psInsert.setString(2, idSociete);
                psInsert.setString(3, newId);
    
                psInsert.executeUpdate();
            }
    
            System.out.println("➕ Nouveau client inséré, ID = " + newId);
            return newId;
    
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getNextClientSequence() throws Exception {
        String sql = "SELECT seq_client.NEXTVAL FROM dual";
    
        try (Connection conn = OracleConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
    
            rs.next();
            return rs.getInt(1);
        }
    }
    
    
    
}
