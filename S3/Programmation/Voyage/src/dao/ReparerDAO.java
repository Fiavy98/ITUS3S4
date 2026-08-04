package dao;

import model.Reparer;
import java.sql.*;
import java.util.Vector;
import connection.OracleConnection;
import util.ResultSetUtil;
public class ReparerDAO {

    // --- Ajouter une réparation ---
// --- Ajouter une réparation et retourner son ID ---
public static int insererReparation(Reparer rep) {
    String sql = "INSERT INTO reparer (id, id_trou, id_route, id_type_route, date_reparation, description) " +
                 "VALUES (reparer_seq.NEXTVAL, ?, ?, ?, ?, ?)";

    try (Connection conn = OracleConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id"})) {

        ps.setInt(1, rep.getIdTrou());
        ps.setInt(2, rep.getIdRoute());
        ps.setInt(3, rep.getIdTypeRoute());
        ps.setDate(4, new java.sql.Date(rep.getDateReparation().getTime()));
        ps.setString(5, rep.getDescription());

        int affectedRows = ps.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("L'insertion a échoué, aucune ligne affectée.");
        }

        // récupérer l'ID généré
        try (ResultSet rs = ps.getGeneratedKeys()) {
            if (rs.next()) {
                int idGenere = rs.getInt(1);
                rep.setId(idGenere); // mettre à jour l'objet
                return idGenere;
            } else {
                throw new SQLException("L'insertion a réussi mais impossible de récupérer l'ID.");
            }
        }

    } catch (SQLException e) {
        System.out.println("❌ Erreur SQL insererReparation : " + e.getMessage());
        return -1;
    }
}

    // --- Colonnes du tableau ---
public static Vector<String> getColumns() {
    Vector<String> col = new Vector<>();
    col.add("ID");
    col.add("Trou");
    col.add("ID_Route");
    col.add("Date");
    col.add("Surface (m²)");
    col.add("Profondeur (cm)");
    col.add("Type de route"); // nouveau
    col.add("Prix");          // prix calculé ou null
    return col;
}


    // --- Données pour JTable pour une route donnée ---
public static Vector<Vector<Object>> getTableData(int idRoute) {
    Vector<Vector<Object>> data = new Vector<>();
String sql = "SELECT r.id, t.nom, r.id_route, r.date_reparation, " +
             "t.surface, t.profondeur, tr.nom AS type_route, pr.prix " +
             "FROM reparer r " +
             "JOIN trou t ON r.id_trou = t.id " +
             "JOIN type_route tr ON r.id_type_route = tr.id " +
             "LEFT JOIN prix_reparation pr ON r.id = pr.id_reparer " +
             "WHERE r.id_route = ? " +
             "ORDER BY r.date_reparation DESC";

    try (Connection conn = OracleConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, idRoute);
        ResultSet rs = ps.executeQuery();

            while (rs.next()) {
    Vector<Object> row = new Vector<>();
    row.add(rs.getInt("id"));
    row.add(rs.getString("nom"));
    row.add(rs.getInt("id_route"));
    row.add(rs.getDate("date_reparation"));
    row.add(rs.getDouble("surface"));
    row.add(rs.getDouble("profondeur"));
    row.add(rs.getString("type_route")); // nom du type de route
    row.add(rs.getObject("prix"));       // prix (null si pas encore calculé)
    data.add(row);
}


    } catch (SQLException e) {
        System.out.println("❌ Erreur SQL getTableData Reparer : " + e.getMessage());
    }

    return data;
}
public static Vector<Vector<Object>> getTableDataByRouteTriPrix(
        int idRoute, boolean asc) {

    Vector<Vector<Object>> data = new Vector<>();
    String ordre = asc ? " ASC" : " DESC";

    String sql = """
        SELECT r.id,
               t.nom AS nom_trou,
               r.id_route,
               r.date_reparation,
               t.surface,
               t.profondeur,
               tr.nom AS type_route,
               pr.prix
        FROM reparer r
        JOIN trou t ON r.id_trou = t.id
        JOIN type_route tr ON r.id_type_route = tr.id
        LEFT JOIN prix_reparation pr ON r.id = pr.id_reparer
        WHERE r.id_route = ?
        ORDER BY pr.prix""" + ordre;

    try (Connection c = OracleConnection.getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {

        ps.setInt(1, idRoute);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            row.add(rs.getInt("id"));
            row.add(rs.getString("nom_trou"));
            row.add(rs.getInt("id_route"));
            row.add(rs.getDate("date_reparation"));
            row.add(rs.getDouble("surface"));
            row.add(rs.getDouble("profondeur"));
            row.add(rs.getString("type_route"));
            row.add(rs.getObject("prix"));
            data.add(row);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return data;
}


public static Object[][] getTableDataByKmInterval(
        int idRoute, double x1, double x2) {

    String sql = """
        SELECT r.id,
               t.position_km,
               r.date_reparation,
               tr.nom,
               p.prix
        FROM reparer r
        JOIN trou t ON t.id = r.id_trou
        JOIN type_route tr ON tr.id = r.id_type_route
        JOIN prix_reparation p ON p.id_reparer = r.id
        WHERE r.id_route = ?
          AND t.position_km BETWEEN ? AND ?
        ORDER BY t.position_km
    """;

    try (Connection c = OracleConnection.getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {

        ps.setInt(1, idRoute);
        ps.setDouble(2, x1);
        ps.setDouble(3, x2);

        ResultSet rs = ps.executeQuery();

        return ResultSetUtil.toTableData(rs);

    } catch (Exception e) {
        e.printStackTrace();
        return new Object[0][0];
    }
}

}