package db;

import java.sql.*;
public class From_client {
    public static String getOrInsertClientId(String nom,String tel,String adresse, String idSociete) {

        String sqlVerif = "SELECT ID FROM CLIENT WHERE TELEPHONE = ? AND IDSOCIETECONDITION = ?";
        String sqlInsert = "INSERT INTO CLIENT (NOM, TELEPHONE, ADRESSE, IDSOCIETECONDITION,ID) VALUES (?, ?, ?, ?,?)";
    
        try (Connection conn = OracleConnection.getConnection()) {

            try (PreparedStatement ps = conn.prepareStatement(sqlVerif)) {
    
                ps.setString(1, tel);
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
    
                psInsert.setString(1, nom);
                psInsert.setString(2, tel);
                psInsert.setString(3, adresse);
                psInsert.setString(4, idSociete);
                psInsert.setString(5, newId);
    
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
