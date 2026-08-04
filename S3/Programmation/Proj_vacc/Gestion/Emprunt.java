package gestion;
import javax.swing.*; 
import javax.swing.table.DefaultTableModel; 
import java.awt.*; 
import java.util.Vector;

public class Emprunt extends JPanel {


    JTable table;
    DefaultTableModel model;

    public Emprunt(JTextField txtValeur,
                   JTextField txtJour,
                   JComboBox<String> P_Taux,
                   JTextField txtTaux,
                   JRadioButton Ammortissement,
                   JRadioButton Annuite) {

        setLayout(new BorderLayout());

        double valeur = Double.parseDouble(txtValeur.getText());
        int duree = Integer.parseInt(txtJour.getText());

        double taux = Double.parseDouble(txtTaux.getText()) / 100.0;

        String choix = P_Taux.getSelectedItem().toString();

        // ✅ Correction fréquence
        if (choix.equals("Mensuel")) {
            taux /= 12;
            duree *= 12;
        } 
        else if (choix.equals("Trimestriel")) {
            taux /= 4;
            duree *= 4;
        } 
        else if (choix.equals("Bimestriel")) {
            taux /= 6;
            duree *= 6;
        }

        model = new DefaultTableModel();
        table = new JTable(model);

        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        Vector<String> colonnes = Colone();
        Vector<Vector<Object>> data;

        if (Ammortissement.isSelected()) {
            data = ammortissement(valeur, duree, taux);
        } else {
            data = annuite(valeur, duree, taux);
        }

        model.setDataVector(data, colonnes);
    }

    // ===============================
    // AMORTISSEMENT CONSTANT
    // ===============================
    public static Vector<Vector<Object>> ammortissement(double valeur, int duree, double taux) {

        Vector<Vector<Object>> data = new Vector<>();

        double capitalRestant = valeur;
        double amort = round(valeur / duree);

        for (int i = 1; i <= duree; i++) {

            Vector<Object> row = new Vector<>();

            double interet = round(capitalRestant * taux);

            // Ajustement dernière ligne
            if (i == duree) {
                amort = capitalRestant;
            }

            double annuite = round(interet + amort);
            double valeurNette = round(capitalRestant - amort);

            row.add(i);
            row.add(capitalRestant);
            row.add(interet);
            row.add(amort);
            row.add(annuite);
            row.add(valeurNette);

            capitalRestant = valeurNette;

            data.add(row);
        }

        return data;
    }

    // ===============================
    // ANNUITE CONSTANTE
    // ===============================
    public static Vector<Vector<Object>> annuite(double valeur, int duree, double taux) {

        Vector<Vector<Object>> data = new Vector<>();

        double capitalRestant = valeur;
        double annuite;

        // ✅ Gestion taux = 0
        if (taux == 0) {
            annuite = round(valeur / duree);
        } else {
            annuite = round(valeur * taux / (1 - Math.pow(1 + taux, -duree)));
        }

        for (int i = 1; i <= duree; i++) {

            Vector<Object> row = new Vector<>();

            double interet = round(capitalRestant * taux);
            double amort = round(annuite - interet);

            // Ajustement dernière échéance
            if (i == duree) {
                amort = capitalRestant;
                annuite = round(interet + amort);
            }

            double valeurNette = round(capitalRestant - amort);

            row.add(i);
            row.add(capitalRestant);
            row.add(interet);
            row.add(amort);
            row.add(annuite);
            row.add(valeurNette);

            capitalRestant = valeurNette;

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

    public static Vector<String> Colone() {

        Vector<String> colNames = new Vector<>();

        colNames.add("Période");
        colNames.add("Capital restant");
        colNames.add("Intérêt");
        colNames.add("Amortissement");
        colNames.add("Annuité");
        colNames.add("Capital restant après");

        return colNames;
    }
}