package views;

import javax.swing.*;
import java.awt.*;

import controllers.*;
import services.*;
import db.ConnectionDB;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel container;

    private AjoutStockView ajoutView;
    private MvntStockView mvntView;
    private EtatStockView etatView;

    public MainFrame() {

        setTitle("Gestion Stock");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        container = new JPanel(cardLayout);

        try {
            java.sql.Connection conn = ConnectionDB.getConnection();


            ajoutView = new AjoutStockView(this);
            mvntView = new MvntStockView(this);
            etatView = new EtatStockView(this);


            container.add(ajoutView, "ajout");
            container.add(mvntView, "mouvement");
            container.add(etatView, "etat");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur de connexion: " + e.getMessage());
            System.exit(1);
        }

        add(container);
        setVisible(true);
    }

    public void showPage(String name) {
        cardLayout.show(container, name);
        container.revalidate();
        container.repaint();
    }
}