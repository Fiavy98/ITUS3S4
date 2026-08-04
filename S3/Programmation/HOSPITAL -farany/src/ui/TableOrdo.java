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

        add(menu,BorderLayout.NORTH);
        add(scroll,BorderLayout.CENTER);

        LsOrd.addActionListener(e -> load());
        load();
    }

    private void load() {
        try {
            Vector<Vector<Object>> data =From_MedOrdFile.lsOrdonnanceInterneFait();
            Vector<String> colonne =From_MedOrdFile.ColoneOrdonnance();
            model.setDataVector(data, colonne);
        }catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(),
                    "Erreur SQL", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
}
