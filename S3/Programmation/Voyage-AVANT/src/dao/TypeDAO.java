package dao;

import connection.*;
import java.sql.*;
import java.util.Vector;

public class TypeDAO {
    public static Vector<String[]> TypeVoiture(){
        String sql = "SELECT * FROM Type";

        Vector<String[]> lsType = new Vector<>();

        try(Connection conn = PostgresConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

        while (rs.next()) {
                String id=rs.getString("id");
                String nom=rs.getString("nom");
                lsType.add(new String[]{id, nom});  
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL : " + e.getMessage());
        }

        return lsType;
    }
}
