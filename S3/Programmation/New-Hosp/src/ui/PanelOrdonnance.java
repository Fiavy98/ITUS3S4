package ui;

import acte.*;
import db.*;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class PanelOrdonnance extends JPanel {

    // Description
    private String IDORD;
    private String IDMVT;

    private JTextField txtDate, txtNbJr;
    private Vector<String[]> Medicament;
    private JList<String> lsMedicaments;
    private Map<String, JTextField> sortie = new HashMap<>();
    private Map<String, JComboBox<String>> unite = new HashMap<>();
    private Map<String, Map<String, String>> uniteIdMap = new HashMap<>();
    private JButton btnOrd;

    // Client
    private JTextField adresse;
    private Vector<String[]> Societe;
    private Map<String, String> MapSos = new HashMap<>();
    private JComboBox<String> lsSociete;

    //Stock
    private JTextField stkDate;
    private Vector<String[]> Magasin;
    private JComboBox<String> lsMagasin;

    // Résultat
    private JTextArea txtResultat;

    public PanelOrdonnance() {
        setLayout(new BorderLayout(10, 10));

        txtResultat = new JTextArea();

        //============================Description=================================
        JPanel panelDescription = new JPanel();
        panelDescription.setLayout(new BoxLayout(panelDescription, BoxLayout.Y_AXIS));
        panelDescription.setBorder(BorderFactory.createTitledBorder("Description"));

        //----------------ORDONNANCE----------------
        txtDate = new JTextField(15);
        txtNbJr = new JTextField(15);


        panelDescription.add(labeledPanel("Date:", txtDate));
        panelDescription.add(labeledPanel("Nombre de jours:", txtNbJr));

        //------------------ ORDONANCEFILLE ---------------
        //Medicaments
        Medicament = From_Medicament.lsMedicament();
        String[] nomsMed = Medicament.stream().map(m -> m[1]).toArray(String[]::new);
        lsMedicaments = new JList<>(nomsMed);
        lsMedicaments.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollMed = new JScrollPane(lsMedicaments);
        scrollMed.setPreferredSize(new Dimension(200, 100));

        panelDescription.add(new JLabel("Médicaments:"));
        panelDescription.add(scrollMed);

        //Quantités
        JPanel panelQuantites = new JPanel();
        panelQuantites.setLayout(new BoxLayout(panelQuantites, BoxLayout.Y_AXIS));
        panelDescription.add(panelQuantites);

        //=========================MEDICAMENTS==================================
        lsMedicaments.addListSelectionListener(e -> {
            panelQuantites.removeAll();
            sortie.clear();    
            unite.clear();
            uniteIdMap.clear(); 
        
            for (String med : lsMedicaments.getSelectedValuesList()) {
                JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
                panelQuantites.add(row);
        
                JTextField qte = new JTextField(5);
                row.add(new JLabel(med + " Quantite:"));
                row.add(qte);
        
                String idMed = From_Medicament.getIdMedicament(med);
        
                Vector<String[]> unitesVec = From_unite.lsUniteParMedicament(idMed);
        
                String[] nomUnites = new String[unitesVec.size()];
                Map<String, String> idMap = new HashMap<>();
        
                for (int i = 0; i < unitesVec.size(); i++) {
                    String[] u = unitesVec.get(i);
                    String nomAffichage = u[1] + " (x" + u[2] + ")"; 
                    nomUnites[i] = nomAffichage;
        
                    idMap.put(nomAffichage, u[4]);  
                }
        
                JComboBox<String> cbUnites = new JComboBox<>(nomUnites);
                row.add(cbUnites);
        
                unite.put(med, cbUnites);
                uniteIdMap.put(med, idMap);
        
                sortie.put(med, qte);
            }
        
            panelQuantites.revalidate();
            panelQuantites.repaint();
        });
        
        
        //=========================Client=============================
        JPanel panelClient = new JPanel();
        panelClient.setLayout(new BoxLayout(panelClient, BoxLayout.Y_AXIS));
        panelClient.setBorder(BorderFactory.createTitledBorder("Client"));

        adresse = new JTextField(15);

        Societe = From_societe.lsSociete();
        lsSociete = new JComboBox<>();
        for (String[] sos : Societe) {
            String id = sos[0];
            String nomSoc = sos[1];
            lsSociete.addItem(nomSoc);
            MapSos.put(nomSoc, id);
        }

        btnOrd = new JButton("valider Ordonnance");
        JPanel btnPanel = new JPanel();
        btnPanel.add(btnOrd);

        panelClient.add(labeledPanel("Société:", lsSociete));
        panelClient.add(labeledPanel("Adresse:", adresse));
        panelClient.add(btnOrd);


        //========================INS ORDONNANCE============================
      btnOrd.addActionListener(e -> {
        try {
            if (IDORD == null) {
                String dt = txtDate.getText();
                int nbJr = Integer.parseInt(txtNbJr.getText());

                String societer = (String) lsSociete.getSelectedItem();
                String id_soc = MapSos.get(societer);

                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date = LocalDate.parse(dt, format);
                java.sql.Date ins_Date = java.sql.Date.valueOf(date);

                //Ins ORD
                IDORD = From_MedOrdon.insOrd(
                    ins_Date, nbJr, From_Medecin.getIdMedecin(Session.getUserId()), id_soc
                );
            }
    
            if(IDORD!=null){
                new ActionOrdonnance(
                    IDORD, txtDate, txtNbJr,lsMedicaments, sortie,
                    adresse, lsSociete, txtResultat, MapSos,unite,uniteIdMap
                ).actionPerformed(e);
            } 
    
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
        });


        JPanel panelTop = new JPanel();
        panelTop.setLayout(new GridLayout(1, 2, 10, 0));
        panelTop.add(panelDescription);
        panelTop.add(panelClient);
        add(panelTop, BorderLayout.NORTH);

        //Result
        txtResultat.setEditable(false);
        txtResultat.setFont(new Font("Serif", Font.PLAIN, 16));
        txtResultat.setBorder(BorderFactory.createTitledBorder("Ordonnance"));
        add(new JScrollPane(txtResultat), BorderLayout.CENTER);


        //========================= STOCKAGE =================================
        JPanel panelBas = new JPanel();
        panelBas.setLayout(new GridLayout(1, 2, 10, 0)); 
            
        JPanel panelStock = new JPanel();
        panelStock.setLayout(new GridLayout(3, 2, 5, 5)); 
        panelStock.setBorder(BorderFactory.createTitledBorder("Pharmacie"));
                
        //Dt
        stkDate = new JTextField(15);
        panelStock.add(new JLabel("Date :"));
        panelStock.add(stkDate);
        
        //Magasin
        Magasin = From_Magasin.lsMagasin();
        lsMagasin = new JComboBox<>();
        for (String[] mag : Magasin) {
            lsMagasin.addItem(mag[1]);
        }
        panelStock.add(new JLabel("Magasin :"));
        panelStock.add(lsMagasin);
        
        // ACTION SORTIE STOCK
        JButton btnSTK = new JButton("Valider");
        btnSTK.addActionListener (e -> {
            String dt = stkDate.getText();
            LocalDate date = LocalDate.parse(dt, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            java.sql.Date ins_Date = java.sql.Date.valueOf(date);
        
            String Mag = (String) lsMagasin.getSelectedItem();
            String idMang = From_Magasin.getIdmagasin(Mag);
   
            IDMVT = From_Stock.insMvtSock_sortie(ins_Date, idMang);
        
            ActionStKSort act = new ActionStKSort(IDMVT, lsMedicaments, sortie,uniteIdMap,unite);
            act.execute(); 
        });
        


        panelStock.add(new JLabel()); 
        panelStock.add(btnSTK);
        
        panelBas.add(panelStock);
        add(panelBas, BorderLayout.SOUTH);

        
    }

    // label + composant
    private JPanel labeledPanel(String label, JComponent comp) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.add(new JLabel(label));
        panel.add(comp);
        return panel;
    }

    public void resetPage() {
        IDORD = null;
        IDMVT = null;
    
        txtDate.setText("");
        txtNbJr.setText("");
    
        lsMedicaments.clearSelection();
    
        sortie.clear();
        unite.clear();
        uniteIdMap.clear();

        adresse.setText("");
    
        if (lsSociete.getItemCount() > 0) {
            lsSociete.setSelectedIndex(0);  
        }
    
        stkDate.setText("");
    
        if (lsMagasin.getItemCount() > 0) {
            lsMagasin.setSelectedIndex(0);
        }
    
        txtResultat.setText("");

        revalidate();
        repaint();
    }
    
}
