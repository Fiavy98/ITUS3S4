package com.gestion.stock.metier.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.gestion.stock.config.DatabaseConfig;
import com.toedter.calendar.JDateChooser;

public class HistoriqueMouvementsPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private final JDateChooser startDate = new JDateChooser();
    private final JDateChooser endDate = new JDateChooser();
    private final JLabel stockGlobalLabel = new JLabel("Stock global: 0");

    public HistoriqueMouvementsPanel() {
        setLayout(new BorderLayout());
        initFilters();
        initTable();
        initFooter();
        loadData();
    }

    private void initFilters() {
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton applyBtn = new JButton("Filtrer");
        JButton clearBtn = new JButton("Effacer");
        JButton refreshBtn = new JButton("Rafraichir");

        filterPanel.add(new JLabel("Du:"));
        filterPanel.add(startDate);
        filterPanel.add(new JLabel("Au:"));
        filterPanel.add(endDate);
        filterPanel.add(applyBtn);
        filterPanel.add(clearBtn);
        filterPanel.add(refreshBtn);

        applyBtn.addActionListener(e -> loadData());
        clearBtn.addActionListener(e -> {
            startDate.setDate(null);
            endDate.setDate(null);
            loadData();
        });
        refreshBtn.addActionListener(e -> loadData());

        add(filterPanel, BorderLayout.NORTH);
    }

    private void initTable() {
        tableModel = new DefaultTableModel(new String[]{
                "Date", "Article", "Type", "Methode", "Qte +/-", "PU", "Valeur", "Stock(qte)",
                "Detail stock", "Valeur stock cumulee", "CUMP", "Source tiers", "Reference", "article_id", "mouvement_id"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setDefaultRenderer(Object.class, new MovementRenderer());
        hideColumn(13);
        hideColumn(14);
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.rowAtPoint(e.getPoint());
                    int col = table.columnAtPoint(e.getPoint());
                    if (row >= 0) {
                        if (col == 8) {
                            Object articleIdValue = table.getValueAt(row, 13);
                            Object articleCode = table.getValueAt(row, 1);
                            if (articleIdValue != null) {
                                showStockDetail(Integer.parseInt(articleIdValue.toString()), String.valueOf(articleCode));
                            }
                        } else {
                            Object mouvementIdValue = table.getValueAt(row, 14);
                            if (mouvementIdValue != null) {
                                showDetail(Integer.parseInt(mouvementIdValue.toString()));
                            }
                        }
                    }
                }
            }
        });
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void initFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footer.add(stockGlobalLabel);
        add(footer, BorderLayout.SOUTH);
    }

    private void hideColumn(int columnIndex) {
        table.getColumnModel().getColumn(columnIndex).setMinWidth(0);
        table.getColumnModel().getColumn(columnIndex).setMaxWidth(0);
        table.getColumnModel().getColumn(columnIndex).setPreferredWidth(0);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        StringBuilder sql = new StringBuilder("SELECT * FROM v_mouvement_detail");
        String where = buildDateFilter();
        if (!where.isEmpty()) {
            sql.append(" WHERE ").append(where);
        }
        sql.append(" ORDER BY date_mouvement DESC, mouvement_id DESC");

        Map<Integer, String> detailCache = new HashMap<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            bindDateFilter(ps);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String type = rs.getString("type_mouvement");
                    java.math.BigDecimal quantite = rs.getBigDecimal("quantite");
                    int articleId = rs.getInt("article_id");
                    String detail = detailCache.computeIfAbsent(articleId, this::buildStockDetail);
                    tableModel.addRow(new Object[]{
                            rs.getDate("date_mouvement"),
                            rs.getString("article_code"),
                            type,
                            rs.getString("methode_valorisation"),
                            signedQuantity(type, quantite),
                            rs.getBigDecimal("pu"),
                            rs.getBigDecimal("valeur_mouvement"),
                            rs.getBigDecimal("stock_qte_apres"),
                            detail,
                            rs.getBigDecimal("valeur_stock_apres"),
                            rs.getBigDecimal("cump_apres"),
                            rs.getString("source_tiers"),
                            rs.getString("source_reference"),
                            articleId,
                            rs.getInt("mouvement_id")
                    });
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur chargement: " + e.getMessage());
        }

        updateStockGlobal();
    }

    private void updateStockGlobal() {
        String sql = "SELECT COALESCE(SUM(valeur_stock_actuelle), 0) AS total FROM article";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            BigDecimal total = BigDecimal.ZERO;
            if (rs.next()) {
                total = rs.getBigDecimal("total");
            }
            stockGlobalLabel.setText("Stock global: " + (total != null ? total.toPlainString() : "0"));
        } catch (SQLException e) {
            stockGlobalLabel.setText("Stock global: Erreur");
        }
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
        } catch (SQLException e) {
            return "Erreur";
        }
        return sb.length() == 0 ? "-" : sb.toString();
    }

    private void showStockDetail(int articleId, String articleCode) {
        DefaultTableModel lotsModel = new DefaultTableModel(new String[]{"Qte restante", "PU"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        String sql = "SELECT quantite_restante, prix_unitaire FROM lot "
                + "WHERE article_id = ? AND epuise = false ORDER BY date_entree, id";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, articleId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lotsModel.addRow(new Object[]{
                            rs.getBigDecimal("quantite_restante"),
                            rs.getBigDecimal("prix_unitaire")
                    });
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur chargement lots: " + e.getMessage());
            return;
        }

        JTable lotsTable = new JTable(lotsModel);
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this),
                "Detail stock - " + articleCode, Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setContentPane(new JScrollPane(lotsTable));
        dialog.setSize(420, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private String buildDateFilter() {
        boolean hasStart = startDate.getDate() != null;
        boolean hasEnd = endDate.getDate() != null;
        if (!hasStart && !hasEnd) {
            return "";
        }
        if (hasStart && hasEnd) {
            return "date_mouvement BETWEEN ? AND ?";
        }
        if (hasStart) {
            return "date_mouvement >= ?";
        }
        return "date_mouvement <= ?";
    }

    private void bindDateFilter(PreparedStatement ps) throws SQLException {
        int index = 1;
        Date start = startDate.getDate();
        Date end = endDate.getDate();
        if (start != null && end != null) {
            ps.setDate(index++, new java.sql.Date(start.getTime()));
            ps.setDate(index, new java.sql.Date(end.getTime()));
        } else if (start != null) {
            ps.setDate(index, new java.sql.Date(start.getTime()));
        } else if (end != null) {
            ps.setDate(index, new java.sql.Date(end.getTime()));
        }
    }

    private void showDetail(int mouvementId) {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Detail mouvement", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setContentPane(new MouvementDetailPanel(mouvementId));
        dialog.setSize(1050, 620);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private java.math.BigDecimal signedQuantity(String type, java.math.BigDecimal quantite) {
        if (quantite == null) {
            return java.math.BigDecimal.ZERO;
        }
        return "SORTIE".equalsIgnoreCase(type) ? quantite.negate() : quantite;
    }

    private static class MovementRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof BigDecimal && (column == 4 || column == 7)) {
                value = formatQuantity((BigDecimal) value);
            }
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            String type = String.valueOf(table.getValueAt(row, 2));
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

    private static String formatQuantity(BigDecimal value) {
        return value == null ? "0" : value.setScale(0, RoundingMode.HALF_UP).toPlainString();
    }
}
