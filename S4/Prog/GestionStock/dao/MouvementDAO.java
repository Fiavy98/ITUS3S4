package dao;

import db.ConnectionDB;
import models.Mouvement;
import models.TypeMouvement;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MouvementDAO {

public void insert(Mouvement m) throws Exception {

    Connection conn = null;

    try {

        conn = ConnectionDB.getConnection();
        conn.setAutoCommit(false);

        // =========================
        // GET METHODE PRODUIT
        // =========================
        String sqlProduit = """
            SELECT methode_valuation
            FROM produits
            WHERE id = ?
        """;

        PreparedStatement psProduit = conn.prepareStatement(sqlProduit);
        psProduit.setInt(1, m.getProduitId());

        ResultSet rsProduit = psProduit.executeQuery();

        String methode = null;

        if (rsProduit.next()) {
            methode = rsProduit.getString("methode_valuation");
        }

        rsProduit.close();
        psProduit.close();

        // =========================
        // ENTREE
        // =========================
        if (m.getType() == TypeMouvement.ENTREE) {

            String sql = """
                INSERT INTO mouvements
                (produit_id, type, quantite, quantite_restante, prix_unitaire, date)
                VALUES (?, ?::type_mouvement_enum, ?, ?, ?, ?)
            """;

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, m.getProduitId());
            ps.setString(2, m.getType().name());
            ps.setInt(3, m.getQuantite());
            ps.setInt(4, m.getQuantite());
            ps.setDouble(5, m.getPrixUnitaire());
            ps.setTimestamp(6, Timestamp.valueOf(m.getDate()));

            ps.executeUpdate();
            ps.close();
        }

        // =========================
        // SORTIE
        // =========================
        else if (m.getType() == TypeMouvement.SORTIE) {

            int resteASortir = m.getQuantite();
            double coutTotal = 0;

            // =========================
            // FIFO ou LIFO
            // =========================
            String orderBy = "ASC";
            if ("LIFO".equalsIgnoreCase(methode)) {
                orderBy = "DESC";
            }

            String sqlLots = """
                SELECT id, quantite_restante, prix_unitaire
                FROM mouvements
                WHERE produit_id = ?
                AND type = 'ENTREE'
                AND quantite_restante > 0
                ORDER BY date %s
            """.formatted(orderBy);

            PreparedStatement psLots = conn.prepareStatement(sqlLots);
            psLots.setInt(1, m.getProduitId());

            ResultSet rsLots = psLots.executeQuery();

            // =========================
            // CONSOMMATION DES LOTS
            // =========================
            while (rsLots.next() && resteASortir > 0) {

                int idLot = rsLots.getInt("id");
                int qteRestante = rsLots.getInt("quantite_restante");
                double pu = rsLots.getDouble("prix_unitaire");

                if (qteRestante <= 0) continue;

                int qtePrise = Math.min(resteASortir, qteRestante);

                coutTotal += qtePrise * pu;

                int nouvelleQte = qteRestante - qtePrise;

                String sqlUpdate = """
                    UPDATE mouvements
                    SET quantite_restante = ?
                    WHERE id = ?
                """;

                PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate);

                psUpdate.setInt(1, nouvelleQte);
                psUpdate.setInt(2, idLot);

                psUpdate.executeUpdate();
                psUpdate.close();

                resteASortir -= qtePrise;
            }

            rsLots.close();
            psLots.close();

            // =========================
            // VERIFICATION STOCK
            // =========================
            if (resteASortir > 0) {
                throw new Exception("Stock insuffisant pour ce produit");
            }

            // =========================
            // PRIX MOYEN SORTIE
            // =========================
            double prixUnitaireSortie = coutTotal / m.getQuantite();

            // =========================
            // INSERT SORTIE
            // =========================
            String sqlSortie = """
                INSERT INTO mouvements
                (produit_id, type, quantite, quantite_restante, prix_unitaire, date)
                VALUES (?, ?::type_mouvement_enum, ?, 0, ?, ?)
            """;

            PreparedStatement psSortie = conn.prepareStatement(sqlSortie);

            psSortie.setInt(1, m.getProduitId());
            psSortie.setString(2, m.getType().name());
            psSortie.setInt(3, m.getQuantite());
            psSortie.setDouble(4, prixUnitaireSortie);
            psSortie.setTimestamp(5, Timestamp.valueOf(m.getDate()));
            
            psSortie.executeUpdate();
            psSortie.close();
        }

        conn.commit();

    } catch (Exception e) {

        if (conn != null) {
            conn.rollback();
        }

        throw e;

    } finally {

        if (conn != null) {
            conn.close();
        }
    }
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

public double getStock(int produitId, LocalDate date) throws Exception {

    String sql = """
        SELECT COALESCE(SUM(
            CASE 
                WHEN type = 'ENTREE' THEN quantite
                WHEN type = 'SORTIE' THEN -quantite
                ELSE 0
            END
        ), 0) AS stock
        FROM mouvements
        WHERE produit_id = ?
        AND date::date <= ?
    """;

    try (Connection conn = ConnectionDB.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, produitId);
        ps.setDate(2, java.sql.Date.valueOf(date));

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {

            double stock = rs.getDouble("stock");

            if (stock < 0) {
                stock = 0;
            }

            return stock;
        }

        return 0;

    }
}

public double getValeurStock(int produitId, LocalDate date, String methode) throws Exception {

    String sql = """
        SELECT type, quantite, quantite_restante, prix_unitaire
        FROM mouvements
        WHERE produit_id = ?
        AND date::date <= ?
        ORDER BY date ASC
    """;

    try (Connection conn = ConnectionDB.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, produitId);
        ps.setDate(2, java.sql.Date.valueOf(date));

        ResultSet rs = ps.executeQuery();

        double valeur = 0;

        while (rs.next()) {

            String type = rs.getString("type");
            int qte = rs.getInt("quantite");
            double pu = rs.getDouble("prix_unitaire");

            if (valeur < 0) {
                valeur = 0;
            }

            if ("ENTREE".equals(type)) {

                valeur += qte * pu;

            } else if ("SORTIE".equals(type)) {

                // sortie = valeur retirée du stock
                valeur -= qte * pu;
            }
        }

        return valeur;
    }
}

public Mouvement getLastMovement(int produitId) throws Exception {

    String sql = """
        SELECT id, produit_id, type, quantite, quantite_restante, prix_unitaire, date
        FROM mouvements
        WHERE produit_id = ?
        ORDER BY date DESC
        LIMIT 1
    """;

    try (Connection conn = ConnectionDB.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, produitId);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {

            Mouvement m = new Mouvement();

            m.setId(rs.getInt("id"));
            m.setProduitId(rs.getInt("produit_id"));
            m.setType(TypeMouvement.valueOf(rs.getString("type")));
            m.setQuantite(rs.getInt("quantite"));
            m.setQuantiteRestante(rs.getInt("quantite_restante"));
            m.setPrixUnitaire(rs.getDouble("prix_unitaire"));
            m.setDate(rs.getTimestamp("date").toLocalDateTime());

            return m;
        }

        return null;
    }
}


}

