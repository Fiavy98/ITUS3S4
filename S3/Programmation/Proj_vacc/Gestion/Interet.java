package gestion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Interet extends JPanel {

    private JTextField txtValeur;
    private JTextField txtJour;
    private JComboBox<String> periode;
     private JComboBox<String> P_Taux;
    private JTextField txtTaux;
    private JPanel JResultat;
    private JRadioButton rbValeurActuelle;
    private JRadioButton rbValeurAcquise;


    private JLabel lblInteretSimple;
    private JLabel lblInteretCompose;

    private double interetSimple;
    private double interetCompose;

public Interet() {

    setLayout(new BorderLayout(15, 15));
    setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

    // =========================
    // PANEL CHAMPS
    // =========================
    JPanel panelChamps = new JPanel(new GridLayout(5, 2, 10, 10));

    panelChamps.add(new JLabel("Valeur (Capital) :"));
    txtValeur = new JTextField();
    panelChamps.add(txtValeur);

    panelChamps.add(new JLabel("Durée :"));
    txtJour = new JTextField();
    panelChamps.add(txtJour);

    panelChamps.add(new JLabel("Période :"));
    periode = new JComboBox<>(new String[]{"Jours", "Mois", "Années"});
    panelChamps.add(periode);

    panelChamps.add(new JLabel("Fréquence taux :"));
    P_Taux = new JComboBox<>(new String[]{
            "Annuel", "Mensuel", "Trimestriel", "Bimestriel"
    });
    panelChamps.add(P_Taux);

    panelChamps.add(new JLabel("Taux (%) :"));
    txtTaux = new JTextField();
    panelChamps.add(txtTaux);

    // =========================
    // PANEL RADIO
    // =========================
    JPanel panelRadio = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
    panelRadio.setBorder(BorderFactory.createTitledBorder("Type de valeur"));

    rbValeurActuelle = new JRadioButton("Valeur actuelle");
    rbValeurAcquise = new JRadioButton("Valeur acquise");

    ButtonGroup group = new ButtonGroup();
    group.add(rbValeurActuelle);
    group.add(rbValeurAcquise);

    panelRadio.add(rbValeurActuelle);
    panelRadio.add(rbValeurAcquise);

    // =========================
    // PANEL BAS (Bouton + Résultat)
    // PANEL BAS : bouton + résultat
        JPanel panelBas = new JPanel();
        panelBas.setLayout(new BorderLayout(10, 10));
        
        // Sous-panel pour le bouton
        JPanel panelBtn = new JPanel();
        JButton btnCalculer = new JButton("Valider");
        panelBtn.add(btnCalculer);
        panelBas.add(panelBtn, BorderLayout.NORTH);
        
        // Sous-panel pour le résultat
        JResultat = new JPanel(new GridLayout(2, 1, 5, 5));
        lblInteretSimple = new JLabel("Intérêt simple : ");
        lblInteretCompose = new JLabel("Intérêt composé : ");
        JResultat.add(lblInteretSimple);
        JResultat.add(lblInteretCompose);
        
        panelBas.add(JResultat, BorderLayout.CENTER);
        
        // Ajout du panelBas global dans le BorderLayout SOUTH du JFrame
        add(panelBas, BorderLayout.SOUTH);
        
        // Action du bouton
        btnCalculer.addActionListener(e -> calculerInteret());
        
    // =========================
    // AJOUT GLOBAL
    // =========================
    add(panelChamps, BorderLayout.NORTH);
    add(panelRadio, BorderLayout.CENTER);
    add(panelBas, BorderLayout.SOUTH);
}
    public void  calculerInteret(){
        try {

            String valeur=txtValeur.getText();
            double vo=Double.parseDouble(valeur);

            String choixPeriode = periode.getSelectedItem().toString();
            int duree = Integer.parseInt(txtJour.getText());

            if (choixPeriode.equals("Mois")) {
                duree *= 30;
            }
            else if (choixPeriode.equals("Années")) {
                duree *= 360;
            }

            String choixTaux = P_Taux.getSelectedItem().toString();
            double taux = Double.parseDouble(txtTaux.getText());
            taux = taux / 100.0;
            if (choixTaux.equals("Mensuel")) {
                taux /= 12;
            }
            else if (choixTaux.equals("Trimestriel")) {
                taux /= 4;
            }
            else if (choixTaux.equals("Bimestriel")) {
                taux /= 6;
            }

            if (rbValeurActuelle.isSelected()) {
                interetSimple=calculInteretSimpleActuelle(vo,taux,duree);
                interetCompose=calculInteretComposeActuelle(vo,taux,duree);
            } 
            else if (rbValeurAcquise.isSelected()) {
                interetSimple=calculInteretSimpleAcquis(vo,taux,duree);
                interetCompose=calculInteretComposeAcquis(vo,taux,duree);
            }

            resultat(interetSimple, interetCompose);

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public double calculInteretSimpleActuelle(double vo, double taux, int duree){
        return vo - (vo * taux * duree / 360.0);
    }

    public double calculInteretComposeActuelle(double vo, double taux, int duree){
        return vo * Math.pow(1 + taux, -duree);
    }

    public double calculInteretSimpleAcquis(double vo, double taux, int duree){
        return vo + (vo * taux * duree / 360.0);
    }

    public double calculInteretComposeAcquis(double vo, double taux, int duree){
        return vo * Math.pow(1 + taux, duree);
    }

    public void resultat(double simple, double compose) {
    lblInteretSimple.setText("Intérêt simple : " + String.format("%.2f", simple));
    lblInteretCompose.setText("Intérêt composé : " + String.format("%.2f", compose));

    // Rafraîchir le panel
    JResultat.revalidate();
    JResultat.repaint();
}
}
