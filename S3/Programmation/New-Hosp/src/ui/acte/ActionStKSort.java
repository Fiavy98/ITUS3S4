package acte;

import ui.*;
import db.*;

import javax.swing.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class ActionStKSort implements ActionListener {
    private String mvt;
    private JList<String> lsMedicaments;
    private Map<String, JTextField> sortiee;
    private Map<String, JComboBox<String>> unite = new HashMap<>();
    Map<String, Map<String, String>> uniteIdMap = new HashMap<>();

    public ActionStKSort(String mvt, JList<String> lsMedicaments, Map<String, JTextField> sortiee,Map<String, Map<String, String>> uniteIdMap,Map<String, JComboBox<String>> unite) {
        this.mvt = mvt;
        this.lsMedicaments = lsMedicaments;
        this.sortiee = sortiee;
        this.uniteIdMap=uniteIdMap;
        this.unite=unite;
    }

    public void execute() {
        try {
            for (String med : lsMedicaments.getSelectedValuesList()) {
                String idMedic = From_Medicament.getIdMedicament(med);

                String txt = sortiee.get(med).getText().trim();
                if (txt.isEmpty()) {
                    throw new NumberFormatException("Quantité vide pour : " + med);
                }

                JComboBox<String> cbUnites = unite.get(med);
                String uniteSelectionnee = (String) cbUnites.getSelectedItem();

                String idUnit = uniteIdMap.get(med).get(uniteSelectionnee);

                double sortie = Calcul.parseQuantite(txt);

                // Insérer mouvement fille
                From_Stock.insMvtStockFille_ETAT(mvt, idMedic, 0, sortie,idUnit);
            }
        
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null,
                    "Veuillez entrer un nombre valide.\n" + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Erreur : " + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        execute();
    }
}
