package com.gestion.stock.metier.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.gestion.stock.config.DatabaseConfig;
import com.gestion.stock.metier.entity.Article;
import com.gestion.stock.metier.repository.ArticleRepository;

public class DetailArticlePanel extends JPanel {
    private final ArticleRepository articleRepository = new ArticleRepository();
    private Article article;
    private final JLabel infoLabel = new JLabel();
    private final JLabel stockDetailLabel = new JLabel();
    private final DefaultTableModel mouvementsModel;
    private final DefaultTableModel lotsModel;
    private final JTable mouvementsTable;
    private final JTable lotsTable;
    private int currentArticleId;

    public DetailArticlePanel(int articleId) {
        setLayout(new BorderLayout());
        mouvementsModel = new DefaultTableModel(new String[]{
            "Date", "Type", "Méthode", "Qté +/-", "PU", "Valeur", "Stock après", "Valeur stock cumulée", "CUMP", "Source tiers", "Référence", "mouvement_id"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        lotsModel = new DefaultTableModel(new String[]{
                "Lot#", "Date entrée", "Qté prélevée", "PU lot", "Valeur", "Source lot"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        mouvementsTable = new JTable(mouvementsModel);
        lotsTable = new JTable(lotsModel);
        initUI();
        loadArticle(articleId);
        loadMouvements(articleId);
    }

    private void initUI() {
        JPanel topPanel = new JPanel(new BorderLayout(8, 8));
        topPanel.add(infoLabel, BorderLayout.CENTER);
        stockDetailLabel.setBorder(BorderFactory.createEmptyBorder(0, 4, 4, 4));
        topPanel.add(stockDetailLabel, BorderLayout.SOUTH);

        JButton refreshButton = new JButton("Rafraîchir");
        refreshButton.addActionListener(e -> {
            if (currentArticleId > 0) {
                loadArticle(currentArticleId);
                loadMouvements(currentArticleId);
            }
        });
        topPanel.add(refreshButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        mouvementsTable.setDefaultRenderer(Object.class, new MouvementRenderer());
        mouvementsTable.getColumnModel().getColumn(11).setMinWidth(0);
        mouvementsTable.getColumnModel().getColumn(11).setMaxWidth(0);
        mouvementsTable.getColumnModel().getColumn(11).setPreferredWidth(0);

        mouvementsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = mouvementsTable.rowAtPoint(e.getPoint());
                    if (row >= 0) {
                        Object movementIdValue = mouvementsTable.getValueAt(row, 11);
                        if (movementIdValue != null) {
                            showMovementDetail(Integer.parseInt(movementIdValue.toString()));
                        }
                    }
                }
            }
        });

        mouvementsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = mouvementsTable.getSelectedRow();
                if (row >= 0) {
                    Object movementIdValue = mouvementsTable.getValueAt(row, 11);
                    if (movementIdValue != null) {
                        loadLotsSource(Integer.parseInt(movementIdValue.toString()));
                    }
                }
            }
        });

        JSplitPane bottomSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(mouvementsTable), new JScrollPane(lotsTable));
        bottomSplit.setResizeWeight(0.7);
        add(bottomSplit, BorderLayout.CENTER);
    }

    private void loadArticle(int articleId) {
        currentArticleId = articleId;
        try {
            article = articleRepository.findById(articleId).orElse(null);
            if (article != null) {
                String categorie = article.getCategorieLibelle() != null ? article.getCategorieLibelle() : "Aucune";
                String text = "Article: " + article.getCode()
                        + " | Catégorie: " + categorie
                        + " | Stock: " + format(article.getStockActuel())
                        + " | CUMP: " + format(article.getCumpActuel())
                        + " | Valeur stock cumulée: " + format(article.getValeurStockActuelle());
                infoLabel.setText(text);
                stockDetailLabel.setText("Stock restant: " + buildStockDetail(articleId));
            }
        } catch (Exception e) {
            infoLabel.setText("Erreur chargement article: " + e.getMessage());
        }
    }

    private void loadMouvements(int articleId) {
        mouvementsModel.setRowCount(0);
        String sql = "SELECT * FROM v_mouvement_detail WHERE article_id = ? ORDER BY date_mouvement DESC, mouvement_id DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, articleId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String type = rs.getString("type_mouvement");
                    BigDecimal quantite = rs.getBigDecimal("quantite");
                    mouvementsModel.addRow(new Object[]{
                            rs.getDate("date_mouvement"),
                            type,
                            rs.getString("methode_valorisation"),
                            signedQuantity(type, quantite),
                            rs.getBigDecimal("pu"),
                            rs.getBigDecimal("valeur_mouvement"),
                            rs.getBigDecimal("stock_qte_apres"),
                            rs.getBigDecimal("valeur_stock_apres"),
                            rs.getBigDecimal("cump_apres"),
                            rs.getString("source_tiers"),
                            rs.getString("source_reference"),
                            rs.getInt("mouvement_id")
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur chargement mouvements: " + e.getMessage());
        }
    }

    private void loadLotsSource(int mouvementId) {
        lotsModel.setRowCount(0);
        String sql = "SELECT mls.lot_id, l.date_entree, mls.quantite_prelevee, mls.prix_unitaire_lot, mls.valeur_prelevee, mls.source_lot_reference "
                + "FROM mouvement_lot_source mls "
                + "LEFT JOIN lot l ON l.id = mls.lot_id "
                + "WHERE mls.mouvement_id = ? ORDER BY mls.ordre_consommation";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, mouvementId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lotsModel.addRow(new Object[]{
                            rs.getObject("lot_id"),
                            rs.getDate("date_entree"),
                            rs.getBigDecimal("quantite_prelevee"),
                            rs.getBigDecimal("prix_unitaire_lot"),
                            rs.getBigDecimal("valeur_prelevee"),
                            rs.getString("source_lot_reference")
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur chargement lots: " + e.getMessage());
        }
    }

    private void showMovementDetail(int mouvementId) {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Détail mouvement", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setContentPane(new MouvementDetailPanel(mouvementId));
        dialog.setSize(1050, 620);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private String format(BigDecimal value) {
        return value != null ? value.toPlainString() : "0";
    }

    private String buildStockDetail(int articleId) {
        String sql = "SELECT quantite_restante, prix_unitaire FROM lot "
                + "WHERE article_id = ? AND epuise = false ORDER BY date_entree, id";
        StringBuilder sb = new StringBuilder();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, articleId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    if (sb.length() > 0) sb.append(" | ");
                    sb.append(rs.getBigDecimal("quantite_restante").toPlainString())
                      .append(" @ ")
                      .append(rs.getBigDecimal("prix_unitaire").toPlainString());
                }
            }
        } catch (Exception e) {
            return "Erreur";
        }
        return sb.length() == 0 ? "-" : sb.toString();
    }

    private BigDecimal signedQuantity(String type, BigDecimal quantite) {
        if (quantite == null) {
            return BigDecimal.ZERO;
        }
        return "SORTIE".equalsIgnoreCase(type) ? quantite.negate() : quantite;
    }

    private static class MouvementRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            String type = (String) table.getValueAt(row, 1);
            if (!isSelected) {
                if ("ENTREE".equalsIgnoreCase(type)) {
                    c.setBackground(new Color(210, 255, 210));
                } else if ("SORTIE".equalsIgnoreCase(type)) {
                    c.setBackground(new Color(255, 220, 220));
                } else {
                    c.setBackground(table.getBackground());
                }
            }
            return c;
        }
    }
}
