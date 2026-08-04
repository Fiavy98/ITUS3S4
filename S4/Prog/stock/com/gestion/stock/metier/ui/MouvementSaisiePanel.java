package com.gestion.stock.metier.ui;

import javax.swing.*;

import com.gestion.stock.config.DatabaseConfig;
import com.gestion.stock.core.ui.GenericFormPanel;
import com.gestion.stock.metier.metadata.MouvementMetadata;
import com.gestion.stock.metier.repository.EtatStockRepository;
import com.gestion.stock.metier.repository.MouvementRepository;

import java.awt.*;
import java.sql.Connection;

public class MouvementSaisiePanel extends JPanel {
    
    private MouvementRepository repo;

    private GenericFormPanel form;

    public MouvementSaisiePanel() {

        Connection conn = DatabaseConfig.getConnection();

        repo = new MouvementRepository(conn);

        setLayout(new BorderLayout());

        form = new GenericFormPanel<>(MouvementMetadata.build());

        JButton save = new JButton("Enregistrer");

        add(form, BorderLayout.CENTER);
        add(save, BorderLayout.SOUTH);

        initLogic();

        save.addActionListener(e -> saveMouvement());
    }

    // ================= LOGIQUE METIER UI =================
    private void initLogic() {

        JComboBox<String> typeCombo =
                (JComboBox<String>) form.getField("type");

        JComponent puField =
                form.getField("prixUnitaire");

        typeCombo.addActionListener(e -> {

            String type = (String) typeCombo.getSelectedItem();

            if ("SORTIE".equals(type)) {
                puField.setEnabled(false);
            } else {
                puField.setEnabled(true);
            }
        });
    }

    // ================= SAVE =================
    public void saveMouvement() {

        try {
            Object produit = form.getValue("produitId");
            Object type = form.getValue("type");
            Object quantite = form.getValue("quantite");

            Object pu = form.getValue("prixUnitaire");
            Object date = form.getValue("date");

            System.out.println("MOUVEMENT:");
            System.out.println("Produit = " + produit);
            System.out.println("Type = " + type);
            System.out.println("Quantité = " + quantite);
            System.out.println("PU = " + pu);
            System.out.println("Date = " + date);

            JOptionPane.showMessageDialog(this, "Mouvement enregistré (mode test)");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}