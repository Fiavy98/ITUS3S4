package view;

import javax.swing.*;
import java.awt.*;
import gestion.*;

public class EmpruntPanel extends JPanel {
    JPanel form;
    JButton acc, valider;
    Emprunt emprunt;

    private JTextField txtValeur;
    private JTextField txtJour;
    private JComboBox<String> P_Taux;
    private JTextField txtTaux;

    private JRadioButton Ammortissement;
    private JRadioButton Annuite;

    public EmpruntPanel(MyFrame frame) {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Bouton accueil en haut
        acc = new JButton("Accueil");
        acc.addActionListener(e -> frame.showPage("accueil"));
        add(acc, BorderLayout.NORTH);

        // Formulaire à gauche
        form = new JPanel(new GridLayout(0, 2, 12, 12));

        form.add(new JLabel("Valeur (Capital) :"));
        txtValeur = new JTextField();
        form.add(txtValeur);

        form.add(new JLabel("Durée (en années) :"));
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

        JPanel panelRadio = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelRadio.setBorder(BorderFactory.createTitledBorder("Type de remboursement"));

        Ammortissement = new JRadioButton("Amortissement Constant");
        Annuite = new JRadioButton("Annuité Constante");

        ButtonGroup group = new ButtonGroup();
        group.add(Ammortissement);
        group.add(Annuite);

        panelRadio.add(Ammortissement);
        panelRadio.add(Annuite);

        form.add(panelRadio);

        valider = new JButton("Valider");
        form.add(valider);

        // Zone de résultats avec scroll
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
                emprunt = new Emprunt(
                        txtValeur,
                        txtJour,
                        P_Taux,
                        txtTaux,
                        Ammortissement,
                        Annuite
                );
                // Remplacer contenu du scroll par le nouveau tableau
                scrollResultat.setViewportView(emprunt);
                revalidate();
                repaint();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Erreur : Vérifiez vos champs !");
            }
        });
    }
}
