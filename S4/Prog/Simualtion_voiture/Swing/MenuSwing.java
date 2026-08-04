package Swing;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import Back.logique.DAO.PisteDAO;
import Back.logique.DAO.VoitureDAO;
import Back.modele.Piste;
import Back.modele.Voiture;

public class MenuSwing extends JPanel {
    private JComboBox<String> voitureCombo;
    private JComboBox<String> pisteCombo;
    private List<Voiture> voitures;
    private List<Piste> pistes;
    private JFrame frame;

    public MenuSwing(JFrame frame) {
        this.frame = frame;

        VoitureDAO voitureDAO = new VoitureDAO();
        PisteDAO pisteDAO = new PisteDAO();

        this.voitures = voitureDAO.getAllVoitures();
        this.pistes = pisteDAO.getAllPiste();

        setBackground(new Color(4, 7, 13));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Title
        JLabel title = new JLabel("SIMULATION VOITURE");
        title.setFont(new Font("DejaVu Sans", Font.BOLD, 48));
        title.setForeground(new Color(234, 220, 99));
        add(title, gbc);

        // Voiture label
        gbc.gridy++;
        gbc.insets = new Insets(30, 15, 5, 15);
        JLabel voitureLabel = new JLabel("Sélectionner une voiture :");
        voitureLabel.setFont(new Font("DejaVu Sans", Font.BOLD, 20));
        voitureLabel.setForeground(new Color(234, 220, 99));
        add(voitureLabel, gbc);

        // Voiture combo
        gbc.gridy++;
        gbc.insets = new Insets(5, 15, 15, 15);
        voitureCombo = new JComboBox<>();
        voitureCombo.setPreferredSize(new Dimension(400, 40));
        voitureCombo.setFont(new Font("DejaVu Sans", Font.PLAIN, 16));
        for (Voiture v : voitures) {
            voitureCombo.addItem(v.getNom());
        }
        add(voitureCombo, gbc);

        // Piste label
        gbc.gridy++;
        gbc.insets = new Insets(30, 15, 5, 15);
        JLabel pisteLabel = new JLabel("Sélectionner une piste :");
        pisteLabel.setFont(new Font("DejaVu Sans", Font.BOLD, 20));
        pisteLabel.setForeground(new Color(234, 220, 99));
        add(pisteLabel, gbc);

        // Piste combo
        gbc.gridy++;
        gbc.insets = new Insets(5, 15, 15, 15);
        pisteCombo = new JComboBox<>();
        pisteCombo.setPreferredSize(new Dimension(400, 40));
        pisteCombo.setFont(new Font("DejaVu Sans", Font.PLAIN, 16));
        for (Piste p : pistes) {
            pisteCombo.addItem(p.getNom() + " (" + p.getLongueur() + " km)");
        }
        add(pisteCombo, gbc);

        // Start button
        gbc.gridy++;
        gbc.insets = new Insets(30, 15, 15, 15);
        JButton startButton = new JButton("DÉMARRER LA SIMULATION");
        startButton.setFont(new Font("DejaVu Sans", Font.BOLD, 18));
        startButton.setBackground(new Color(234, 220, 99));
        startButton.setForeground(new Color(4, 7, 13));
        startButton.setPreferredSize(new Dimension(400, 50));
        startButton.setFocusPainted(false);
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        startButton.addActionListener(e -> startSimulation());
        add(startButton, gbc);
    }

    private void startSimulation() {
        if (voitureCombo.getSelectedItem() == null || pisteCombo.getSelectedItem() == null) {
            return;
        }

        String selectedVoiture = (String) voitureCombo.getSelectedItem();
        String selectedPiste = ((String) pisteCombo.getSelectedItem()).split(" ")[0];

        Voiture voiture = null;
        for (Voiture v : voitures) {
            if (v.getNom().equals(selectedVoiture)) {
                voiture = v;
                break;
            }
        }

        Piste piste = null;
        for (Piste p : pistes) {
            if (p.getNom().equals(selectedPiste)) {
                piste = p;
                break;
            }
        }

        if (voiture != null && piste != null) {
            frame.setContentPane(new SimulationSwing(voiture, piste));
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.revalidate();
            frame.repaint();
        }
    }
}
