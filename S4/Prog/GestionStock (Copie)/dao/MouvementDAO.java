package dao;

import db.ConnectionDB;
import models.Mouvement;
import models.TypeMouvement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MouvementDAO {

    public void insert(Mouvement m) throws Exception {

        String sql = """
            INSERT INTO mouvements
            (produit_id, type, quantite, prix_unitaire, date)
            VALUES (?, ?::type_mouvement_enum, ?, ?, ?)
        """;

        Connection conn = ConnectionDB.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, m.getProduitId());
        ps.setString(2, m.getType().name());
        ps.setInt(3, m.getQuantite());
        ps.setDouble(4, m.getPrixUnitaire());
        ps.setTimestamp(5, Timestamp.valueOf(m.getDate()));

        ps.executeUpdate();

        ps.close();
        conn.close();
    }

    public List<Mouvement> getByProduit(int produitId) throws Exception {

        String sql = """
            SELECT * FROM mouvements
            WHERE produit_id = ?
            ORDER BY date ASC
        """;

        Connection conn = ConnectionDB.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, produitId);

        ResultSet rs = ps.executeQuery();

        List<Mouvement> list = new ArrayList<>();

        while (rs.next()) {

            Mouvement m = new Mouvement();

            m.setProduitId(rs.getInt("produit_id"));
            m.setType(TypeMouvement.valueOf(rs.getString("type")));
            m.setQuantite(rs.getInt("quantite"));
            m.setPrixUnitaire(rs.getDouble("prix_unitaire"));
            m.setDate(rs.getTimestamp("date").toLocalDateTime());

            list.add(m);
        }

        rs.close();
        ps.close();
        conn.close();

        return list;
    }
}