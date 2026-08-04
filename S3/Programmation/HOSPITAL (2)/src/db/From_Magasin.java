package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class From_Magasin {
        public static Vector<String[]> lsMagasin(){
        String sql="SELECT * FROM MAGASIN2";
        Vector<String[]> magasin = new Vector<>();

        try(Connection conn=OracleConnection.getConnection()){
            PreparedStatement ps=conn.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();

            while (rs.next()) {
                String id=rs.getString("ID");
                String nom=rs.getString("VAL");
                magasin.add(new String[]{id, nom});  
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL : " + e.getMessage());
        }

        return magasin;
    }


    public static String getIdmagasin(String form_nom) {
        String id = null;
        String sql = "SELECT ID FROM MAGASIN2 WHERE VAL = ?";
    
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
