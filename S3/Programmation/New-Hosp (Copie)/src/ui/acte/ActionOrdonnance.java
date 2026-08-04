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

public class ActionOrdonnance implements ActionListener {
    // description
    private String idORD;
    private JTextField txtDate, txtNbJr;
    private JList<String> lsMedicaments;
    private Map<String, JTextField> qteMap;
    private Map<String, JComboBox<String>> unite = new HashMap<>();
    Map<String, Map<String, String>> uniteIdMap = new HashMap<>();

    // client
    private JTextField adresse;
    private JComboBox<String> lsSociete;
    private Map<String, String> MapSos;
    
    // Zone result
    private JTextArea txtResultat;

    public ActionOrdonnance(String idORD, JTextField txtDate, JTextField txtNbJr,
                            JList<String> lsMedicaments, Map<String, JTextField> qteMap,
                            JTextField adresse,
                            JComboBox<String> lsSociete, JTextArea txtResultat,
                            Map<String, String> MapSos,Map<String, JComboBox<String>> unite,
                            Map<String, Map<String, String>> uniteIdMap ) {
        
        this.idORD=idORD;
        this.txtDate = txtDate;
        this.txtNbJr = txtNbJr;
        this.lsMedicaments = lsMedicaments;
        this.qteMap = qteMap;

        this.adresse = adresse;
        this.lsSociete = lsSociete;
        this.MapSos = MapSos;
        this.uniteIdMap=uniteIdMap;

        this.unite=unite;
        this.txtResultat = txtResultat;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String dt = txtDate.getText();
            int nbJr = Integer.parseInt(txtNbJr.getText());
            String addresse = adresse.getText();
            String societer = (String) lsSociete.getSelectedItem();
            String id_soc = MapSos.get(societer);

            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(dt, format);
            java.sql.Date ins_Date = java.sql.Date.valueOf(date);

            String idClient=From_client.getOrInsertClientId(addresse,id_soc);
            From_societe.insClientSos(idClient);

            // Recup Medicament whith id
            StringBuilder medicamentsText = new StringBuilder();
            for (String med : lsMedicaments.getSelectedValuesList()) {
                String idMedic = From_Medicament.getIdMedicament(med);
                int qte = Integer.parseInt(qteMap.get(med).getText());

                JComboBox<String> cbUnites = unite.get(med);
                String uniteSelectionnee = (String) cbUnites.getSelectedItem();


                String idUnit = uniteIdMap.get(med).get(uniteSelectionnee);


                Double PU=From_unite.getPUunite(idMedic,idUnit);

                Double Prix = From_unite.calculPrix(idMedic,idUnit,qte);

                // INSERT MED_ORDONNANCE_FILE
                From_MedOrdFile.insOrdFile(idMedic, idORD, Session.getUserId(),nbJr,qte,uniteSelectionnee,PU,Prix);
                medicamentsText.append("- ").append(med).append(" (").append(qte).append(") pour ").append(nbJr).append(" jours\n");
            
            }

            String resultat = "===== ORDONNANCE =====\n\n"
                    + "Date : " + dt + "\n"
                    + "Adresse : " + addresse + "\n"
                   // + "\nMédicaments :\n" + medicamentsText
                    + "\n\nDr " + Session.getUsername()
                    + "\n\n=========================";

            txtResultat.setText(resultat);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Veuillez entrer un nombre valide", "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
