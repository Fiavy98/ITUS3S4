package view;
import javax.swing.*;

import gestion.Invest;

import java.awt.*;

public class InvestPanel extends JPanel{
    JButton acc,valider;
    JPanel form;
    Invest invest;
    private JTextField investissement;
    private JTextField txtValeur;
    private JTextField txtJour;
    private JComboBox<String> P_Taux;
    private JTextField txtTaux;

    public InvestPanel(MyFrame frame){
        setLayout(new BorderLayout(15, 15));

        // Bouton accueil en haut
        acc = new JButton("Accueil");
        acc.addActionListener(e -> frame.showPage("accueil"));
        add(acc, BorderLayout.NORTH);

        // Formulaire à gauche
        form = new JPanel(new GridLayout(0, 2, 12, 12));

        form.add(new JLabel("Investissement:"));
        investissement = new JTextField();
        form.add(investissement);

        form.add(new JLabel("Valeur (Capital) :"));
        txtValeur = new JTextField();
        form.add(txtValeur);

        form.add(new JLabel("N :"));
        txtJour = new JTextField();
        form.add(txtJour);

        form.add(new JLabel("Fréquence taux :"));
        P_Taux = new JComboBox<>(new String[]{
                "Annuel", "Mensuel", "Trimestriel", "Bimestriel", "Semestriel"
        });
        form.add(P_Taux);

        form.add(new JLabel("Taux (%) :"));
        txtTaux = new JTextField();
        form.add(txtTaux);


        valider = new JButton("Valider");
        form.add(valider);

                JPanel resultatPanel = new JPanel(new BorderLayout());
        JScrollPane scrollResultat = new JScrollPane();
        resultatPanel.add(scrollResultat, BorderLayout.CENTER);

        // Split pane pour séparer formulaire et résultats
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, form, resultatPanel);
        splitPane.setResizeWeight(0.3); // 30% pour le formulaire, 70% pour résultats
        add(splitPane, BorderLayout.CENTER);

        // Action bouton
        valider.addActionListener(e -> {
            try {

                invest = new Invest(
                       investissement,
                        txtValeur,
                        txtJour,
                        P_Taux,
                        txtTaux
                );
                scrollResultat.setViewportView(invest);
                revalidate();
                repaint();
            } catch (Exception ex) {
            }
        });


        add(form,BorderLayout.CENTER);
    }
}
