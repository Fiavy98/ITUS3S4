package src.views;

import javax.swing.*;

import src.Model.*;
import src.controller.*;
import java.awt.*;

public class Ajout extends JPanel{

    private JLabel titre;
    public JTextField coeff,contraintes;
    public JComboBox<String> nbVariable,operator;
    public JButton btnResoudre;

    public Ajout(MainFrame frame) {

        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        JPanel menu = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
 
        JButton btnAjout= new JButton("Ajout");
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

        titre = new JLabel("Page d'Ajout");

        titre.setFont(new Font("Arial", Font.BOLD, 24));
        titre.setHorizontalAlignment(SwingConstants.CENTER);

        topPanel.add(menu, BorderLayout.NORTH);
        topPanel.add(titre, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        AjtEquation();
    }

    public void AjtEquation(){
        JPanel form = new JPanel(new GridLayout(5, 2, 10, 10));

        form.add(new JLabel("Nombre de variable"));
        nbVariable = new JComboBox<>(VariableController.values());
        form.add(nbVariable);

    }


}
