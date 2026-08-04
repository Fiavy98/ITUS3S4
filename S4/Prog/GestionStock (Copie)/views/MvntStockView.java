package views;
import javax.swing.*;

import controllers.MouvementController;
import controllers.ProduitController;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;
import models.*;

public class MvntStockView extends JPanel {

    public JComboBox<Produit> cbProduit;
    public JComboBox<TypeMouvement> cbType;

    public JTextField txtQuantite;
    public JTextField txtPrixUnitaire;
    public JTextField txtDate;

    public JButton btnAjouter, btnReset;

    public JButton ajout, mvnt, etat;

    private MouvementController controller = new MouvementController();
    private ProduitController produitController = new ProduitController();

    public MvntStockView(MainFrame frame) {

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // ================= MENU TOP =================
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
        JPanel form = new JPanel(new GridLayout(6, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        form.add(new JLabel("Produit"));
        cbProduit = new JComboBox<>();
        loadProduits();
        form.add(cbProduit);

        cbProduit.addActionListener(e -> {  updatePrixAutomatique(); });

        form.add(new JLabel("Type Mouvement"));
        cbType = new JComboBox<>(TypeMouvement.values());
        form.add(cbType);

        cbType.addActionListener(e -> { updatePrixAutomatique();});

        form.add(new JLabel("Quantité"));
        txtQuantite = new JTextField();
        form.add(txtQuantite);

        form.add(new JLabel("Prix Unitaire"));
        txtPrixUnitaire = new JTextField();
        form.add(txtPrixUnitaire);

        form.add(new JLabel("Date (YYYY-MM-DD)"));
        txtDate = new JTextField();
        form.add(txtDate);


        btnAjouter = new JButton("Ajouter");
        btnAjouter.addActionListener(e -> { ajouterMouvement(); });

        form.add(btnAjouter);


        add(form, BorderLayout.CENTER);
    }

    private void updatePrixAutomatique(){
        try {

            Produit produit = (Produit) cbProduit.getSelectedItem();

            TypeMouvement type = (TypeMouvement) cbType.getSelectedItem();

            if(produit == null || type == null){
                return;
            }

            // ===== SI SORTIE =====
            if(type == TypeMouvement.SORTIE){

                txtPrixUnitaire.setText(
                    String.valueOf(
                        produit.getPrixVenteDefaut()
                    )
                );
            }

            // ===== SI ENTREE =====
            else {

                txtPrixUnitaire.setText("");
            }

            } 
        catch (Exception ex) { 
            ex.printStackTrace();
        }
    }

    private void loadProduits(){    
        cbProduit.removeAllItems();

        List<Produit> produits =
        produitController.getAll();

        for(Produit p : produits){
            cbProduit.addItem(p);
        }
        cbProduit.setSelectedIndex(-1);
    }
    

    
    private void ajouterMouvement() {

        try {

            Produit produit =(Produit) cbProduit.getSelectedItem();

            if(produit == null) {
                throw new Exception(
                        "Veuillez sélectionner un produit"
                );
            }

            Mouvement m = new Mouvement();

            m.setProduitId(produit.getId());
            m.setType((TypeMouvement) cbType.getSelectedItem());
            m.setQuantite(Integer.parseInt(txtQuantite.getText()));
            m.setPrixUnitaire(Double.parseDouble(txtPrixUnitaire.getText()));
            m.setDate(LocalDateTime.now());

            boolean success =controller.Ajouter(m);

            if(success){
                JOptionPane.showMessageDialog(
                    this,"Mouvement ajouté"
                );
                  resetForm();
            } else {
                JOptionPane.showMessageDialog(
                    this,"Erreur insertion"
                );
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage()
            );
        }
    }

    private void resetForm() {

        txtQuantite.setText("");
        txtPrixUnitaire.setText("");
        txtDate.setText("");

        cbProduit.setSelectedIndex(-1);
        cbType.setSelectedIndex(0);

        txtQuantite.requestFocus();
    }

}