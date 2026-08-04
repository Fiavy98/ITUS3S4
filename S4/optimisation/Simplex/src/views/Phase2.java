package src.views;

import javax.swing.*;
import java.awt.*;

public class Phase2 extends JPanel{
    
    private JLabel titre;

    public Phase2(MainFrame frame){
            setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        JPanel menu = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton btnAjout = new JButton("Ajout");
        JButton btnStandard = new JButton("Standard");
        JButton btnPhase1 = new JButton("Phase 1");
        JButton btnPhase2 = new JButton("Phase 2");
        JButton btnResultat = new JButton("Résultat");

        btnAjout.addActionListener(e -> frame.showPage("ajout"));
        btnStandard.addActionListener(e -> frame.showPage("standard"));
        btnPhase1.addActionListener(e -> frame.showPage("phase1"));
        btnPhase2.addActionListener(e -> frame.showPage("phase2"));
        btnResultat.addActionListener(e -> frame.showPage("resultat"));

        menu.add(btnAjout);
        menu.add(btnStandard);
        menu.add(btnPhase1);
        menu.add(btnPhase2);
        menu.add(btnResultat);

        titre = new JLabel("Phase 2");

        titre.setFont(new Font("Arial", Font.BOLD, 24));
        titre.setHorizontalAlignment(SwingConstants.CENTER);

        topPanel.add(menu, BorderLayout.NORTH);
        topPanel.add(titre, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
    }
}
