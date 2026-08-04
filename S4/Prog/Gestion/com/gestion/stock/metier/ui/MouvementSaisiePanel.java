package com.gestion.stock.metier.ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;

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
        refreshBtn.addActionListener(e -> refreshArticleLists());
        toolBar.add(refreshBtn);
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
        JPanel panel = new JPanel(new GridBagLayout());
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
        panel.add(new JLabel("Article:"), gbc);
        gbc.gridx = 1;
        panel.add(articleCombo, gbc);
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel("Date:"), gbc);
        gbc.gridx = 1;
        panel.add(dateChooser, gbc);
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel("Quantité:"), gbc);
        gbc.gridx = 1;
        panel.add(quantiteField, gbc);
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel("Prix unitaire:"), gbc);
        gbc.gridx = 1;
        panel.add(prixField, gbc);
        row++;
        JButton validerBtn = new JButton("Valider entrée");
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        panel.add(validerBtn, gbc);

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

        return panel;
    }

    private JPanel createSortiePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
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
        panel.add(new JLabel("Article:"), gbc);
        gbc.gridx = 1;
        panel.add(articleCombo, gbc);
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel("Date:"), gbc);
        gbc.gridx = 1;
        panel.add(dateChooser, gbc);
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel("Quantité:"), gbc);
        gbc.gridx = 1;
        panel.add(quantiteField, gbc);
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel("Méthode article:"), gbc);
        gbc.gridx = 1;
        panel.add(methodeValueLabel, gbc);
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel("PU sortie (decoratif FIFO/LIFO):"), gbc);
        gbc.gridx = 1;
        panel.add(puSortieField, gbc);
        row++;
        JButton validerBtn = new JButton("Valider sortie");
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        panel.add(validerBtn, gbc);

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

        return panel;
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
}
