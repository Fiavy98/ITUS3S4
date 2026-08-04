package ui;

import javax.swing.*;
import java.awt.*;
import dao.PrixReparationDAO;

public class DetailRoute extends JPanel {

    public DetailRoute(int idRoute) {

        setLayout(new BorderLayout(10, 10));

        JLabel lblTitre = new JLabel("Détail de la route", SwingConstants.CENTER);
        lblTitre.setFont(new Font("Arial", Font.BOLD, 20));
        add(lblTitre, BorderLayout.NORTH);

        double total = PrixReparationDAO.getTotalByRoute(idRoute);

        JLabel lblTotal = new JLabel(
            "Total des réparations : " + total + " Ar",
            SwingConstants.CENTER
        );
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));

        

        add(lblTotal, BorderLayout.CENTER);

                // ---------------- BOUTON RETOUR ----------------
        JButton btnRetour = new JButton("Retour");
        btnRetour.addActionListener(e -> {
            Container parent = getParent();
            parent.removeAll();

            // Crée un nouveau panel Demarrer
            Demarrer demarrer = new Demarrer();
            demarrer.updateDetails(idRoute); // facultatif, si tu veux garder la route affichée
            parent.add(demarrer, BorderLayout.CENTER);

            parent.revalidate();
            parent.repaint();
        });

        JPanel panelBas = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBas.add(btnRetour);
        add(panelBas, BorderLayout.SOUTH);
        
    }
}
