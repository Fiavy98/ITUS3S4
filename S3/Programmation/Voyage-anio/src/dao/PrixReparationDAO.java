package dao;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Prix;
import connection.OracleConnection; 
public class PrixReparationDAO {

    // --- Ajouter un prix pour une réparation ---
    public static boolean insererPrix(int idReparer, double prix) {
        String sql = "INSERT INTO Prix_reparation (id_reparer, prix) VALUES (?, ?)";

        try (Connection conn = OracleConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idReparer);
            ps.setDouble(2, prix);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL insererPrix : " + e.getMessage());
            return false;
        }
    }

    // --- Récupérer le prix d'une réparation ---
    public static Double getPrixByReparer(int idReparer) {
        String sql = "SELECT prix FROM Prix_reparation WHERE id_reparer = ?";
        try (Connection conn = OracleConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idReparer);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble("prix");

        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL getPrixByReparer : " + e.getMessage());
        }
        return null;
    }

    public static double getTotalByRoute(int idRoute) {

    String sql =
        "SELECT NVL(SUM(pr.prix), 0) AS total " +
        "FROM reparer r " +
        "JOIN prix_reparation pr ON r.id = pr.id_reparer " +
        "WHERE r.id_route = ?";

    try (Connection conn = OracleConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, idRoute);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getDouble("total");
        }

    } catch (Exception e) {
        System.out.println("❌ Erreur calcul total : " + e.getMessage());
    }

    return 0;
}


public static double getTotalByIntervalle(int idIntervalle) {

    String sql = """
        SELECT NVL(SUM(p.prix), 0)
        FROM intervroute i
        JOIN trou t
             ON t.position_km BETWEEN i.depart_km AND i.arrive_km
        JOIN reparer r
             ON r.id_trou = t.id
        JOIN prix_reparation p
             ON p.id_reparer = r.id
        WHERE i.id = ?
    """;

    try (Connection c = OracleConnection.getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {

        ps.setInt(1, idIntervalle);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getDouble(1);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return 0;
}

public static double getTotalByRouteAndKm(
        int idRoute,
        double kmDebut,
        double kmFin
) {
    String sql = """
        SELECT NVL(SUM(pr.montant), 0)
        FROM reparer r
        JOIN prix_reparation pr ON pr.id_reparer = r.id
        JOIN trou t ON t.id = r.id_trou
        WHERE r.id_route = ?
        AND t.position_km BETWEEN ? AND ?
    """;

    try (
        Connection c = OracleConnection.getConnection();
        PreparedStatement ps = c.prepareStatement(sql)
    ) {
        ps.setInt(1, idRoute);
        ps.setDouble(2, kmDebut);
        ps.setDouble(3, kmFin);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getDouble(1);

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return 0;
}

}
