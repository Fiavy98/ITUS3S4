package views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import controllers.EtatStockController;
import controllers.ProduitController;
import models.*;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

public class EtatStockView extends JPanel {

    public JButton ajout, mvnt, etat;

    public JFormattedTextField txtDate;
    public JComboBox<Object> cbProduit;
    public JButton btnFiltrer;

    public JTable table;
    public DefaultTableModel model;

    private EtatStockController controller = new EtatStockController();
    private ProduitController produitController = new ProduitController();

    public EtatStockView(MainFrame frame) {

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 245, 245));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        initMenu(frame);
        initTable();

        loadProduits();
        loadEtatStock();
    }

    // ================= CHARGEMENT PRODUITS =================

    private void loadProduits() {
        cbProduit.removeAllItems();
        cbProduit.addItem("-- Tous les produits --");

        try {
            List<Produit> produits = produitController.getAll();
            for (Produit p : produits) {
                cbProduit.addItem(p);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur chargement produits: " + e.getMessage());
        }
    }

    // ================= MENU =================

    private void initMenu(MainFrame frame) {
        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BorderLayout());

        JPanel menu = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        ajout = new JButton("Ajout");
        mvnt = new JButton("Mouvement");
        etat = new JButton("Etat");

        ajout.addActionListener(e -> frame.showPage("ajout"));
        mvnt.addActionListener(e -> frame.showPage("mouvement"));
        etat.addActionListener(e -> frame.showPage("etat"));

        menu.add(ajout);
        menu.add(mvnt);
        menu.add(etat);

        // ================= FILTRE =================
        JPanel filter = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filter.setBorder(BorderFactory.createTitledBorder("Filtre Etat Stock"));

        filter.add(new JLabel("Produit :"));

        cbProduit = new JComboBox<>();
        cbProduit.setPreferredSize(new Dimension(150, 25));
        filter.add(cbProduit);

        filter.add(new JLabel("Date :"));

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        txtDate = new JFormattedTextField(format);
        txtDate.setColumns(10);
        txtDate.setPreferredSize(new Dimension(120, 25));
        txtDate.setToolTipText("Format: yyyy-MM-dd  (ex: 2024-12-31)");

        filter.add(txtDate);

        // Bouton Réinitialiser
        JButton btnReset = new JButton("Réinitialiser");
        btnReset.addActionListener(e -> {
            txtDate.setValue(null);
            txtDate.setText("");
            cbProduit.setSelectedIndex(0);
            loadEtatStock();
        });
        filter.add(btnReset);

        btnFiltrer = new JButton("Filtrer");
        btnFiltrer.setBackground(new Color(0, 123, 255));
        btnFiltrer.setForeground(Color.WHITE);

        // ✅ FIX: ActionListener branché sur le bouton Filtrer
        btnFiltrer.addActionListener(e -> loadEtatStock());

        filter.add(btnFiltrer);

        topContainer.add(menu, BorderLayout.NORTH);
        topContainer.add(filter, BorderLayout.SOUTH);

        add(topContainer, BorderLayout.NORTH);
    }

    // ================= TABLE =================

    private void initTable() {

        String[] columns = {
                "Date",
                "Produit ID",
                "Nom",
                "Stock",
                "Valeur Stock",
                "Prix Moyen"
        };

        model = new DefaultTableModel(columns, 0);

        table = new JTable(model);
        table.setRowHeight(25);
        table.setFillsViewportHeight(true);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new EmptyBorder(10, 0, 0, 0));

        add(scroll, BorderLayout.CENTER);

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    int produitId = (int) table.getValueAt(row, 1);
                    new DetailEtatDialog(produitId).setVisible(true);
                }
            }
        });
    }

    public void loadEtatStock() {

        model.setRowCount(0);

        try {
            // ✅ FIX: Lecture et parsing robuste de la date
            LocalDate date = null;
            String texte = txtDate.getText();
            if (texte != null && !texte.trim().isEmpty()) {
                try {
                    date = LocalDate.parse(texte.trim());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Format de date invalide. Utilisez yyyy-MM-dd (ex: 2024-12-31)");
                    return;
                }
            }

            // ✅ FIX: Vérification correcte de la sélection du produit
            Object selected = cbProduit.getSelectedItem();
            Produit produitSelectionne = (selected instanceof Produit) ? (Produit) selected : null;

            List<EtatStockDTO> liste;

            if (produitSelectionne != null) {
                liste = controller.getEtatStockByProduit(produitSelectionne.getId(), date);
            } else {
                liste = controller.getEtatStock(date);
            }

            for (EtatStockDTO e : liste) {
                model.addRow(new Object[]{
                        e.getLastMovementDate(),
                        e.getProduitId(),
                        e.getNomProduit(),
                        e.getStockQuantite(),
                        String.format("%.2f", e.getValeurStock()),
                        String.format("%.2f", e.getPrixMoyen())
                });
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erreur filtre: " + ex.getMessage());
        }
    }
}
