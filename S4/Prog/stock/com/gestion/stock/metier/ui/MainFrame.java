package com.gestion.stock.metier.ui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private CardLayout cardLayout = new CardLayout();
    private JPanel contentPanel = new JPanel(cardLayout);

    public MainFrame() {

        setTitle("Gestion Stock");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initMenu();
        initPages();

        add(contentPanel);
    }

    private void initMenu() {

        JPanel menu = new JPanel();

        JButton btnProduit = new JButton("Produits");
        JButton btnMouvement = new JButton("Mouvements");
        JButton btnEtat = new JButton("Etat Stock");

        btnProduit.addActionListener(e -> cardLayout.show(contentPanel, "produit"));
        btnMouvement.addActionListener(e -> cardLayout.show(contentPanel, "mouvement"));
        btnEtat.addActionListener(e -> cardLayout.show(contentPanel, "etat"));

        menu.add(btnProduit);
        menu.add(btnMouvement);
        menu.add(btnEtat);

        add(menu, BorderLayout.NORTH);
    }

    private void initPages() {

        // PAGE 1
        contentPanel.add(new ProduitFormPanel(), "produit");

        // PAGE 2
        contentPanel.add(new MouvementSaisiePanel(), "mouvement");

        // PAGE 3
        contentPanel.add(new EtatStockPanel(), "etat");
    }
}