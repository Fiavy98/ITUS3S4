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


public class ActionStock implements ActionListener {
    // description
    private String mvt;
    private JList<String> lsMedicaments;
    private Map<String, JTextField> entre;
    private Map<String, JComboBox<String>> unite = new HashMap<>();
    private Map<String, Map<String, String>> uniteIdMap = new HashMap<>();

    public ActionStock(String mvt,JList<String> lsMedicaments, Map<String,JTextField> entre,Map<String, Map<String, String>> uniteIdMap,Map<String, JComboBox<String>> unite) {
        this.mvt=mvt;
        this.lsMedicaments = lsMedicaments;
        this.entre=entre;
        this.uniteIdMap=uniteIdMap;
        this.unite=unite;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            // Recup Medicament whith id
            StringBuilder medicamentsText = new StringBuilder();
            for (String med : lsMedicaments.getSelectedValuesList()) {
                String idMedic = From_Medicament.getIdMedicament(med);
                double entree = Double.parseDouble(entre.get(med).getText());

                JComboBox<String> cbUnites = unite.get(med);
                String uniteSelectionnee = (String) cbUnites.getSelectedItem();

                String idUnit = uniteIdMap.get(med).get(uniteSelectionnee);

                //INS MVNT STOCKFILE/ETATSTOCK
               From_Stock.insMvtStockFille_ETAT(mvt,idMedic,entree,0,idUnit);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Veuillez entrer un nombre valide", "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
