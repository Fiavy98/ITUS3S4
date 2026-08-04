package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class From_Medicament {
    public static Vector<String[]> lsMedicament(){
        String sql = "SELECT ID,LIBELLE FROM AS_INGREDIENTS ORDER BY LIBELLE ASC";
        Vector<String[]> medicament = new Vector<>();

        try(Connection conn=OracleConnection.getConnection()){
            PreparedStatement ps=conn.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();

            while (rs.next()) {
                String id=rs.getString("ID");
                String nom=rs.getString("LIBELLE");
                medicament.add(new String[]{id, nom});  
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL : " + e.getMessage());
        }

        return medicament;
    }

    public static String getIdMedicament(String form_nom) {
        String id = null;
        String sql = "SELECT ID FROM AS_INGREDIENTS WHERE LIBELLE = ?";
    
        try (Connection conn = OracleConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
    
            ps.setString(1, form_nom);            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    id = rs.getString("ID");  
                }
            }
    
        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL : " + e.getMessage());
        }
    
        return id;  
    }

}

