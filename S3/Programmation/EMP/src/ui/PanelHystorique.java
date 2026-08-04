package ui;
import javax.swing.*;

import db.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Vector;

public class PanelHystorique extends JPanel{
    JTable table;
    DefaultTableModel model;
    JTextField hst_date;
    JPanel menu;
    JPanel cherche;

    public PanelHystorique(MaFenetre frame){ 
        setLayout(new BorderLayout(15, 15));

         menu = new JPanel();

         JButton acc = new JButton("Accueil");
         acc.addActionListener(e -> frame.changePanel("principale"));
         menu.add(acc);
   
         JButton hist = new JButton("Historique");
         hist.addActionListener(e -> frame.changePanel("secondaire"));
         menu.add(hist);
         add(menu, BorderLayout.NORTH);
   
         JButton ins = new JButton("Inserer");
         ins.addActionListener(e -> frame.changePanel("insert"));
         menu.add(ins);
   
        model = new DefaultTableModel();
        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder("Hystorique des salaire"));

        hst_date = new JTextField();
        JButton aff = new JButton("Valider");
        cherche = new JPanel(new GridLayout(1, 3, 10, 10));
        cherche.setBorder(BorderFactory.createTitledBorder("Recherche par date"));
        cherche.add(new JLabel("Date (yyyy-MM-dd) : "));
        cherche.add(hst_date);
        cherche.add(aff);
        
        aff.addActionListener(e -> val());
        add(menu,BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(cherche, BorderLayout.SOUTH);

    }

    private void val(){
        try {
            String formDate = hst_date.getText().trim();
            if (formDate.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez saisir une date !");
                return;
            }
            
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(formDate, format);
            java.util.Date utilDate = java.sql.Date.valueOf(localDate);

            Vector<Vector<Object>> data = From_hystorique.hyst_sal(utilDate);
            Vector<String> colonne = From_hystorique.getCol();
            model.setDataVector(data, colonne);
       
        }catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Format de date invalide (yyyy-MM-dd)");
        }
    }
}   
