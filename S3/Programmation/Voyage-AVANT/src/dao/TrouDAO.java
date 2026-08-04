package dao;
import connection.OracleConnection;

import model.Trou;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class TrouDAO {

    // --- Insertion d'un trou ---
public static boolean insererTrou(Trou trou) {
    String sql = "INSERT INTO trou (id_route, position_km, profondeur, surface) VALUES (?, ?, ?, ?)";

    try (Connection conn = OracleConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, trou.getIdRoute());
        ps.setDouble(2, trou.getPositionKm());
        ps.setDouble(3, trou.getProfondeur());
        ps.setDouble(4, trou.getSurface());

        int rows = ps.executeUpdate();
        return rows > 0;

    } catch (SQLException e) {
        System.out.println("❌ Erreur SQL insererTrou : " + e.getMessage());
        return false;
    }
}

    // --- Colonnes ---
    public static Vector<String> getColumns() {
        Vector<String> colNames = new Vector<>();
        colNames.add("ID");
        colNames.add("Position (km)");
        colNames.add("Profondeur (cm)");
        colNames.add("Surface (m²)");
        return colNames;
    }

    // --- Données pour JTable ---
    public static Vector<Vector<Object>> getTableData(int idRoute) {
        Vector<Vector<Object>> data = new Vector<>();
         String sql = "SELECT * FROM trou WHERE id_route = ? AND status = 'non_repare' ORDER BY position_km";

        try (Connection conn = OracleConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idRoute);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getDouble("position_km"));
                row.add(rs.getDouble("profondeur"));
                row.add(rs.getDouble("surface"));
                data.add(row);
            }

        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL getTableData Trou : " + e.getMessage());
        }

        return data;
    }

        public static boolean marquerCommeRepare(int idTrou) {
        String sql = "UPDATE trou SET status = 'repare' WHERE id = ?";

        try (Connection conn = OracleConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idTrou);
            int lignes = ps.executeUpdate();
            return lignes > 0;

        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL marquerCommeRepare : " + e.getMessage());
            return false;
        }
    }

    // --- Récupérer un trou par son ID ---
    public static Trou getTrouById(int idTrou) {
        String sql = "SELECT id, id_route, position_km, profondeur, surface FROM trou WHERE id = ?";
        try (Connection conn = OracleConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idTrou);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Trou trou = new Trou();
                trou.setId(rs.getInt("id"));
                trou.setIdRoute(rs.getInt("id_route"));
                trou.setPositionKm(rs.getDouble("position_km"));
                trou.setProfondeur(rs.getDouble("profondeur"));
                trou.setSurface(rs.getDouble("surface"));
                return trou;
            }

        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL getTrouById : " + e.getMessage());
        }
        return null;
    }
}
