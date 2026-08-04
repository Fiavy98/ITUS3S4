package dao;


import java.sql.*;
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

}
