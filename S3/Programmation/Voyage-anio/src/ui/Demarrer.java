package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import dao.*;
import model.*;

public class Demarrer extends JPanel {

    private JPanel panelButtons;
    private JPanel panelTrous;
    private JPanel panelReparations;

    private JButton btnNomRoute;
    private int idRouteCourant = -1;
    private String nomRoute;

    public Demarrer() {
        setLayout(new BorderLayout(10, 10));

        /* ================= HEADER ================= */
        JPanel panelHeader = new JPanel(new BorderLayout());

        btnNomRoute = new JButton("Route");
        btnNomRoute.setFont(new Font("Arial", Font.BOLD, 18));
        btnNomRoute.setBorderPainted(false);
        btnNomRoute.setContentAreaFilled(false);
        btnNomRoute.setFocusPainted(false);
        btnNomRoute.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNomRoute.addActionListener(e -> afficherDetailRoute());

        JButton btnAjouterIntervalle = new JButton("+ Intervalle");
        btnAjouterIntervalle.addActionListener(e -> ajouterRoute());

        panelHeader.add(btnNomRoute, BorderLayout.WEST);
        panelHeader.add(btnAjouterIntervalle, BorderLayout.EAST);
        add(panelHeader, BorderLayout.NORTH);

        /* ================= BOUTONS ================= */
        panelButtons = new JPanel(new GridLayout(4, 1, 5, 5));
        panelButtons.setPreferredSize(new Dimension(180, 200));

        JButton btnAjouterTrou = new JButton("Ajouter un trou");
        JButton btnAjouterReparation = new JButton("Ajouter réparation");
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
        this.nomRoute = RouteDAO.getNameById(idRoute);
        btnNomRoute.setText("Route : " + nomRoute);
    }

    /* ================= AJOUT TROU ================= */
    private void ajouterTrou() {
        if (idRouteCourant <= 0) return;

        JDialog dialog = new JDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                "Ajouter un trou", true
        );
        dialog.setSize(300, 250);
        dialog.setLayout(new GridLayout(6, 2, 10, 10));
        dialog.setLocationRelativeTo(this);

        JTextField tfNom = new JTextField();
        JTextField tfSurface = new JTextField();
        JTextField tfProfondeur = new JTextField();
        JTextField tfPosition = new JTextField();

        dialog.add(new JLabel("Nom")); dialog.add(tfNom);
        dialog.add(new JLabel("Surface (m²)")); dialog.add(tfSurface);
        dialog.add(new JLabel("Profondeur")); dialog.add(tfProfondeur);
        dialog.add(new JLabel("Position (km)")); dialog.add(tfPosition);

        JButton btnValider = new JButton("Ajouter");
        btnValider.addActionListener(e -> {
            try {
                Trou t = new Trou();
                t.setNom(tfNom.getText());
                t.setIdRoute(idRouteCourant);
                t.setSurface(Double.parseDouble(tfSurface.getText()));
                t.setProfondeur(Double.parseDouble(tfProfondeur.getText()));
                t.setPositionKm(Double.parseDouble(tfPosition.getText()));

                TrouDAO.insererTrou(t);
                afficherTrous();
                dialog.dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Valeurs invalides");
            }
        });

        dialog.add(new JLabel());
        dialog.add(btnValider);
        dialog.setVisible(true);
    }

    /* ================= AJOUT INTERVALLE ================= */
    private void ajouterRoute() {
        if (idRouteCourant <= 0) return;

        JDialog dialog = new JDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                "Ajouter intervalle", true
        );
        dialog.setSize(350, 250);
        dialog.setLayout(new GridLayout(5, 2, 10, 10));
        dialog.setLocationRelativeTo(this);

        JTextField tfNom = new JTextField();
        JTextField tfDepart = new JTextField();
        JTextField tfArrive = new JTextField();
        JTextField tfPluie = new JTextField();

        dialog.add(new JLabel("Nom")); dialog.add(tfNom);
        dialog.add(new JLabel("Départ (km)")); dialog.add(tfDepart);
        dialog.add(new JLabel("Arrivée (km)")); dialog.add(tfArrive);
        dialog.add(new JLabel("Pluie")); dialog.add(tfPluie);

        JButton btnValider = new JButton("Ajouter");
        btnValider.addActionListener(e -> {
            try {
                IntervRoute i = new IntervRoute();
                i.setNom(tfNom.getText());
                i.setRn(nomRoute);
                i.setDepartKm(Double.parseDouble(tfDepart.getText()));
                i.setArriveKm(Double.parseDouble(tfArrive.getText()));
                i.setPluie(Double.parseDouble(tfPluie.getText()));

                IntervRouteDAO.inserer(i);
                dialog.dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Erreur saisie");
            }
        });

        dialog.add(new JLabel());
        dialog.add(btnValider);
        dialog.setVisible(true);
    }

    /* ================= AJOUT RÉPARATION ================= */
    private void ajouterReparation() {
        if (idRouteCourant <= 0) return;

        JDialog dialog = new JDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                "Ajouter réparation", true
        );
        dialog.setSize(350, 200);
        dialog.setLayout(new GridLayout(3, 2, 10, 10));

        JTextField tfIdTrou = new JTextField();
        JTextField tfDesc = new JTextField();

        dialog.add(new JLabel("ID Trou")); dialog.add(tfIdTrou);
        dialog.add(new JLabel("Description")); dialog.add(tfDesc);

        JButton btnValider = new JButton("Ajouter");
        btnValider.addActionListener(e -> {
            try {
                int idTrou = Integer.parseInt(tfIdTrou.getText());
                Trou trou = TrouDAO.getTrouById(idTrou);

                IntervRoute interval =
                        IntervRouteDAO.getByRouteAndKm(
                                nomRoute, trou.getPositionKm()
                        );

                String nomType =
                        Pluie.determinerNomType(interval.getPluie());

                TypeRoute typeRoute =
                        TypeRouteDAO.getByName(nomType);

                double prix =
                        Prix.calculerPrix(trou, typeRoute);

                Reparer r = new Reparer();
                r.setIdRoute(idRouteCourant);
                r.setIdTrou(idTrou);
                r.setIdTypeRoute(typeRoute.getId());
                r.setDateReparation(new java.util.Date());
                r.setDescription(tfDesc.getText());

                int idRep = ReparerDAO.insererReparation(r);
                PrixReparationDAO.insererPrix(idRep, prix);
                TrouDAO.marquerCommeRepare(idTrou);

                afficherReparations();
                afficherTrous();

                dialog.dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage());
            }
        });

        dialog.add(new JLabel());
        dialog.add(btnValider);
        dialog.setVisible(true);
    }

    /* ================= AFFICHER TROUS ================= */
    private void afficherTrous() {
        JTable table = new JTable(
                new DefaultTableModel(
                        TrouDAO.getTableData(idRouteCourant),
                        TrouDAO.getColumns()
                )
        );

        panelTrous.removeAll();
        panelTrous.add(new JScrollPane(table));
        panelTrous.revalidate();
        panelTrous.repaint();
    }

    /* ================= AFFICHER RÉPARATIONS + TOTAL INTERVALLE ================= */
    private void afficherReparations() {

        panelReparations.removeAll();

        JComboBox<IntervRoute> cbIntervalle =
                new JComboBox<>(IntervRouteDAO.getByRoute(nomRoute));

        JButton btnCalculer = new JButton("Total intervalle");
        JLabel lblTotal = new JLabel(" ", SwingConstants.CENTER);
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel top = new JPanel();
        top.add(new JLabel("Intervalle"));
        top.add(cbIntervalle);
        top.add(btnCalculer);

        JTable table = new JTable(
                new DefaultTableModel(
                        ReparerDAO.getTableData(idRouteCourant),
                        ReparerDAO.getColumns()
                )
        );

        btnCalculer.addActionListener(e -> {
            IntervRoute i = (IntervRoute) cbIntervalle.getSelectedItem();
            double total =
                    PrixReparationDAO.getTotalByIntervalle(i.getId());

            lblTotal.setText(
                    "Total [" + i.getDepartKm() + " → "
                            + i.getArriveKm() + "] = " + total + " Ar"
            );
        });

        panelReparations.add(top, BorderLayout.NORTH);
        panelReparations.add(new JScrollPane(table), BorderLayout.CENTER);
        panelReparations.add(lblTotal, BorderLayout.SOUTH);

        panelReparations.revalidate();
        panelReparations.repaint();
    }

    /* ================= DETAIL ROUTE ================= */
    private void afficherDetailRoute() {
        Container parent = getParent();
        parent.removeAll();
        parent.add(new DetailRoute(idRouteCourant));
        parent.revalidate();
        parent.repaint();
    }
}
