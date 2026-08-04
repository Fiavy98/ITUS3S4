package dao;

import java.sql.*;
import java.util.Date;
import connection.OracleConnection;

public class TrouRepareDAO {

    // Insérer un trou réparé
    public static boolean insererTrouRepare(int idTrou, int idReparer, Date dateReparation) {
        String sql = "INSERT INTO trou_repare (id_trou, id_reparer, date_reparation) VALUES (?, ?, ?)";
        try (Connection conn = OracleConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idTrou);
            ps.setInt(2, idReparer);
            ps.setDate(3, new java.sql.Date(dateReparation.getTime()));

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL insererTrouRepare : " + e.getMessage());
            return false;
        }
    }

    // Supprimer un trou de la table principale
    public static boolean supprimerTrou(int idTrou) {
        String sql = "DELETE FROM trou WHERE id = ?";
        try (Connection conn = OracleConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idTrou);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL supprimerTrou : " + e.getMessage());
            return false;
        }
    }
}
