package com.gestion.stock.metier.ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import com.gestion.stock.exception.StockInsuffisantException;
import com.gestion.stock.metier.entity.Article;
import com.gestion.stock.metier.service.ArticleService;
import com.gestion.stock.metier.service.StockService;
import com.toedter.calendar.JDateChooser;

public class MouvementSaisiePanel extends JPanel {
    private final JTabbedPane tabbedPane;
    private final StockService stockService = new StockService();
    private final ArticleService articleService = new ArticleService();
    private JComboBox<Article> entreeArticleCombo;
    private JComboBox<Article> sortieArticleCombo;
    private DefaultTableModel batchEntreeModel;
    private DefaultTableModel batchSortieModel;

    public MouvementSaisiePanel() {
        setLayout(new BorderLayout());
        add(createHeaderBar(), BorderLayout.NORTH);
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Entrée", createEntreePanel());
        tabbedPane.addTab("Sortie", createSortiePanel());
        add(tabbedPane, BorderLayout.CENTER);
    }

    private JToolBar createHeaderBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        JButton refreshBtn = new JButton("Rafraîchir articles");
        JButton validateAllBtn = new JButton("Valider tout (entrées + sorties)");
        refreshBtn.addActionListener(e -> refreshArticleLists());
        toolBar.add(refreshBtn);
        validateAllBtn.addActionListener(e -> validateAllBatches());
        toolBar.add(validateAllBtn);
        return toolBar;
    }

    private void refreshArticleLists() {
        try {
            List<Article> articles = articleService.findAll();
            reloadCombo(entreeArticleCombo, articles);
            reloadCombo(sortieArticleCombo, articles);
            JOptionPane.showMessageDialog(this, "Liste des articles mise à jour");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur rafraîchissement articles: " + ex.getMessage());
        }
    }

    private void reloadCombo(JComboBox<Article> comboBox, List<Article> articles) {
        if (comboBox == null) {
            return;
        }
        Article selected = (Article) comboBox.getSelectedItem();
        comboBox.removeAllItems();
        for (Article article : articles) {
            comboBox.addItem(article);
        }
        if (selected != null && selected.getId() != null) {
            for (int i = 0; i < comboBox.getItemCount(); i++) {
                Article candidate = comboBox.getItemAt(i);
                if (candidate != null && candidate.getId() != null && candidate.getId().equals(selected.getId())) {
                    comboBox.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private JPanel createEntreePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        JComboBox<Article> articleCombo = new JComboBox<>();
        entreeArticleCombo = articleCombo;
        JDateChooser dateChooser = new JDateChooser();
        JTextField quantiteField = new JTextField(15);
        JTextField prixField = new JTextField(15);

        loadArticlesIntoCombo(articleCombo);

        int row = 0;
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Article:"), gbc);
        gbc.gridx = 1;
        formPanel.add(articleCombo, gbc);
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Date:"), gbc);
        gbc.gridx = 1;
        formPanel.add(dateChooser, gbc);
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Quantité:"), gbc);
        gbc.gridx = 1;
        formPanel.add(quantiteField, gbc);
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Prix unitaire:"), gbc);
        gbc.gridx = 1;
        formPanel.add(prixField, gbc);
        row++;
        JButton validerBtn = new JButton("Valider entrée");
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        formPanel.add(validerBtn, gbc);

        validerBtn.addActionListener(e -> {
            try {
                Article selected = (Article) articleCombo.getSelectedItem();
                if (selected == null) {
                    throw new IllegalArgumentException("Sélectionne un article");
                }
                if (dateChooser.getDate() == null) {
                    throw new IllegalArgumentException("La date est obligatoire");
                }
                LocalDate date = dateChooser.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                BigDecimal qte = new BigDecimal(quantiteField.getText());
                BigDecimal pu = new BigDecimal(prixField.getText());
                stockService.enregistrerEntree(selected, date, qte, pu, null, null, null, null);
                JOptionPane.showMessageDialog(this, "Entrée enregistrée");
                quantiteField.setText("");
                prixField.setText("");
                refreshArticleLists();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage());
            }
        });

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(buildMultiEntreePanel(), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createSortiePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        JComboBox<Article> articleCombo = new JComboBox<>();
        sortieArticleCombo = articleCombo;
        JDateChooser dateChooser = new JDateChooser();
        JTextField quantiteField = new JTextField(15);
        JTextField puSortieField = new JTextField(15);
        JLabel methodeValueLabel = new JLabel("-");

        loadArticlesIntoCombo(articleCombo);

        int row = 0;
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Article:"), gbc);
        gbc.gridx = 1;
        formPanel.add(articleCombo, gbc);
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Date:"), gbc);
        gbc.gridx = 1;
        formPanel.add(dateChooser, gbc);
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Quantité:"), gbc);
        gbc.gridx = 1;
        formPanel.add(quantiteField, gbc);
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Méthode article:"), gbc);
        gbc.gridx = 1;
        formPanel.add(methodeValueLabel, gbc);
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("PU sortie (decoratif FIFO/LIFO):"), gbc);
        gbc.gridx = 1;
        formPanel.add(puSortieField, gbc);
        row++;
        JButton validerBtn = new JButton("Valider sortie");
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        formPanel.add(validerBtn, gbc);

        articleCombo.addActionListener(e -> {
            Article selected = (Article) articleCombo.getSelectedItem();
            String methode = selected != null && selected.getMethodeGestion() != null
                    ? selected.getMethodeGestion().toUpperCase()
                    : "CUMP";
            methodeValueLabel.setText(methode);
            boolean enablePu = "FIFO".equals(methode) || "LIFO".equals(methode);
            puSortieField.setEnabled(enablePu);
            if (!enablePu) {
                puSortieField.setText("");
            }
        });

        validerBtn.addActionListener(e -> {
            try {
                Article selected = (Article) articleCombo.getSelectedItem();
                if (selected == null) {
                    throw new IllegalArgumentException("Sélectionne un article");
                }
                if (dateChooser.getDate() == null) {
                    throw new IllegalArgumentException("La date est obligatoire");
                }
                LocalDate date = dateChooser.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                BigDecimal qte = new BigDecimal(quantiteField.getText());
                BigDecimal puSortie = null;
                if (!puSortieField.getText().trim().isEmpty()) {
                    puSortie = new BigDecimal(puSortieField.getText().trim());
                }
                stockService.enregistrerSortie(selected, date, qte, puSortie, null, null, null, null);
                JOptionPane.showMessageDialog(this, "Sortie enregistrée");
                quantiteField.setText("");
                puSortieField.setText("");
                refreshArticleLists();
            } catch (StockInsuffisantException ex) {
                JOptionPane.showMessageDialog(this, "Stock insuffisant: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage());
            }
        });

        articleCombo.setSelectedItem(articleCombo.getSelectedItem());

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(buildMultiSortiePanel(), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildMultiEntreePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Saisie multiple (Entrées)"));

        DefaultTableModel model = new DefaultTableModel(new String[]{"Article", "Date", "Quantité", "Prix"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };
        JTable table = new JTable(model);
        configureArticleEditor(table, 0);
        table.getColumnModel().getColumn(1).setCellEditor(new DateCellEditor());
        batchEntreeModel = model;

        JButton addRow = new JButton("Ajouter ligne");
        JButton removeRow = new JButton("Supprimer ligne");
        JButton validateAll = new JButton("Valider toutes les entrées");

        addRow.addActionListener(e -> model.addRow(new Object[]{"", "", "", ""}));
        removeRow.addActionListener(e -> removeSelectedRows(table, model));
        validateAll.addActionListener(e -> handleBatchEntrees(model));

        JPanel actions = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        actions.add(addRow, gbc);
        gbc.gridx = 1;
        actions.add(removeRow, gbc);
        gbc.gridx = 2;
        actions.add(validateAll, gbc);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(actions, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildMultiSortiePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Saisie multiple (Sorties)"));

        DefaultTableModel model = new DefaultTableModel(new String[]{"Article", "Date", "Quantité"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };
        JTable table = new JTable(model);
        configureArticleEditor(table, 0);
        table.getColumnModel().getColumn(1).setCellEditor(new DateCellEditor());
        batchSortieModel = model;

        JButton addRow = new JButton("Ajouter ligne");
        JButton removeRow = new JButton("Supprimer ligne");
        JButton validateAll = new JButton("Valider toutes les sorties");

        addRow.addActionListener(e -> model.addRow(new Object[]{"", "", ""}));
        removeRow.addActionListener(e -> removeSelectedRows(table, model));
        validateAll.addActionListener(e -> handleBatchSorties(model));

        JPanel actions = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        actions.add(addRow, gbc);
        gbc.gridx = 1;
        actions.add(removeRow, gbc);
        gbc.gridx = 2;
        actions.add(validateAll, gbc);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(actions, BorderLayout.SOUTH);
        return panel;
    }

    private void configureArticleEditor(JTable table, int columnIndex) {
        try {
            List<Article> articles = articleService.findAll();
            JComboBox<String> combo = new JComboBox<>();
            for (Article article : articles) {
                combo.addItem(article.getCode());
            }
            table.getColumnModel().getColumn(columnIndex).setCellEditor(new DefaultCellEditor(combo));
        } catch (Exception ignored) {
        }
    }

    private void removeSelectedRows(JTable table, DefaultTableModel model) {
        int[] rows = table.getSelectedRows();
        for (int i = rows.length - 1; i >= 0; i--) {
            model.removeRow(rows[i]);
        }
    }

    private void handleBatchEntrees(DefaultTableModel model) {
        try {
            Map<String, Article> articleMap = loadArticleMap();
            for (int i = 0; i < model.getRowCount(); i++) {
                String code = cellString(model, i, 0);
                LocalDate date = cellDate(model, i, 1);
                String qteStr = cellString(model, i, 2);
                String prixStr = cellString(model, i, 3);

                if (isRowEmpty(code, qteStr, prixStr) && date == null) {
                    continue;
                }

                if (code.isEmpty()) {
                    throw new IllegalArgumentException("Code article obligatoire (ligne " + (i + 1) + ")");
                }
                Article article = articleMap.get(code);
                if (article == null) {
                    throw new IllegalArgumentException("Article introuvable: " + code);
                }
                if (date == null) {
                    throw new IllegalArgumentException("Date obligatoire (ligne " + (i + 1) + ")");
                }
                if (qteStr.isEmpty()) {
                    throw new IllegalArgumentException("Quantité obligatoire (ligne " + (i + 1) + ")");
                }
                if (prixStr.isEmpty()) {
                    throw new IllegalArgumentException("Prix obligatoire (ligne " + (i + 1) + ")");
                }
                try {
                    BigDecimal quantite = new BigDecimal(qteStr);
                    BigDecimal prix = new BigDecimal(prixStr);
                    stockService.enregistrerEntree(article, date, quantite, prix, null, null, null, null);
                } catch (NumberFormatException nfe) {
                    throw new IllegalArgumentException("Format numérique invalide (ligne " + (i + 1) + ")");
                }
            }

            JOptionPane.showMessageDialog(this, "Entrées enregistrées");
            model.setRowCount(0);
            refreshArticleLists();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur saisie multiple: " + (ex.getMessage() != null ? ex.getMessage() : "Erreur inconnue"));
        }
    }

    private void handleBatchSorties(DefaultTableModel model) {
        try {
            Map<String, Article> articleMap = loadArticleMap();
            for (int i = 0; i < model.getRowCount(); i++) {
                String code = cellString(model, i, 0);
                LocalDate date = cellDate(model, i, 1);
                String qteStr = cellString(model, i, 2);
                if (isRowEmpty(code, qteStr) && date == null) {
                    continue;
                }

                if (code.isEmpty()) {
                    throw new IllegalArgumentException("Code article obligatoire (ligne " + (i + 1) + ")");
                }
                Article article = articleMap.get(code);
                if (article == null) {
                    throw new IllegalArgumentException("Article introuvable: " + code);
                }
                if (date == null) {
                    throw new IllegalArgumentException("Date obligatoire (ligne " + (i + 1) + ")");
                }
                if (qteStr.isEmpty()) {
                    throw new IllegalArgumentException("Quantité obligatoire (ligne " + (i + 1) + ")");
                }
                try {
                    BigDecimal quantite = new BigDecimal(qteStr);
                    stockService.enregistrerSortie(article, date, quantite, null, null, null, null, null);
                } catch (NumberFormatException nfe) {
                    throw new IllegalArgumentException("Format numérique invalide (ligne " + (i + 1) + ")");
                }
            }

            JOptionPane.showMessageDialog(this, "Sorties enregistrées");
            model.setRowCount(0);
            refreshArticleLists();
        } catch (StockInsuffisantException ex) {
            JOptionPane.showMessageDialog(this, "Stock insuffisant: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur saisie multiple: " + (ex.getMessage() != null ? ex.getMessage() : "Erreur inconnue"));
        }
    }

    private Map<String, Article> loadArticleMap() throws Exception {
        List<Article> articles = articleService.findAll();
        Map<String, Article> map = new HashMap<>();
        for (Article article : articles) {
            map.put(article.getCode(), article);
        }
        return map;
    }

    private boolean isRowEmpty(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return false;
            }
        }
        return true;
    }

    private String cellString(DefaultTableModel model, int row, int col) {
        Object value = model.getValueAt(row, col);
        return value == null ? "" : value.toString().trim();
    }

    private LocalDate cellDate(DefaultTableModel model, int row, int col) {
        Object value = model.getValueAt(row, col);
        if (value instanceof Date) {
            return ((Date) value).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        String text = value == null ? "" : value.toString().trim();
        if (text.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(text, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (java.time.format.DateTimeParseException e) {
            throw new IllegalArgumentException("Format de date invalide (attendu: YYYY-MM-DD): " + text);
        }
    }

    private static class DateCellEditor extends AbstractCellEditor implements TableCellEditor {
        private final JDateChooser chooser = new JDateChooser();

        @Override
        public Object getCellEditorValue() {
            return chooser.getDate();
        }

        @Override
        public java.awt.Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (value instanceof Date) {
                chooser.setDate((Date) value);
            } else {
                chooser.setDate(null);
            }
            return chooser;
        }
    }

    private void loadArticlesIntoCombo(JComboBox<Article> comboBox) {
        try {
            List<Article> articles = articleService.findAll();
            comboBox.removeAllItems();
            for (Article article : articles) {
                comboBox.addItem(article);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur chargement articles: " + e.getMessage());
        }
    }

    private void validateAllBatches() {
        if (batchEntreeModel != null) {
            handleBatchEntrees(batchEntreeModel);
        }
        if (batchSortieModel != null) {
            handleBatchSorties(batchSortieModel);
        }
    }
}
