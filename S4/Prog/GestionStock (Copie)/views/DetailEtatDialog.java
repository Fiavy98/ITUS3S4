package views;

import javax.swing.*;
import java.awt.*;

public class DetailEtatDialog extends JDialog {

    public DetailEtatDialog(int produitId) {

        setTitle("Détail Produit - ID: " + produitId);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(0, 2, 10, 10));

        // ===== LABELS (valeurs seront remplies depuis DAO) =====
        add(new JLabel("Produit ID"));
        JLabel lbId = new JLabel();
        add(lbId);

        add(new JLabel("Nom produit"));
        JLabel lbNom = new JLabel();
        add(lbNom);

        add(new JLabel("Unité"));
        JLabel lbUnite = new JLabel();
        add(lbUnite);

        add(new JLabel("Méthode"));
        JLabel lbMethode = new JLabel();
        add(lbMethode);

        add(new JLabel("Stock"));
        JLabel lbStock = new JLabel();
        add(lbStock);

        add(new JLabel("Valeur stock"));
        JLabel lbValeur = new JLabel();
        add(lbValeur);

        add(new JLabel("Prix moyen"));
        JLabel lbPrixMoyen = new JLabel();
        add(lbPrixMoyen);

        add(new JLabel("Dernier achat"));
        JLabel lbLastAchat = new JLabel();
        add(lbLastAchat);

        add(new JLabel("Prix vente défaut"));
        JLabel lbPrixVente = new JLabel();
        add(lbPrixVente);

        add(new JLabel("Dernier mouvement"));
        JLabel lbLastMvnt = new JLabel();
        add(lbLastMvnt);

        add(new JLabel("Entrées totales"));
        JLabel lbEntree = new JLabel();
        add(lbEntree);

        add(new JLabel("Sorties totales"));
        JLabel lbSortie = new JLabel();
        add(lbSortie);

        add(new JLabel("Résumé"));
        JTextArea txtDetail = new JTextArea();
        txtDetail.setEditable(false);
        add(txtDetail);

        // ===== CHARGEMENT DONNÉES =====

    }
}