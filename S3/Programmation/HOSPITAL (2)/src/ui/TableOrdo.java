package ui;

import db.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.Vector;


public class TableOrdo extends JPanel {
    JTable table;
    DefaultTableModel model;
    JPanel menu;
    JButton acc,btnDecon,LsOrd;

    JTable table2;
    DefaultTableModel model2;

    JTextField Idmed,qt,idOrd;
    JButton retourner;

    public TableOrdo(MaFenetre frame){
        setLayout(new BorderLayout(10, 10));

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

        LsOrd = new JButton("Liste Ordonance");
        LsOrd.addActionListener(e -> {
            frame.showPage("Ordo");
        });

        menu.add(acc);
        menu.add(btnDecon);
        menu.add(LsOrd);


        model = new DefaultTableModel();
        table = new JTable(model);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder("Liste des Ordonnance Fait"));
        add(scroll, BorderLayout.CENTER);



        model2 = new DefaultTableModel();
        table2 = new JTable(model2);

        JScrollPane scroll2 = new JScrollPane(table2);
        scroll2.setBorder(BorderFactory.createTitledBorder("Liste des Ordonnance Fille"));
        add(scroll2, BorderLayout.CENTER);


        add(menu,BorderLayout.NORTH);
        add(scroll,BorderLayout.WEST);
        add(scroll2,BorderLayout.EAST);

        LsOrd.addActionListener(e -> load());
        load();

      
        JPanel form = new JPanel();
        Idmed = new JTextField(15);
        qt = new JTextField(15);
        idOrd = new JTextField(15);
        retourner =new JButton("retourner");

        form.add(new JLabel("ID ORD"));
        form.add(idOrd);
        form.add(new JLabel("ID MED"));
        form.add(Idmed);
        form.add(new JLabel("Quantite"));
        form.add(qt);
        form.add(retourner);

        retourner.addActionListener(e -> {
            String idOrdonnance=idOrd.getText();
            String idMed = Idmed.getText();
            double qte = Double.parseDouble(qt.getText());
        
            String result = From_Stock.retourner(idOrdonnance, idMed, qte);
        
            if("OK".equals(result)){
                JOptionPane.showMessageDialog(null, "Retour effectué avec succès !");
            }
        });
        

        add(form,BorderLayout.SOUTH);  

    }

    private void load() {
        try {
            Vector<Vector<Object>> data =From_MedOrdFile.lsOrdonnanceInterneFait();
            Vector<String> colonne =From_MedOrdFile.ColoneOrdonnance();
            model.setDataVector(data, colonne);

            Vector<Vector<Object>> data2 =From_MedOrdFile.lsOrdonnanceFille();
            Vector<String> colonne2 =From_MedOrdFile.ColoneOrdonnanceFille();           
            model2.setDataVector(data2, colonne2);
        
        }catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(),
                    "Erreur SQL", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
}
