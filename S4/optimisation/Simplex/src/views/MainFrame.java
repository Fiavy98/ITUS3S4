package src.views;

import javax.swing.*;
import java.awt.*;


public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel container;

    private Ajout ajout;
    private FormeStandard standard;
    private Phase1 phase1;
    private Phase2 phase2;
    private Resultat resultat;

    public MainFrame() {

        setTitle("Tsy kobo");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        container = new JPanel(cardLayout);

        ajout = new Ajout(this);
        standard = new FormeStandard(this);
        phase1 = new Phase1(this);
        phase2 = new Phase2(this);
        resultat = new Resultat(this);

        container.add(ajout, "ajout");
        container.add(standard, "standard");
        container.add(phase1, "phase1");
        container.add(phase2, "phase2");
        container.add(resultat, "resultat");


        add(container);
        setVisible(true);
    }

    public void showPage(String name) {
        cardLayout.show(container, name);
        container.revalidate();
        container.repaint();
    }
}