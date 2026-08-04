package views;
import javax.swing.*;

import controllers.MouvementController;
import controllers.ProduitController;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import models.*;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class MvntStockView extends JPanel {

    public JComboBox<Produit> cbProduit;
    public JComboBox<TypeMouvement> cbType;

    private JSpinner dateSpinner;
    
    public JTextField txtQuantite;
    public JTextField txtPrixUnitaire;

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

        form.add(new JLabel("Date"));
        SpinnerDateModel model = new SpinnerDateModel();
        dateSpinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(editor);
        form.add(dateSpinner);

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

            if(type == TypeMouvement.SORTIE){

                txtPrixUnitaire.setEnabled(false);
                txtPrixUnitaire.setText("Calculé automatiquement");

            } else {

                txtPrixUnitaire.setEnabled(true);
                txtPrixUnitaire.setText("");
            }

        } catch (Exception ex) {
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

        Produit produit = (Produit) cbProduit.getSelectedItem();

        if(produit == null) {
            throw new Exception("Veuillez sélectionner un produit");
        }

        TypeMouvement type = (TypeMouvement) cbType.getSelectedItem();

        int qte = Integer.parseInt(txtQuantite.getText());

        if(qte <= 0){
            throw new Exception("Quantité invalide");
        }

        Mouvement m = new Mouvement();
        m.setProduitId(produit.getId());
        m.setType(type);
        m.setQuantite(qte);

        Date date = (Date) dateSpinner.getValue();
        m.setDate(date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime());

        if(type == TypeMouvement.ENTREE){

            if(txtPrixUnitaire.getText().isEmpty()){
                throw new Exception("Prix unitaire requis pour une entrée");
            }

            m.setPrixUnitaire(
                Double.parseDouble(txtPrixUnitaire.getText())
            );

        } else {
            m.setPrixUnitaire(0);
        }

        boolean success = controller.Ajouter(m);

        if(success){
            JOptionPane.showMessageDialog(this, "Mouvement ajouté");
            resetForm();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur insertion");
        }

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage());
    }
}
    private void resetForm() {

        txtQuantite.setText("");
        txtPrixUnitaire.setText("");

        cbProduit.setSelectedIndex(-1);
        cbType.setSelectedIndex(0);

        txtQuantite.requestFocus();
    }

}