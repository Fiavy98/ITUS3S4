package view;
import javax.swing.*;
import java.awt.*;
import gestion.*;
import view.EmpruntPanel;
import view.InvestPanel;
public class MyFrame extends JFrame {
    CardLayout cardLayout;
    Container container;
    MyPanel panel;
    EmpruntPanel empruntPanel;
    InvestPanel invPanel;

    public MyFrame() {
        setTitle("Gestion de Vaccination");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        panel = new MyPanel(this);
        empruntPanel = new EmpruntPanel(this);
        invPanel = new InvestPanel(this);

        cardLayout = new CardLayout();
        container = new JPanel(cardLayout);

        container.add(panel, "accueil");
        container.add(empruntPanel,"emprunt");
        container.add(invPanel,"invets");
        
        add(container);
        setVisible(true);

    }

    public void showPage(String name) {
        cardLayout.show(container, name);
    }

     
}