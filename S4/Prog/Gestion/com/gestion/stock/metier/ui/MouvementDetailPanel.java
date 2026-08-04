package com.gestion.stock.metier.ui;

import java.awt.BorderLayout;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.gestion.stock.config.DatabaseConfig;

public class MouvementDetailPanel extends JPanel {
    private final JLabel summaryLabel = new JLabel();
    private final DefaultTableModel lotsModel;
    private final JTable lotsTable;
    private final int mouvementId;

    public MouvementDetailPanel(int mouvementId) {
        this.mouvementId = mouvementId;
        setLayout(new BorderLayout(8, 8));
        lotsModel = new DefaultTableModel(new String[]{
                "Lot#", "Date entrée", "Qté prélevée", "PU lot", "Valeur prélevée", "Source lot", "Type source"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        lotsTable = new JTable(lotsModel);
        lotsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer());
        initUi();
        loadData();
    }

    private void initUi() {
        summaryLabel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        add(summaryLabel, BorderLayout.NORTH);
        add(new JScrollPane(lotsTable), BorderLayout.CENTER);
    }

    private void loadData() {
        lotsModel.setRowCount(0);
        String movementSql = "SELECT * FROM v_mouvement_detail WHERE mouvement_id = ?";
        String lotsSql = "SELECT mls.lot_id, l.date_entree, mls.quantite_prelevee, mls.prix_unitaire_lot, mls.valeur_prelevee, "
                + "mls.source_lot_reference, mls.source_lot_type "
                + "FROM mouvement_lot_source mls "
                + "LEFT JOIN lot l ON l.id = mls.lot_id "
                + "WHERE mls.mouvement_id = ? ORDER BY mls.ordre_consommation";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement movementPs = conn.prepareStatement(movementSql);
             PreparedStatement lotsPs = conn.prepareStatement(lotsSql)) {
            movementPs.setInt(1, mouvementId);
            try (ResultSet rs = movementPs.executeQuery()) {
                if (rs.next()) {
                    String articleCode = rs.getString("article_code");
                    String type = rs.getString("type_mouvement");
                    String methode = rs.getString("methode_valorisation");
                    BigDecimal quantite = rs.getBigDecimal("quantite");
                    BigDecimal qteApres = rs.getBigDecimal("stock_qte_apres");
                    BigDecimal valeurApres = rs.getBigDecimal("valeur_stock_apres");
                    BigDecimal qteAvant = rs.getBigDecimal("stock_qte_avant");
                    BigDecimal valeurAvant = rs.getBigDecimal("valeur_stock_avant");
                    BigDecimal pu = rs.getBigDecimal("pu");
                    BigDecimal valeurMouvement = rs.getBigDecimal("valeur_mouvement");
                    BigDecimal cumpApres = rs.getBigDecimal("cump_apres");
                    BigDecimal cumpAvant = computeCump(valeurAvant, qteAvant);

                    summaryLabel.setText(
                            "Article: " + articleCode
                                    + " | Date: " + rs.getDate("date_mouvement")
                                    + " | Type: " + type
                                    + " | Méthode: " + (methode != null ? methode : "-")
                                    + " | Qté: " + formatSigned(type, quantite)
                                    + " | PU: " + format(pu)
                                    + " | Valeur mouvement: " + format(valeurMouvement)
                                    + " | Stock avant: " + format(qteAvant)
                                    + " / " + format(valeurAvant)
                                    + " | Stock après: " + format(qteApres)
                                    + " / " + format(valeurApres)
                                    + " | CUMP avant: " + format(cumpAvant)
                                    + " | CUMP après: " + format(cumpApres)
                    );
                } else {
                    summaryLabel.setText("Mouvement introuvable.");
                    return;
                }
            }

            lotsPs.setInt(1, mouvementId);
            try (ResultSet rs = lotsPs.executeQuery()) {
                while (rs.next()) {
                    lotsModel.addRow(new Object[]{
                            rs.getObject("lot_id"),
                            rs.getDate("date_entree"),
                            rs.getBigDecimal("quantite_prelevee"),
                            rs.getBigDecimal("prix_unitaire_lot"),
                            rs.getBigDecimal("valeur_prelevee"),
                            rs.getString("source_lot_reference"),
                            rs.getString("source_lot_type")
                    });
                }
            }
        } catch (Exception ex) {
            summaryLabel.setText("Erreur chargement détail mouvement: " + ex.getMessage());
        }
    }

    private BigDecimal computeCump(BigDecimal valeur, BigDecimal qte) {
        if (valeur == null || qte == null || qte.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return valeur.divide(qte, 4, java.math.RoundingMode.HALF_UP);
    }

    private String format(BigDecimal value) {
        return value != null ? value.toPlainString() : "0";
    }

    private String formatSigned(String type, BigDecimal quantite) {
        BigDecimal signed = "SORTIE".equalsIgnoreCase(type) && quantite != null ? quantite.negate() : quantite;
        return signed != null ? signed.toPlainString() : "0";
    }
}
