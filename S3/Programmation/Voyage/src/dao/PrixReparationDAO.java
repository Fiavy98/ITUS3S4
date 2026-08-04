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


    public static List<ReparerPrix> getPrixParIntervalle(
            int idIntervRoute,
            double x1,
            double x2
    ) {

        List<ReparerPrix> liste = new ArrayList<>();

        String sql = """
            SELECT
                t.position_km,
                pr.prix,
                r.date_reparation
            FROM prix_reparation pr
            JOIN reparer r ON r.id = pr.id_reparer
            JOIN trou t ON t.id = r.id_trou
            JOIN intervroute ir
                ON ir.id_route = t.id_route
               AND t.position_km BETWEEN ir.depart AND ir.arrive
            WHERE ir.id = ?
              AND t.position_km BETWEEN ? AND ?
            ORDER BY t.position_km
        """;

        try (Connection conn = OracleConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idIntervRoute);
            ps.setDouble(2, x1);
            ps.setDouble(3, x2);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ReparerPrix rp = new ReparerPrix();
                rp.setPositionKm(rs.getDouble("position_km"));
                rp.setPrix(rs.getDouble("prix"));
                rp.setDateReparation(rs.getDate("date_reparation"));

                liste.add(rp);
            }

        } catch (SQLException e) {
            System.out.println("❌ Erreur getPrixParIntervalle : " + e.getMessage());
        }

        return liste;
    }

}
