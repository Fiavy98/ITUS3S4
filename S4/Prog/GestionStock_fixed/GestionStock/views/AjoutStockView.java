package views;
import javax.swing.*;

import controllers.ProduitController;
import models.*;

import java.awt.*;

public class AjoutStockView extends JPanel {

    public JTextField txtNom, txtPu, txtPrixDeVente;
    JComboBox<MethodeValuation> methode;
    public JComboBox<String> unite;

    public JButton btnAjouter;
    
    public JButton ajout;
    public JButton mvnt;
    public JButton etat;

    private ProduitController controller = new ProduitController();

    public AjoutStockView(MainFrame frame) {

        setLayout(new BorderLayout());

        // ================= MENU =================
        JPanel menu = new JPanel();

        ajout = new JButton("Ajout");
        mvnt = new JButton("Mouvement");
        etat = new JButton("Etat");

        ajout.addActionListener(e -> frame.showPage("ajout"));
        mvnt.addActionListener(e -> frame.showPage("mouvement"));
        etat.addActionListener(e -> frame.showPage("etat"));

        menu.add(ajout);
        menu.add(mvnt);
        menu.add(etat);

        add(menu, BorderLayout.NORTH);

        // ================= FORM =================
        JPanel form = new JPanel(new GridLayout(5, 2, 10, 10));

        form.add(new JLabel("Nom"));
        txtNom = new JTextField();
        form.add(txtNom);

        form.add(new JLabel("Méthode de valuation"));
        methode = new JComboBox<>(MethodeValuation.values());
        form.add(methode);

        form.add(new JLabel("Prix de vente (Ar)"));
        txtPu = new JTextField();
        form.add(txtPu);

        form.add(new JLabel("Unité"));
        unite = new JComboBox<>(new String[]{"pièce", "kg", "litre"});
        form.add(unite);

        btnAjouter = new JButton("Ajouter");
        
        btnAjouter.addActionListener(e -> {
            try {
                Produit p= new Produit();

                p.setNom(txtNom.getText());
                p.setMethodeValuation((MethodeValuation) methode.getSelectedItem());
                p.setPrixVenteDefaut(Double.parseDouble(txtPu.getText()));
                p.setUnite((String) unite.getSelectedItem());

                controller.ajouterProduit(p);
                JOptionPane.showMessageDialog(this, "Produit ajouté !");
       
                txtNom.setText("");
                txtPu.setText("");

                methode.setSelectedIndex(0);
                unite.setSelectedIndex(0);
                
                txtNom.requestFocus();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage());
            }
        });

        form.add(btnAjouter);
        add(form, BorderLayout.CENTER);
    }
}