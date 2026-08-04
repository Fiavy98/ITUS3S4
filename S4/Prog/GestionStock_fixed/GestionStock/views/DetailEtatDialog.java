package views;

import dao.MouvementDAO;
import dao.ProduitDAO;
import models.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DetailEtatDialog extends JDialog {

    private ProduitDAO produitDAO = new ProduitDAO();
    private MouvementDAO mouvementDAO = new MouvementDAO();

    public DetailEtatDialog(int produitId) {

        setTitle("Détail Produit - ID: " + produitId);
        setSize(500, 450);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(0, 2, 10, 10));

        try {

            // =========================
            // PRODUIT
            // =========================
            Produit p = produitDAO.getById(produitId);

            // =========================
            // STOCK
            // =========================
            double stock = mouvementDAO.getStock(produitId, java.time.LocalDate.now());

            // =========================
            // VALEUR STOCK
            // =========================
            double valeur = mouvementDAO.getValeurStock(
                    produitId,
                    java.time.LocalDate.now(),
                    p.getMethodeValuation().name()
            );

            double prixMoyen = (stock > 0) ? (valeur / stock) : 0;

            // =========================
            // LABELS
            // =========================

            add(new JLabel("Produit ID"));
            add(new JLabel(String.valueOf(p.getId())));

            add(new JLabel("Nom produit"));
            add(new JLabel(p.getNom()));

            add(new JLabel("Unité"));
            add(new JLabel(p.getUnite()));

            add(new JLabel("Méthode"));
            add(new JLabel(p.getMethodeValuation().name()));

            add(new JLabel("Stock"));
            add(new JLabel(String.valueOf(stock)));

            add(new JLabel("Valeur stock"));
            add(new JLabel(String.valueOf(valeur)));

            add(new JLabel("Prix moyen"));
            add(new JLabel(String.format("%.2f", prixMoyen)));

            add(new JLabel("Prix vente défaut"));
            add(new JLabel(String.valueOf(p.getPrixVenteDefaut())));

            // =========================
            // DERNIER MOUVEMENT
            // =========================
            Mouvement last = mouvementDAO.getLastMovement(produitId);

            add(new JLabel("Dernier mouvement"));
            add(new JLabel(last != null ? last.getType().name() : "Aucun"));

            // =========================
            // RÉSUMÉ
            // =========================
            JTextArea txtDetail = new JTextArea();
            txtDetail.setEditable(false);
            txtDetail.setText(
                    "Résumé produit:\n\n" +
                    "Stock actuel = " + stock + "\n" +
                    "Valeur stock = " + valeur + "\n" +
                    "Prix moyen = " + prixMoyen + "\n" +
                    "Méthode = " + p.getMethodeValuation().name()
            );

            add(new JLabel("Résumé"));
            add(txtDetail);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this,
                    "Erreur chargement produit: " + e.getMessage()
            );
        }
    }
}