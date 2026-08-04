package db;
import java.sql.*;
import java.util.Vector;

public class From_Role {
    public static Vector<String> lsRole(){
        String sql="SELECT * FROM ROLES";
        Vector<String> noms = new Vector<>();

        try(Connection conn=OracleConnection.getConnection()){
            PreparedStatement ps=conn.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();

            while (rs.next()) {
                noms.add(rs.getString("DESCROLE"));
            }

            
        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL : " + e.getMessage());
        }

        return noms;
    }

    public static String getRoleIdByName(String form_nom) {
        String idRole = null;
        String sql = "SELECT IDROLE FROM ROLES WHERE DESCROLE = ?";
    
        try (Connection conn = OracleConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
    
            ps.setString(1, form_nom);           
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    idRole = rs.getString("IDROLE");  
                }
            }
    
        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL : " + e.getMessage());
        }
    
        return idRole;  
    }
    
}
