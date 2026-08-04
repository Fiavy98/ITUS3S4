package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import connection.*;

public class RouteDAO {

    public static Vector<String[]> lsRoute(){
        String sql="SELECT id,rn FROM route";
        Vector<String[]> route = new Vector<>();

        try(Connection conn=OracleConnection.getConnection()){
            PreparedStatement ps=conn.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
     
            while (rs.next()) {
                String id=rs.getString("id");
                String nom=rs.getString("rn");
                route.add(new String[]{id, nom});  
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL : " + e.getMessage());
        }

        return route;
    }

      public static String getNameById(int idRoute) {
        String nomRoute = null;
        String sql = "SELECT rn FROM route WHERE id = ?";

        try (Connection conn = OracleConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idRoute);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                nomRoute = rs.getString("rn");
            }

        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL getNameById : " + e.getMessage());
        }

        return nomRoute;
    }
    
    public static int getIdByName(String nomRoute) {
    String sql = "SELECT id FROM route WHERE rn = ?";
    try (Connection conn = OracleConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, nomRoute);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("id");
        }
    } catch (SQLException e) {
        System.out.println("❌ Erreur SQL getIdByName : " + e.getMessage());
    }
    return -1; // si pas trouvé
}

}
