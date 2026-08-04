package ui;

import db.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.Vector;


public class TableLivrer extends JPanel {
    JTable table;
    DefaultTableModel model;
    JPanel menu;
    JButton acc,btnDecon,lsLivrer;

    public TableLivrer(MaFenetre frame){
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

        lsLivrer = new JButton("Liste Livraison");
        lsLivrer.addActionListener(e -> {
            frame.showPage("livrer");
        });

        menu.add(acc);
        menu.add(btnDecon);
        menu.add(lsLivrer);


        model = new DefaultTableModel();
        table = new JTable(model);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder("Liste des Ordonnance Fait"));
        add(scroll, BorderLayout.CENTER);

        add(menu,BorderLayout.NORTH);
        add(scroll,BorderLayout.CENTER);

        lsLivrer.addActionListener(e -> load());
        load();
    }

    private void load() {
        try {
            Vector<Vector<Object>> data =From_Livraison.lsLivraison();
            Vector<String> colonne =From_Livraison.ColoneLivraison();
            model.setDataVector(data, colonne);
        }catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(),
                    "Erreur SQL", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
}
