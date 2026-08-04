package dao;

import model.TypeRoute;
import java.sql.*;
import java.util.Vector;

import connection.OracleConnection;

public class TypeRouteDAO {

    // --- Récupérer tous les types ---
    public static Vector<TypeRoute> getAllTypes() {
        Vector<TypeRoute> types = new Vector<>();
        String sql = "SELECT id, nom FROM type_route ORDER BY nom";

        try (Connection conn = OracleConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                types.add(new TypeRoute(rs.getInt("id"), rs.getString("nom")));
            }

        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL getAllTypes : " + e.getMessage());
        }

        return types;
    }

    // --- Récupérer ID par nom ---
    public static int getIdByName(String nom) {
        String sql = "SELECT id FROM type_route WHERE nom = ?";
        try (Connection conn = OracleConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nom);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }

        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL getIdByName : " + e.getMessage());
        }
        return -1;
    }

    // --- Récupérer nom par ID ---
     public static TypeRoute getTypeById(int idTypeRoute) {
        String sql = "SELECT id, nom FROM type_route WHERE id = ?";
        try (Connection conn = OracleConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idTypeRoute);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new TypeRoute(rs.getInt("id"), rs.getString("nom"));
            }

        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL getTypeById : " + e.getMessage());
        }
        return null;
    }
}
