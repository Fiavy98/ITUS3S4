package acte;

import ui.*;
import db.*;

import javax.swing.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Vector;

public class ActionLivrer implements ActionListener {
    // description
    private JList<String> lsMedicaments;
    private Map<String, JTextField> qteMap;

    // client
    private JTextField nom, telephone, adresse;
    private JComboBox<String> lsSociete;
    private Map<String, String> MapSos;

    //Livrer
    private JTextField LvrDate;
    private JComboBox<String>  Forn;

    public ActionLivrer(JList<String> lsMedicaments, Map<String, JTextField> qteMap,
                            JTextField nom, JTextField telephone, JTextField adresse,
                            JComboBox<String> lsSociete,
                            Map<String, String> MapSos,JTextField LvrDate,JComboBox<String> Forn) {
                            
        this.lsMedicaments = lsMedicaments;
        this.qteMap = qteMap;

        this.nom = nom;
        this.telephone = telephone;
        this.adresse = adresse;
        this.lsSociete = lsSociete;
        this.MapSos = MapSos;

        this.LvrDate=LvrDate;
        this.Forn=Forn;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {

            //CLI
            String name = nom.getText();
            String addresse = adresse.getText();
            String tel = telephone.getText();
            String societer = (String) lsSociete.getSelectedItem();
            String id_soc = MapSos.get(societer);


            String invdt = LvrDate.getText();
            DateTimeFormatter formatInv = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dtt = LocalDate.parse(invdt, formatInv);
            java.sql.Date ins_dt = java.sql.Date.valueOf(dtt);

            //Forniseur
            String Forniseur = (String) Forn.getSelectedItem();
            String idForn = From_Livraison.geIdForn(Forniseur);
  
            //INSERT LIVRAISONINTERNE
           String idLivr = From_Livraison.insLivrInt(idForn,ins_dt);

            // Recup Medicament whith id
            StringBuilder medicamentsText = new StringBuilder();
            for (String med : lsMedicaments.getSelectedValuesList()) {
                String idMedic = From_Medicament.getIdMedicament(med);
                int qte = Integer.parseInt(qteMap.get(med).getText());

                //INSERT LIVRAISONINTERNEFILLE
                From_Livraison.insLivrIntFll(idLivr,idMedic,qte);

            }

            JOptionPane.showMessageDialog(null, "Livreison enregistrée !");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Veuillez entrer un nombre valide", "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
