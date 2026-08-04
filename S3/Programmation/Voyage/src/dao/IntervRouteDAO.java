package dao;

import model.IntervRoute;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import connection.OracleConnection;
import model.Reparer;

public class IntervRouteDAO {

    // --- Insérer un nouvel intervalle de route ---
    public static void inserer(IntervRoute i) {
        String sql = """
            INSERT INTO intervroute
            (nom, rn, depart_km, arrive_km, pluie)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection c = OracleConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, i.getNom());
            ps.setString(2, i.getRn());
            ps.setDouble(3, i.getDepartKm());
            ps.setDouble(4, i.getArriveKm());
            ps.setDouble(5, i.getPluie());

            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL inserer IntervRoute : " + e.getMessage());
            e.printStackTrace();
        }
    }

    // --- Récupérer tous les intervalles ---
    public static Vector<IntervRoute> getAll() {
        Vector<IntervRoute> list = new Vector<>();
        String sql = "SELECT id, nom, rn, depart_km, arrive_km, pluie FROM intervroute ORDER BY depart_km";

        try (Connection c = OracleConnection.getConnection();
             Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                IntervRoute i = new IntervRoute();
                i.setId(rs.getInt("id"));
                i.setNom(rs.getString("nom"));
                i.setRn(rs.getString("rn"));
                i.setDepartKm(rs.getDouble("depart_km"));
                i.setArriveKm(rs.getDouble("arrive_km"));
                i.setPluie(rs.getDouble("pluie"));
                list.add(i);
            }

        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL getAll IntervRoute : " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    // --- Récupérer l’intervalle correspondant à un km donné ---
    public static IntervRoute getByRouteAndKm(String rn, double km) {

    String sql = """
        SELECT id, nom, rn, depart_km, arrive_km, pluie
        FROM intervroute
        WHERE rn = ?
          AND ? BETWEEN depart_km AND arrive_km
    """;

    try (Connection c = OracleConnection.getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {

        ps.setString(1, rn);
        ps.setDouble(2, km);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            IntervRoute i = new IntervRoute();
            i.setId(rs.getInt("id"));
            i.setNom(rs.getString("nom"));
            i.setRn(rs.getString("rn"));
            i.setDepartKm(rs.getDouble("depart_km"));
            i.setArriveKm(rs.getDouble("arrive_km"));
            i.setPluie(rs.getDouble("pluie"));
            return i;
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return null;
}


    public static List<Reparation> getReparationsParIntervalle(
        int idIntervRoute,
        double x1,
        double x2
) {

    List<Reparation> liste = new ArrayList<>();

    String sql = """
        SELECT
            t.id AS id_trou,
            t.position_km,
            r.prix
        FROM trou t
        JOIN intervroute ir
            ON ir.id_route = t.id_route
           AND t.position_km BETWEEN ir.depart AND ir.arrive
        JOIN reparation r
            ON r.id_trou = t.id
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
            Reparation rep = new Reparation();
            rep.setIdTrou(rs.getInt("id_trou"));
            rep.setPositionKm(rs.getDouble("position_km"));
            rep.setPrix(rs.getDouble("prix"));
            liste.add(rep);
        }

    } catch (SQLException e) {
        System.out.println("❌ Erreur getReparationsParIntervalle : " + e.getMessage());
    }

    return liste;
}

}
