package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class From_societe {
    public static Vector<String[]> lsSociete(){
        String sql="SELECT * FROM SOCIETECONDITION";
        Vector<String[]> societe = new Vector<>();

        try(Connection conn=OracleConnection.getConnection()){
            PreparedStatement ps=conn.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
     
            while (rs.next()) {
                String id=rs.getString("ID");
                String nom=rs.getString("SOCIETE");
                societe.add(new String[]{id, nom});  
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL : " + e.getMessage());
        }

        return societe;
    }

    public static String getIdSociete(String form_nom) {
        String idsos = null;
        String sql = "SELECT ID FROM SOCIETECONDITION WHERE  SOCIETE = ?";
    
        try (Connection conn = OracleConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
    
            ps.setString(1, form_nom);         
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    idsos = rs.getString("ID"); 
                }
            }
    
        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL : " + e.getMessage());
        }
    
        return idsos;  
    }



    public static void insClientSos(String idClient){
        String insert = "INSERT INTO SOCIETECONDITION(IDCLIENT) VALUES (?)";
        try (Connection conn = OracleConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(insert)) {
                ps.setString(1, idClient);
                ps.executeUpdate();
    
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL : " + e.getMessage());
        }
    }
    



    
}
