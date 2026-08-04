package ui;

import javax.swing.*;
import java.awt.*;

public class MaFenetre extends JFrame {
    CardLayout cardLayout;
    Container container;

    public MaFenetre() {
        setTitle("Election");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        container = new JPanel(cardLayout);

        PanelPrincipal panel = new PanelPrincipal(this);
        PanelHystorique panel2 = new PanelHystorique(this);
        PanelInsert panelins = new PanelInsert(this);

        container.add(panel, "principale");
        container.add(panel2, "secondaire");
        container.add(panelins, "insert");

        add(container);
        setVisible(true);
    }

    // Methode pour changer la page
    public void changePanel(String name) {
        cardLayout.show(container, name);
    }

}
