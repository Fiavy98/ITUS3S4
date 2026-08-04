package view;
import javax.swing.*;
import java.awt.*;
import gestion.*;
import view.MyFrame;

public class MyPanel extends JPanel {
    JButton acc,inv;
    JPanel menu;
    public MyPanel(MyFrame frame) {
        setLayout(new BorderLayout(15, 15)); // marges plus larges
        menu = new JPanel(new BorderLayout(15,15));

        acc = new JButton("Emprunt");
        acc.addActionListener(e -> {
            frame.showPage("emprunt");
        });
         menu.add(acc, BorderLayout.EAST);
        
        inv = new JButton("Investissement");
        inv.addActionListener(e -> frame.showPage("invets"));
        menu.add(inv, BorderLayout.WEST);

        add(menu,BorderLayout.NORTH);

        // Titre en haut
        JLabel label = new JLabel("Gestion");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20)); // police plus grande
        add(label, BorderLayout.CENTER);

        // Panel central : ton panel Interet
        Interet interetPanel = new Interet();
        interetPanel.setBorder(BorderFactory.createTitledBorder("Calcul d'intérêt"));
        add(interetPanel, BorderLayout.SOUTH);


    }
}