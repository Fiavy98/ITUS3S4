package com.gestion.stock.metier.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.math.BigDecimal;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.gestion.stock.config.DatabaseConfig;

/**
 * Panel pour afficher le tableau complet des écritures comptables
 * avec date, description, numéro de compte, débits, crédits
 */
public class EcritureTablePanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;

    public EcritureTablePanel() {
        setLayout(new BorderLayout());
        initControls();
        initTable();
        loadData();
    }

    private void initControls() {
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton refreshBtn = new JButton("Rafraichir");
        JLabel summaryLabel = new JLabel("Résumé: ");
        
        refreshBtn.addActionListener(e -> loadData());
        
        controlPanel.add(refreshBtn);
        controlPanel.add(summaryLabel);
        
        add(controlPanel, BorderLayout.NORTH);
    }

    private void initTable() {
        tableModel = new DefaultTableModel(new String[]{
                "Date", "Libellé", "Journal", "Compte", "Description", "Débit", "Crédit", "Mouvement ID"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setPreferredWidth(100);  // Date
        table.getColumnModel().getColumn(1).setPreferredWidth(250);  // Libellé mère
        table.getColumnModel().getColumn(2).setPreferredWidth(80);   // Journal
        table.getColumnModel().getColumn(3).setPreferredWidth(80);   // Compte
        table.getColumnModel().getColumn(4).setPreferredWidth(200);  // Description fils
        table.getColumnModel().getColumn(5).setPreferredWidth(100);  // Débit
        table.getColumnModel().getColumn(6).setPreferredWidth(100);  // Crédit
        table.getColumnModel().getColumn(7).setPreferredWidth(100);  // Mouvement ID

        // Centrer et formatter les colonnes numériques
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        DefaultTableCellRenderer numberRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.RIGHT);
                
                // Colorer les débits/crédits positifs en vert
                if (column == 5 || column == 6) {
                    if (value != null && !value.toString().equals("0")) {
                        c.setBackground(isSelected ? table.getSelectionBackground() : new Color(220, 255, 220));
                    } else {
                        c.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
                    }
                }
                
                return c;
            }
        };

        table.getColumnModel().getColumn(5).setCellRenderer(numberRenderer);  // Débit
        table.getColumnModel().getColumn(6).setCellRenderer(numberRenderer);  // Crédit
        table.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);  // Équilibrée
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement()) {
            
            String query = "SELECT " +
                    "    em.date_ecriture, " +
                    "    em.libelle as libelle_mere, " +
                    "    em.journal, " +
                    "    COALESCE(ef.numero_compte, 'N/A') as numero_compte, " +
                    "    COALESCE(ef.libelle, '') as libelle_fils, " +
                    "    COALESCE(ef.debit, 0) as debit, " +
                    "    COALESCE(ef.credit, 0) as credit, " +
                    "    CASE WHEN em.id IN (" +
                    "     ecriture_mere em " +
                    "LEFT JOIN ecriture_fils ef ON em.id = ef.ecriture_mere_id " +
                    "ORDER BY em.date_ecriture DESC, em.id DESC, ef.id";
            
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                Object[] row = new Object[]{
                    rs.getDate(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5),
                    formatNumber(rs.getBigDecimal(6)),
                    formatNumber(rs.getBigDecimal(7)),
                    rs.getString(8),
                    rs.getInt(9) > 0 ? rs.getInt(9) : ""
                };
                tableModel.addRow(row);
            }
            
        } catch (SQLExceptInt(8) > 0 ? rs.getInt(8
            tableModel.addRow(new Object[]{"Erreur de chargement", "", "", "", "", "", "", "", ""});
        }
    }

    private String formatNumber(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) == 0) {
            return "";
        }
        return String.format("%.2f €", value);
    }

    private void exportData() {
        try {
            StringBuilder sb = new StringBuilder();
            
            // En-tête
            for (int i = 0; i < tableModel.getColumnCount(); i++) {
                sb.append(tableModel.getColumnName(i));
