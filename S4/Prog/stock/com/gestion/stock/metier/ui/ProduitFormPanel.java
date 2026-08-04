package com.gestion.stock.metier.ui;

import javax.swing.*;

import com.gestion.stock.config.DatabaseConfig;
import com.gestion.stock.core.ui.GenericFormPanel;
import com.gestion.stock.metier.entity.Produit;
import com.gestion.stock.metier.metadata.ProduitMetadata;
import com.gestion.stock.metier.repository.ProduitRepository;

import java.awt.*;
import java.sql.Connection;

public class ProduitFormPanel extends JPanel{

    private ProduitRepository repo;
    private GenericFormPanel form;

    public ProduitFormPanel(){

        Connection conn = DatabaseConfig.getConnection();
        repo = new ProduitRepository(conn);

        setLayout(new BorderLayout());

        form = new GenericFormPanel<>(ProduitMetadata.build());

        JButton save = new JButton("Enregistrer");


        add(form, BorderLayout.CENTER);
        add(save, BorderLayout.SOUTH);

        save.addActionListener(e -> saveProduit());
    }

    public void saveProduit() {

        try {

            // 1. récupérer les valeurs UI
            String nom = (String) form.getValue("nom");
            String unite = (String) form.getValue("unite");
            String methode = (String) form.getValue("methodeValuation");

            // 2. créer entity
            Produit p = new Produit();
            p.setNom(nom);
            p.setUnite(unite);
            p.setMethodeValuation(methode);

            // 3. sauvegarder en base
            repo.save(p);

            JOptionPane.showMessageDialog(
                    this,
                    "Produit enregistré avec succès"
            );

        } catch (Exception e) {
            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Erreur lors de l'enregistrement"
            );
        }
    }


}
