package com.gestion.stock.metier.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;

import com.gestion.stock.core.ui.GenericTableModel;
import com.gestion.stock.metier.entity.Article;
import com.gestion.stock.metier.entity.Categorie;
import com.gestion.stock.metier.repository.CategorieRepository;
import com.gestion.stock.metier.service.ArticleService;

public class ArticleFormPanel extends JPanel {
    private final JTable table;
    private final GenericTableModel<Article> tableModel;
    private final ArticleService articleService = new ArticleService();
    private final CategorieRepository categorieRepository = new CategorieRepository();

    public ArticleFormPanel() {
        setLayout(new BorderLayout(5, 5));
        initToolbar();
        tableModel = new GenericTableModel<>(List.of(), articleService.getColumnDefinitions(), Article.class);
        table = new JTable(tableModel);
        table.setDefaultRenderer(Object.class, new CategoryCellRenderer());
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.rowAtPoint(e.getPoint());
                    if (row >= 0) {
                        showDetail(tableModel.getEntityAt(row));
                    }
                }
            }
        });
        add(new JScrollPane(table), BorderLayout.CENTER);
        loadData();
    }

    private void initToolbar() {
        JToolBar toolBar = new JToolBar();
        JButton btnNew = new JButton("Nouveau");
        JButton btnEdit = new JButton("Modifier");
        JButton btnDelete = new JButton("Supprimer");
        JButton btnRefresh = new JButton("Rafraîchir");

        btnNew.addActionListener(e -> showForm(null));
        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                showForm(tableModel.getEntityAt(row));
            }
        });
        btnDelete.addActionListener(e -> deleteArticle());
        btnRefresh.addActionListener(e -> loadData());

        toolBar.add(btnNew);
        toolBar.add(btnEdit);
        toolBar.add(btnDelete);
        toolBar.add(btnRefresh);
        add(toolBar, BorderLayout.NORTH);
    }

    private void loadData() {
        try {
            tableModel.setData(articleService.findAll());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur chargement: " + ex.getMessage());
        }
    }

    private void showForm(Article article) {
        boolean isNew = article == null;
        Article workingArticle = isNew ? new Article() : article;
        List<Categorie> categories = loadCategories();

        JTextField codeField = new JTextField(20);
        JComboBox<Object> categorieCombo = buildCategoryCombo(categories);
        JTextField uniteField = new JTextField(20);
        JComboBox<String> methodeCombo = new JComboBox<>(new String[]{"FIFO", "LIFO", "CUMP"});
        JCheckBox actifCheck = new JCheckBox("Actif");

        codeField.setText(workingArticle.getCode() != null ? workingArticle.getCode() : "");
        uniteField.setText(workingArticle.getUniteMesure() != null ? workingArticle.getUniteMesure() : "unité");
        String methode = workingArticle.getMethodeGestion() != null ? workingArticle.getMethodeGestion() : "CUMP";
        methodeCombo.setSelectedItem(methode);
        actifCheck.setSelected(workingArticle.isActif());
        selectCategory(categorieCombo, categories, workingArticle.getCategorieId());

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addRow(formPanel, gbc, 0, "nom",codeField);
        addRow(formPanel, gbc, 1, "Catégorie", categorieCombo);
        addRow(formPanel, gbc, 2, "Unité *", uniteField);
        addRow(formPanel, gbc, 3, "Méthode", methodeCombo);
        addRow(formPanel, gbc, 4, "", actifCheck);

        int result = JOptionPane.showConfirmDialog(this, formPanel, "Fiche article", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String code = codeField.getText() != null ? codeField.getText().trim() : "";
        if (code.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le code est obligatoire.");
            return;
        }

        workingArticle.setCode(code);
        workingArticle.setUniteMesure(uniteField.getText() != null && !uniteField.getText().trim().isEmpty()
                ? uniteField.getText().trim()
                : "unité");
        workingArticle.setMethodeGestion(methodeCombo.getSelectedItem() != null
            ? methodeCombo.getSelectedItem().toString()
            : "CUMP");
        workingArticle.setActif(actifCheck.isSelected());
        workingArticle.setCategorieId(resolveCategoryId(categorieCombo.getSelectedItem()));

        try {
            articleService.save(workingArticle);
            loadData();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur sauvegarde: " + ex.getMessage());
        }
    }

    private void showDetail(Article article) {
        if (article == null || article.getId() == null) {
            return;
        }
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Détail article", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setContentPane(new DetailArticlePanel(article.getId()));
        dialog.setSize(1000, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void deleteArticle() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            Article article = tableModel.getEntityAt(row);
            if (JOptionPane.showConfirmDialog(this, "Supprimer " + article.getCode() + " ?", "Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                try {
                    articleService.deleteById(article.getId());
                    loadData();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erreur suppression: " + ex.getMessage());
                }
            }
        }
    }

    private List<Categorie> loadCategories() {
        try {
            return categorieRepository.findAll();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur chargement catégories: " + ex.getMessage());
            return new ArrayList<>();
        }
    }

    private JComboBox<Object> buildCategoryCombo(List<Categorie> categories) {
        JComboBox<Object> comboBox = new JComboBox<>();
        comboBox.addItem(null);
        for (Categorie categorie : categories) {
            comboBox.addItem(categorie);
        }
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setText(formatCategoryValue(value));
                return this;
            }
        });
        return comboBox;
    }

    private void selectCategory(JComboBox<Object> comboBox, List<Categorie> categories, Integer categorieId) {
        if (categorieId == null) {
            comboBox.setSelectedIndex(0);
            return;
        }
        for (Categorie categorie : categories) {
            if (categorie.getId() != null && categorie.getId().equals(categorieId)) {
                comboBox.setSelectedItem(categorie);
                return;
            }
        }
        comboBox.setSelectedIndex(0);
    }

    

    private Integer resolveCategoryId(Object selectedItem) {
        if (selectedItem instanceof Categorie categorie) {
            return categorie.getId();
        }
        return null;
    }

    private String formatCategoryValue(Object value) {
        if (value == null) {
            return "Aucune";
        }
        return value.toString();
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.weightx = 0;
        if (!label.isEmpty()) {
            panel.add(new JLabel(label), gbc);
        }
        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(field, gbc);
    }

    private static class CategoryCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setText(value == null ? "" : value.toString());
            return this;
        }
    }
}
