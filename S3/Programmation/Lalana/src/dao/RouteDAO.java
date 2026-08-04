package dao;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Route;
import Connection.OracleConnection;
public class RouteDAO {

    public List<Route> getAllRoutes() {
        List<Route> routes = new ArrayList<>();
        String sql = "SELECT id, rn, idVille_depart, idVille_arriver, longueur_km FROM route";

        try (Connection conn = OracleConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Route r = new Route(
                        rs.getInt("id"),
                        rs.getString("rn"),
                        rs.getInt("idVille_depart"),
                        rs.getInt("idVille_arriver"),
                        rs.getDouble("longueur_km")
                );
                routes.add(r);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return routes;
    }
}
