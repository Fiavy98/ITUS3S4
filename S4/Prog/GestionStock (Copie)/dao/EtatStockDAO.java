package dao;

import models.EtatStockDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import db.ConnectionDB;

public class EtatStockDAO {

    public List<EtatStockDTO> getEtatStock() throws Exception {
        List<EtatStockDTO> liste = new ArrayList<>();
        // l’état actuel du stock de chaque produit
        String sql = """
            SELECT 
                p.id AS produit_id,
                p.nom,

                COALESCE(SUM(
                    CASE 
                        WHEN m.type = 'ENTREE' THEN m.quantite
                        WHEN m.type = 'SORTIE' THEN -m.quantite
                        ELSE 0
                    END
                ), 0) AS stock_quantite,

                COALESCE(SUM(
                    CASE 
                        WHEN m.type = 'ENTREE' 
                            THEN m.quantite * m.prix_unitaire
                        WHEN m.type = 'SORTIE' 
                            THEN -m.quantite * m.prix_unitaire
                        ELSE 0
                    END
                ), 0) AS valeur_stock

            FROM produits p
            LEFT JOIN mouvements m 
                ON p.id = m.produit_id

            GROUP BY p.id, p.nom
        """;
                Connection conn = ConnectionDB.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            EtatStockDTO dto = new EtatStockDTO();

            int stock = rs.getInt("stock_quantite");
            double valeur = rs.getDouble("valeur_stock");

            dto.setProduitId(rs.getInt("produit_id"));
            dto.setNomProduit(rs.getString("nom"));

            dto.setStockQuantite(stock);
            dto.setValeurStock(valeur);

            // 🔥 PRIX MOYEN (LOGIQUE MÉTIER)
            if (stock != 0) {
                dto.setPrixMoyen(valeur / stock);
            } else {
                dto.setPrixMoyen(0);
            }

            liste.add(dto);
        }

        rs.close();
        ps.close();
        conn.close();

        return liste;

    }
}
