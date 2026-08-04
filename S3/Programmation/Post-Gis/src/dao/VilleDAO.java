package dao;

import db.DBConnection;
import model.Ville;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class VilleDAO {

    public static List<Ville> getAllVilles() {
        List<Ville> villes = new ArrayList<>();
        String sql = "SELECT id, nom, ST_Y(geom) AS lat, ST_X(geom) AS lon FROM villes";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Ville ville = new Ville(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getDouble("lat"),
                        rs.getDouble("lon")
                );
                villes.add(ville);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return villes;
    }
}
