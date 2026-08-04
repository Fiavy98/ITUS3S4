import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class FormulaireAjoutAffichage extends JFrame {

    private JTextField txtNom;
    private List<String> listeNoms; // stockage mémoire

    public FormulaireAjoutAffichage() {
        setTitle("Formulaire d'ajout");
        setSize(400, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        listeNoms = new ArrayList<>();

        // Zone de saisie
        JLabel lblNom = new JLabel("Nom : ");
        txtNom = new JTextField(20);

        // Boutons
        JButton btnAjouter = new JButton("Ajouter");
        JButton btnAfficher = new JButton("Afficher");

        // Panel principal
        JPanel panel = new JPanel();
        panel.add(lblNom);
        panel.add(txtNom);
        panel.add(btnAjouter);  
        panel.add(btnAfficher);

        add(panel);

        // Action bouton Ajouter
        btnAjouter.addActionListener(e -> {
            String nom = txtNom.getText().trim();
            if (!nom.isEmpty()) {
                listeNoms.add(nom);
                txtNom.setText(""); // vider champ
                JOptionPane.showMessageDialog(this, "Nom ajouté dans la liste !");
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez entrer un nom.");
            }
        });

        // Action bouton Afficher
        btnAfficher.addActionListener(e -> ouvrirFenetreAffichage());
    }

    // Ouvre une nouvelle fenêtre pour afficher le tableau
    private void ouvrirFenetreAffichage() {
        JFrame fenetreAffichage = new JFrame("Liste des Noms");
        fenetreAffichage.setSize(400, 300);
        fenetreAffichage.setLocationRelativeTo(this);

        // Modèle du tableau
        DefaultTableModel model = new DefaultTableModel(new String[]{"Nom"}, 0);

        // Remplir le tableau avec les données de la liste
        for (String nom : listeNoms) {
            model.addRow(new Object[]{nom});
        }

        JTable table = new JTable(model);
        fenetreAffichage.add(new JScrollPane(table), BorderLayout.CENTER);

        fenetreAffichage.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormulaireAjoutAffichage().setVisible(true));
    }
}
