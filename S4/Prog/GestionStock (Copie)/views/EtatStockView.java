package views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import controllers.EtatStockController;
import models.*;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class EtatStockView extends JPanel {

    public JButton ajout, mvnt, etat;

    public JFormattedTextField txtDate;
    public JComboBox<Produit> cbProduit;
    public JButton btnFiltrer;

    public JTable table;
    public DefaultTableModel model;

    private EtatStockController controller = new EtatStockController();

    public EtatStockView(MainFrame frame) {

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 245, 245));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        initMenu(frame);
        initFilter(frame);
        initTable();

        // ================= CHARGEMENT INITIAL =================
        loadEtatStock();
    }

    // ================= MENU =================

    private void initMenu(MainFrame frame) {

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

        add(menu, BorderLayout.NORTH);
    }

    // ================= FILTER =================

    private void initFilter(MainFrame frame) {

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

        filter.add(txtDate);

        btnFiltrer = new JButton("Filtrer");
        btnFiltrer.setBackground(new Color(0, 123, 255));
        btnFiltrer.setForeground(Color.WHITE);

        filter.add(btnFiltrer);

        add(filter, BorderLayout.NORTH);
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

                    int produitId =
                            (int) table.getValueAt(row, 1);

                    new DetailEtatDialog(produitId)
                            .setVisible(true);
                }
            }
        });
    }

    public void loadEtatStock() {

        model.setRowCount(0);

        List<EtatStockDTO> liste =
                controller.getEtatStock();

        for (EtatStockDTO e : liste) {

            model.addRow(new Object[]{

                    new java.util.Date(),

                    e.getProduitId(),
                    e.getNomProduit(),

                    e.getStockQuantite(),
                    e.getValeurStock(),
                    e.getPrixMoyen()
            });
        }
    }
}