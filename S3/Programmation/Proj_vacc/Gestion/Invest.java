package gestion;
import java.util.Vector;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
public class Invest extends JPanel{

    JTable table;
    DefaultTableModel model;
    
    public Invest(JTextField investissement,JTextField txtValeur,JTextField txtJour,JComboBox<String> P_Taux,JTextField txtTaux){
              setLayout(new BorderLayout());

        double valeur = Double.parseDouble(txtValeur.getText());
        double Vo = Double.parseDouble(investissement.getText());

        int N = Integer.parseInt(txtJour.getText());

        double taux = Double.parseDouble(txtTaux.getText()) / 100.0;

        String choix = P_Taux.getSelectedItem().toString();

        // ✅ Correction fréquence
        if (choix.equals("Mensuel")) {
            taux /= 12;
            N *= 12;
        } 
        else if (choix.equals("Trimestriel")) {
            taux /= 4;
            N *= 4;
        } 
        else if (choix.equals("Bimestriel")) {
            taux /= 6;
            N *= 6;
        }

        model = new DefaultTableModel();
        table = new JTable(model);

        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        Vector<String> colonnes = Colone();
        Vector<Vector<Object>> data;

       data = Calcul(Vo, valeur, N, taux);

        model.setDataVector(data, colonnes);

    }

    private double vo_taloha=0.0;
    private double total=0.0;
    public Vector<Vector<Object>> Calcul(double vo,double valeur, int N, double taux) {
        Vector<Vector<Object>> data = new Vector<>();
        double annuite;

        // calcul de l'annuité (versement périodique)
        if (taux == 0) {
            annuite = round(valeur / N);
        } else {
            annuite = round(valeur * taux / (1 - Math.pow(1 + taux, -N)));
        }

        double totalFluxAct = 0.0;

        // ajuster la valeur initiale si l'investissement change
        if (vo != vo_taloha) {
            // on considère l'investissement comme un flux négatif la première année
            valeur += vo;               // vo est négatif de fait, mais on l'ajoute pour le calcul
            vo_taloha = vo;
        }

        for (int an = 1; an <= N; an++) {
            double flux = annuite;
            if (an == 1 && vo != 0) {
                flux += -vo; // incorporer le versement initial négatif
            }

            double fluxActualise = round(flux * Math.pow(1 + taux, -an));
            totalFluxAct += fluxActualise;

            Vector<Object> row = new Vector<>();
            row.add(an);
            row.add(flux);
            row.add(fluxActualise);
            row.add(totalFluxAct);

            data.add(row);
        }

        return data;
    }

    // ===============================
    // ARRONDI BANCAIRE
    // ===============================
    public static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    public double sommeFluxActualiser(DefaultTableModel model) {

    double somme = 0.0;

    for (int i = 0; i < model.getRowCount(); i++) {

        Object value = model.getValueAt(i, 2); // colonne 2

        if (value != null) {
            somme += Double.parseDouble(value.toString());
        }
    }

    return somme;
}

    public static Vector<String> Colone() {

        Vector<String> colNames = new Vector<>();

        colNames.add("Anne");
        colNames.add("flux");
        colNames.add("flux actualisé");
        colNames.add("Somme FNA");

        return colNames;
    }
}
