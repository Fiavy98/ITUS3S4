package dao;

import connection.*;
import java.sql.*;
import java.util.Vector;

public class VilleDAO {

    public static Vector<Vector<Object>> villesParRoute(String idRoute) {
    Vector<Vector<Object>> data = new Vector<>();

    String sql = """
        SELECT v.id, v.nom
        FROM ville v
        JOIN route_Ville rv ON v.id = rv.id_ville
        WHERE rv.id_Route = ?
    """;

    try (Connection conn = OracleConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, idRoute);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            row.add(rs.getString("id"));
            row.add(rs.getString("nom"));
            data.add(row);
        }

    } catch (SQLException e) {
        System.out.println("Erreur SQL : " + e.getMessage());
    }

    return data;
}
}
