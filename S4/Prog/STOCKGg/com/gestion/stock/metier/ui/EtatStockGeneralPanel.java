package com.gestion.stock.metier.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.gestion.stock.config.DatabaseConfig;

public class EtatStockGeneralPanel extends JPanel {
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JTextField searchField = new JTextField(18);
    private final JLabel summaryLabel = new JLabel("Produits: 0");

    public EtatStockGeneralPanel() {
        setLayout(new BorderLayout(8, 8));
        tableModel = new DefaultTableModel(new String[]{
                "Code", "Categorie", "Unite", "Methode", "Stock actuel", "Stock disponible",
                "Stock reserve", "CUMP", "Valeur stock", "article_id"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setDefaultRenderer(Object.class, new StockRenderer());
        table.setRowHeight(24);
        hideColumn(9);

        initToolbar();
        add(new JScrollPane(table), BorderLayout.CENTER);
        initFooter();
        initInteractions();
        loadData();
    }

    private void initToolbar() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton searchButton = new JButton("Rechercher");
        JButton clearButton = new JButton("Effacer");
        JButton refreshButton = new JButton("Rafraichir");
        JButton detailButton = new JButton("Voir detail");

        searchButton.addActionListener(e -> loadData());
        clearButton.addActionListener(e -> {
            searchField.setText("");
            loadData();
        });
        refreshButton.addActionListener(e -> loadData());
        detailButton.addActionListener(e -> openSelectedDetail());

        topPanel.add(new JLabel("Filtre code/categorie:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(clearButton);
        topPanel.add(refreshButton);
        topPanel.add(detailButton);
        add(topPanel, BorderLayout.NORTH);
    }

    private void initFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footer.add(summaryLabel);
        add(footer, BorderLayout.SOUTH);
    }

    private void initInteractions() {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    openSelectedDetail();
                }
            }
        });
    }

    private void loadData() {
        tableModel.setRowCount(0);
        StringBuilder sql = new StringBuilder(
                "SELECT v.article_id, v.code, v.categorie, v.unite_mesure, a.methode_gestion AS methode, "
                        + "v.stock_actuel, v.stock_disponible, v.stock_reserve, v.cump_actuel, "
                        + "v.valeur_stock_actuelle, v.nb_lots_actifs, v.dernier_mouvement "
                        + "FROM v_etat_stock_general v "
                        + "JOIN article a ON a.id = v.article_id"
        );

        String filter = searchField.getText() != null ? searchField.getText().trim().toLowerCase() : "";
        boolean hasFilter = !filter.isEmpty();
        if (hasFilter) {
            sql.append(" WHERE LOWER(v.code) LIKE ? OR LOWER(COALESCE(v.categorie, '')) LIKE ?");
        }
        sql.append(" ORDER BY v.code");

        int count = 0;
        BigDecimal totalValue = BigDecimal.ZERO;
        BigDecimal totalStock = BigDecimal.ZERO;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            if (hasFilter) {
                String like = "%" + filter + "%";
                ps.setString(1, like);
                ps.setString(2, like);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BigDecimal stockActuel = rs.getBigDecimal("stock_actuel");
                    BigDecimal stockDisponible = rs.getBigDecimal("stock_disponible");
                    BigDecimal stockReserve = rs.getBigDecimal("stock_reserve");
                    BigDecimal valeurStock = rs.getBigDecimal("valeur_stock_actuelle");
                    tableModel.addRow(new Object[]{
                            rs.getString("code"),
                            rs.getString("categorie"),
                            rs.getString("unite_mesure"),
                            rs.getString("methode"),
                            stockActuel,
                            stockDisponible,
                            stockReserve,
                            rs.getBigDecimal("cump_actuel"),
                            valeurStock,
                            rs.getInt("article_id")
                    });
                    count++;
                    totalStock = totalStock.add(stockActuel != null ? stockActuel : BigDecimal.ZERO);
                    totalValue = totalValue.add(valeurStock != null ? valeurStock : BigDecimal.ZERO);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur chargement etat stock: " + e.getMessage());
        }

        summaryLabel.setText("Produits: " + count
                + " | Stock total: " + totalStock.toPlainString()
                + " | Valeur totale: " + totalValue.toPlainString());
    }

    private void openSelectedDetail() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selectionne un produit d'abord.");
            return;
        }
        Object articleIdValue = table.getValueAt(row, 9);
        Object codeValue = table.getValueAt(row, 0);
        if (articleIdValue == null) {
            return;
        }

        int articleId = Integer.parseInt(articleIdValue.toString());
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this),
                "Detail produit - " + codeValue, Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setContentPane(new DetailArticlePanel(articleId));
        dialog.setSize(1100, 700);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void hideColumn(int columnIndex) {
        table.getColumnModel().getColumn(columnIndex).setMinWidth(0);
        table.getColumnModel().getColumn(columnIndex).setMaxWidth(0);
        table.getColumnModel().getColumn(columnIndex).setPreferredWidth(0);
    }

    private static class StockRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (!isSelected) {
                BigDecimal stockActuel = asBigDecimal(table.getValueAt(row, 4));
                BigDecimal stockDisponible = asBigDecimal(table.getValueAt(row, 5));
                if (stockActuel != null && stockActuel.compareTo(BigDecimal.ZERO) <= 0) {
                    c.setBackground(new Color(255, 220, 220));
                } else if (stockDisponible != null && stockDisponible.compareTo(BigDecimal.ZERO) <= 0) {
                    c.setBackground(new Color(255, 240, 200));
                } else {
                    c.setBackground(table.getBackground());
                }
            }
            return c;
        }

        private BigDecimal asBigDecimal(Object value) {
            if (value instanceof BigDecimal bigDecimal) {
                return bigDecimal;
            }
            return null;
        }
    }
}
