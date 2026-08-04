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
        btnAjouterIntervalle.setFocusPainted(false);
        btnAjouterIntervalle.addActionListener(e -> ajouterRoute());

        panelHeader.add(btnNomRoute, BorderLayout.WEST);
        panelHeader.add(btnAjouterIntervalle, BorderLayout.EAST);

        add(panelHeader, BorderLayout.NORTH);

        /* ================= BOUTONS GAUCHE ================= */
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
        nomRoute = RouteDAO.getNameById(idRoute);
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
        dialog.setLayout(new GridLayout(6, 2, 10, 10));
        dialog.setLocationRelativeTo(this);

        JTextField tfNom = new JTextField();
        JTextField tfSurface = new JTextField();
        JTextField tfProfondeur = new JTextField();
        JTextField tfPosition = new JTextField();

        dialog.add(new JLabel("Nom"));
        dialog.add(tfNom);
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
                t.setNom(tfNom.getText());
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

    /* ================= AJOUT INTERVALLE ROUTE ================= */
    private void ajouterRoute() {
        if (idRouteCourant <= 0) return;

        JDialog dialog = new JDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                "Ajouter intervalle de route",
                true
        );

        dialog.setSize(350, 250);
        dialog.setLayout(new GridLayout(5, 2, 10, 10));
        dialog.setLocationRelativeTo(this);

        JTextField tfNom = new JTextField();
        JTextField tfDepart = new JTextField();
        JTextField tfArrive = new JTextField();
        JTextField tfPluie = new JTextField();

        dialog.add(new JLabel("Nom"));
        dialog.add(tfNom);
        dialog.add(new JLabel("Départ (km)"));
        dialog.add(tfDepart);
        dialog.add(new JLabel("Arrivée (km)"));
        dialog.add(tfArrive);
        dialog.add(new JLabel("Pluie"));
        dialog.add(tfPluie);

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
                JOptionPane.showMessageDialog(dialog, "Intervalle ajouté !");
                dialog.dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
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

        dialog.setSize(350, 200);
        dialog.setLayout(new GridLayout(3, 2, 10, 10));
        dialog.setLocationRelativeTo(this);

        JTextField tfIdTrou = new JTextField();
        JTextField tfDesc = new JTextField();

        dialog.add(new JLabel("ID du trou"));
        dialog.add(tfIdTrou);
        dialog.add(new JLabel("Description"));
        dialog.add(tfDesc);

        JButton btnValider = new JButton("Ajouter");

        btnValider.addActionListener(e -> {
            try {
                /* ================= 1️⃣ TROU ================= */
                int idTrou = Integer.parseInt(tfIdTrou.getText());
                Trou trou = TrouDAO.getTrouById(idTrou);

                if (trou == null)
                    throw new Exception("Trou introuvable");

                /* ================= 2️⃣ INTERVALLE ================= */
                IntervRoute interval =
                    IntervRouteDAO.getByRouteAndKm(nomRoute, trou.getPositionKm());

    
                if (interval == null)
                    throw new Exception(
                        "Aucun intervalle défini pour le km "
                        + trou.getPositionKm()
                    );

                /* ================= 3️⃣ TYPE (PLUIE) ================= */
                String nomType =
                        Pluie.determinerNomType(interval.getPluie());

                TypeRoute typeRoute =
                        TypeRouteDAO.getByName(nomType);

                if (typeRoute == null)
                    throw new Exception(
                        "Type de route introuvable : " + nomType
                    );

                /* ================= 4️⃣ PRIX ================= */
                double prix =
                        Prix.calculerPrix(trou, typeRoute);

                /* ================= 5️⃣ INSERER REPARATION ================= */
                Reparer r = new Reparer();
                r.setIdRoute(idRouteCourant);
                r.setIdTrou(idTrou);
                r.setIdTypeRoute(typeRoute.getId());
                r.setDateReparation(new java.util.Date());
                r.setDescription(tfDesc.getText());

                int idReparer = ReparerDAO.insererReparation(r);

                if (idReparer <= 0)
                    throw new Exception("Insertion réparation échouée");

                /* ================= 6️⃣ INSERER PRIX ================= */
                PrixReparationDAO.insererPrix(idReparer, prix);

                /* ================= 7️⃣ MARQUER TROU ================= */
                TrouRepareDAO.insererTrouRepare(
                        idTrou,
                        idReparer,
                        r.getDateReparation()
                );

                TrouDAO.marquerCommeRepare(idTrou);

                /* ================= 8️⃣ RAFRAÎCHIR UI ================= */
                afficherTrous();
                afficherReparations();

                JOptionPane.showMessageDialog(
                        dialog,
                        "Réparation ajoutée avec succès\n"
                        + "Type : " + typeRoute.getNom() + "\n"
                        + "Prix : " + prix + " Ar"
                );

                dialog.dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        dialog,
                        ex.getMessage(),
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        dialog.add(new JLabel());
        dialog.add(btnValider);
        dialog.setVisible(true);
    }


    /* ================= AFFICHAGES ================= */
    private void afficherTrous() {
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
        JTable table = new JTable(
                new DefaultTableModel(
                        ReparerDAO.getTableData(idRouteCourant),
                        ReparerDAO.getColumns()
                )
        );
        
        double total = PrixReparationDAO.getTotalByRoute(idRouteCourant);

        JLabel lblTotal = new JLabel(
            "Total des réparations : " + total + " Ar",
            SwingConstants.CENTER
        );
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));

        

        panelReparations.removeAll();
        panelReparations.add(new JScrollPane(table), BorderLayout.CENTER);
        panelReparations.add(lblTotal, BorderLayout.SOUTH);
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
