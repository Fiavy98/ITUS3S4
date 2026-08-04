package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

import dao.*;
import model.*;

public class Demarrer extends JPanel {

    private JPanel panelButtons;
    private JPanel panelTrous;
    private JPanel panelReparations;

    private JButton btnNomRoute;
    private int idRouteCourant = -1;

    public Demarrer() {
        setLayout(new BorderLayout(10, 10));

        /* ================= TITRE / ROUTE ================= */
        btnNomRoute = new JButton("Route");
        btnNomRoute.setFont(new Font("Arial", Font.BOLD, 18));
        btnNomRoute.setBorderPainted(false);
        btnNomRoute.setContentAreaFilled(false);
        btnNomRoute.setFocusPainted(false);
        btnNomRoute.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNomRoute.addActionListener(e -> afficherDetailRoute());

        add(btnNomRoute, BorderLayout.NORTH);

        /* ================= BOUTONS GAUCHE ================= */
        panelButtons = new JPanel(new GridLayout(4, 1, 5, 5));
        panelButtons.setPreferredSize(new Dimension(180, 200));

        JButton btnAjouterTrou = new JButton("Ajouter un trou");
        JButton btnAjouterReparation = new JButton("Ajouter une réparation");
        JButton btnVoirTrou = new JButton("Voir les trous");
        JButton btnVoirReparation = new JButton("Voir les réparations");

        btnAjouterTrou.addActionListener(e -> ajouterTrou());
        btnAjouterReparation.addActionListener(e -> ajouterReparation());
        btnVoirTrou.addActionListener(e -> afficherTrous());
        btnVoirReparation.addActionListener(e -> afficherReparations());

        panelButtons.add(btnAjouterTrou);
        panelButtons.add(btnAjouterReparation);
        panelButtons.add(btnVoirTrou);
        panelButtons.add(btnVoirReparation);

        add(panelButtons, BorderLayout.WEST);

        /* ================= CENTRE ================= */
        panelTrous = new JPanel(new BorderLayout());
        panelTrous.setBorder(BorderFactory.createTitledBorder("Trous"));

        panelReparations = new JPanel(new BorderLayout());
        panelReparations.setBorder(BorderFactory.createTitledBorder("Réparations"));

        JPanel panelCenter = new JPanel(new GridLayout(2, 1, 5, 5));
        panelCenter.add(panelTrous);
        panelCenter.add(panelReparations);

        add(panelCenter, BorderLayout.CENTER);
    }

    /* ================= ROUTE COURANTE ================= */
    public void updateDetails(int idRoute) {
        this.idRouteCourant = idRoute;
        String nomRoute = RouteDAO.getNameById(idRoute);
        btnNomRoute.setText("Route : " + nomRoute);
    }

    /* ================= AJOUT TROU ================= */
    private void ajouterTrou() {
        if (idRouteCourant <= 0) return;

        JDialog dialog = new JDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                "Ajouter un trou",
                true
        );

        dialog.setSize(300, 250);
        dialog.setLayout(new GridLayout(4, 2, 10, 10));
        dialog.setLocationRelativeTo(this);

        JTextField tfSurface = new JTextField();
        JTextField tfProfondeur = new JTextField();
        JTextField tfPosition = new JTextField();

        dialog.add(new JLabel("Surface (m²)"));
        dialog.add(tfSurface);
        dialog.add(new JLabel("Profondeur (cm)"));
        dialog.add(tfProfondeur);
        dialog.add(new JLabel("Position (km)"));
        dialog.add(tfPosition);

        JButton btnValider = new JButton("Ajouter");
        btnValider.addActionListener(e -> {
            try {
                Trou t = new Trou();
                t.setIdRoute(idRouteCourant);
                t.setSurface(Double.parseDouble(tfSurface.getText()));
                t.setProfondeur(Double.parseDouble(tfProfondeur.getText()));
                t.setPositionKm(Double.parseDouble(tfPosition.getText()));

                if (TrouDAO.insererTrou(t)) {
                    JOptionPane.showMessageDialog(dialog, "Trou ajouté !");
                    afficherTrous();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Erreur insertion", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Valeurs invalides", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(new JLabel());
        dialog.add(btnValider);
        dialog.setVisible(true);
    }

    /* ================= AJOUT REPARATION ================= */
    private void ajouterReparation() {
        if (idRouteCourant <= 0) return;

        JDialog dialog = new JDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                "Ajouter réparation",
                true
        );

        dialog.setSize(350, 250);
        dialog.setLayout(new GridLayout(4, 2, 10, 10));
        dialog.setLocationRelativeTo(this);

        JComboBox<TypeRoute> comboType =
                new JComboBox<>(TypeRouteDAO.getAllTypes());

        JTextField tfIdTrou = new JTextField();
        JTextField tfDesc = new JTextField();

        dialog.add(new JLabel("Type de route"));
        dialog.add(comboType);
        dialog.add(new JLabel("ID du trou"));
        dialog.add(tfIdTrou);
        dialog.add(new JLabel("Description"));
        dialog.add(tfDesc);

        JButton btnValider = new JButton("Ajouter");
        btnValider.addActionListener(e -> {
            try {
                int idTrou = Integer.parseInt(tfIdTrou.getText());
                TypeRoute type = (TypeRoute) comboType.getSelectedItem();

                Reparer r = new Reparer();
                r.setIdRoute(idRouteCourant);
                r.setIdTrou(idTrou);
                r.setIdTypeRoute(type.getId());
                r.setDateReparation(new java.util.Date());
                r.setDescription(tfDesc.getText());

                int idReparer = ReparerDAO.insererReparation(r);

                if (idReparer > 0) {
                    Trou trou = TrouDAO.getTrouById(idTrou);
                    double prix = Prix.calculerPrix(trou, type);
                    PrixReparationDAO.insererPrix(idReparer, prix);

                       // --- Nouvelle partie : déplacer le trou dans trou_repare ---
                    TrouRepareDAO.insererTrouRepare(idTrou, idReparer, r.getDateReparation());
                    TrouDAO.marquerCommeRepare(idTrou); // UPDATE trou SET status='repare' WHERE id = ?
                        afficherTrous();
                    
                    JOptionPane.showMessageDialog(dialog,
                            "Réparation ajoutée\nPrix = " + prix);
                    afficherReparations();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Erreur réparation", "Erreur", JOptionPane.ERROR_MESSAGE);
                }

                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Données invalides", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(new JLabel());
        dialog.add(btnValider);
        dialog.setVisible(true);
    }

    /* ================= AFFICHAGES ================= */
    private void afficherTrous() {
        if (idRouteCourant <= 0) return;

        JTable table = new JTable(
                new DefaultTableModel(
                        TrouDAO.getTableData(idRouteCourant),
                        TrouDAO.getColumns()
                )
        );

        panelTrous.removeAll();
        panelTrous.add(new JScrollPane(table), BorderLayout.CENTER);
        panelTrous.revalidate();
        panelTrous.repaint();
    }

    private void afficherReparations() {
        if (idRouteCourant <= 0) return;

        JTable table = new JTable(
                new DefaultTableModel(
                        ReparerDAO.getTableData(idRouteCourant),
                        ReparerDAO.getColumns()
                )
        );

        panelReparations.removeAll();
        panelReparations.add(new JScrollPane(table), BorderLayout.CENTER);
        panelReparations.revalidate();
        panelReparations.repaint();
    }

    /* ================= DETAIL ROUTE ================= */
    private void afficherDetailRoute() {
        Container parent = getParent();
        parent.removeAll();
        parent.add(new DetailRoute(idRouteCourant), BorderLayout.CENTER);
        parent.revalidate();
        parent.repaint();
    }
}
