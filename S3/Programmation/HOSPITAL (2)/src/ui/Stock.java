package ui;
import acte.ActionStock;

import javax.swing.*;

import db.*;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Stock extends JPanel{
    JPanel menu;
    JButton acc,btnDecon,btnStock;

    private JTextField Date;
    private Vector<String[]> Medicament;
    private JList<String> lsMedicaments;
    private Map<String, JTextField> entre = new HashMap<>();

    private Vector<String[]> Magasin;
    private JComboBox<String> lsMagasin;

    private JButton btnValide,ajMvt;

    private String IDMVT;

    public Stock(MaFenetre frame){
        setLayout(new BorderLayout(10, 10));

        //======================= MENU ======================================
        menu = new JPanel(new FlowLayout(FlowLayout.CENTER));
        acc = new JButton("Accueil");
        acc.addActionListener(e -> {
            frame.showPage("accueil");
        });

        btnDecon = new JButton("Déconnecter");
        btnDecon.addActionListener(e -> {
            Session.destroy();
            frame.showPage("login");
        });

        btnStock = new JButton("Stock");
        btnStock.addActionListener(e -> {
            frame.showPage("stock");
        });

        menu.add(acc);
        menu.add(btnDecon);
        menu.add(btnStock);

        //======================== FORMULAIRE ===========================
        JPanel panelForm = new JPanel();
        panelForm.setLayout(new BoxLayout(panelForm, BoxLayout.Y_AXIS));
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelDt = new JPanel();
        Date = new JTextField(15);
        panelDt.add(new JLabel("Date : "));
        panelDt.add(Date);
        panelForm.add(panelDt);

        //Magasin
        JPanel rowMagasin = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rowMagasin.add(new JLabel("Magasin : "));

        Magasin = From_Magasin.lsMagasin();
        lsMagasin = new JComboBox<>();
        for (String[] mag : Magasin) lsMagasin.addItem(mag[1]);

        rowMagasin.add(lsMagasin);
        ajMvt = new JButton("ok");
        rowMagasin.add(ajMvt);
        panelForm.add(rowMagasin);

        //Medicaments
        JPanel rowMedic = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rowMedic.add(new JLabel("Médicaments :"));
        panelForm.add(rowMedic);

        //JList med
        Medicament = From_Medicament.lsMedicament();
        String[] nomsMed = Medicament.stream().map(m -> m[1]).toArray(String[]::new);

        lsMedicaments = new JList<>(nomsMed);
        lsMedicaments.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane scrollMed = new JScrollPane(lsMedicaments);
        scrollMed.setPreferredSize(new Dimension(280, 220));  

        panelForm.add(scrollMed);

        //Quantite
        JPanel panelEntree = new JPanel();
        panelEntree.setLayout(new BoxLayout(panelEntree, BoxLayout.Y_AXIS));
        panelEntree.setBorder(BorderFactory.createTitledBorder("Quantités"));

        panelForm.add(panelEntree);

        //act med
        lsMedicaments.addListSelectionListener(e -> {
            panelEntree.removeAll();
            entre.clear();
        
            for (String med : lsMedicaments.getSelectedValuesList()) {
                JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
                row.add(new JLabel(med + " → ajout : "));
                JTextField qteAjout = new JTextField(15);
                row.add(qteAjout);
            
                entre.put(med, qteAjout);
                panelEntree.add(row);
            }
        
            panelEntree.revalidate();
            panelEntree.repaint();
        });

        btnValide = new JButton("Valider");
        btnValide.setEnabled(false);
        ajMvt.addActionListener(e -> {
            try {
                // Ne crée l'ordonnance parent que si elle n'existe pas encore
                if (IDMVT == null) {
                    String dt = Date.getText();
    
                    String Mag = (String) lsMagasin.getSelectedItem();
                    String idMang = From_Magasin.getIdmagasin(Mag);
    
                    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate date = LocalDate.parse(dt, format);
                    java.sql.Date ins_Date = java.sql.Date.valueOf(date);
    
                    IDMVT =From_Stock.insMvtSock_enter(ins_Date, idMang);
                    btnValide.setEnabled(true);
                } else {
                    JOptionPane.showMessageDialog(null, "mvt déjà créée (ID: " + IDMVT + ").");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
    
          btnValide.addActionListener(e -> {
            if (IDMVT == null) {
                JOptionPane.showMessageDialog(null, "Crée le  parent d'abord (ok).");
                return;
            }
            try {
                new ActionStock(IDMVT,lsMedicaments,entre).actionPerformed(e);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
            });
    
        
        JPanel rowBtn = new JPanel();
        rowBtn.add(btnValide);
        panelForm.add(rowBtn);


        add(menu,BorderLayout.NORTH);
        add(panelForm,BorderLayout.CENTER);
    }
}
