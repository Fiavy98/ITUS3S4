package racesim.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.IOException;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import racesim.data.VoitureRepository;
import racesim.model.Voiture;

public class FenetreAccueil extends JFrame {
    private final VoitureRepository repository;
    private final DefaultComboBoxModel<Voiture> comboModel;
    private final JComboBox<Voiture> comboBox;
    private final JTextField trackLengthField;
    private final JTextField countdownField;
    private final JTextField nomField;
    private final JTextField accelField;
    private final JTextField vmaxField;
    private final JTextField nitroWeightField;
    private final JTextField nitroCapacityField;
    private final JTextField nitroConsumptionField;

    public FenetreAccueil() {
        repository = VoitureRepository.defaultRepo();

        setTitle("RaceSim - Configuration");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(980, 520));
        setLocationRelativeTo(null);

        comboModel = new DefaultComboBoxModel<>();
        List<Voiture> voitures = repository.load();
        for (Voiture v : voitures) {
            comboModel.addElement(v);
        }
        comboBox = new JComboBox<>(comboModel);
        comboBox.setPreferredSize(new Dimension(250, 34));
        comboBox.setFont(UiTheme.BODY);
        comboBox.setBackground(Color.WHITE);

        trackLengthField = new JTextField("400", 6);
        countdownField = new JTextField("-5", 4);
        UiTheme.styleField(trackLengthField);
        UiTheme.styleField(countdownField);

        JButton startButton = new JButton("Demarrer");
        UiTheme.styleButton(startButton, UiTheme.ACCENT, Color.WHITE);
        startButton.addActionListener(e -> openSimulation());

        JPanel header = createHeaderPanel();

        JPanel selectionCard = (JPanel) UiTheme.createCardPanel();
        selectionCard.setLayout(new BorderLayout(0, 12));

        JPanel selectionFields = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        selectionFields.setOpaque(false);
        selectionFields.add(createLabelPair("Voiture", comboBox));
        selectionFields.add(createLabelPair("Piste (m)", trackLengthField));
        selectionFields.add(createLabelPair("Depart (s)", countdownField));
        selectionFields.add(startButton);

        JLabel selectionTitle = new JLabel("Lancer une simulation");
        selectionTitle.setFont(UiTheme.SECTION);
        selectionTitle.setForeground(UiTheme.TEXT);

        JLabel selectionHint = new JLabel("Choisis une voiture, la longueur du terrain et le compte a rebours.");
        selectionHint.setFont(UiTheme.SUBTITLE);
        selectionHint.setForeground(UiTheme.MUTED);

        JPanel selectionText = new JPanel(new BorderLayout(0, 4));
        selectionText.setOpaque(false);
        selectionText.add(selectionTitle, BorderLayout.NORTH);
        selectionText.add(selectionHint, BorderLayout.SOUTH);

        selectionCard.add(selectionText, BorderLayout.NORTH);
        selectionCard.add(selectionFields, BorderLayout.CENTER);

        JLabel addTitle = new JLabel("Ajouter une voiture");
        addTitle.setFont(UiTheme.SECTION);
        addTitle.setForeground(UiTheme.TEXT);

        nomField = new JTextField(10);
        accelField = new JTextField(6);
        vmaxField = new JTextField(6);
        nitroWeightField = new JTextField("10", 6);
        nitroCapacityField = new JTextField("40", 6);
        nitroConsumptionField = new JTextField("120", 6);
        UiTheme.styleField(nomField);
        UiTheme.styleField(accelField);
        UiTheme.styleField(vmaxField);
        UiTheme.styleField(nitroWeightField);
        UiTheme.styleField(nitroCapacityField);
        UiTheme.styleField(nitroConsumptionField);

        JPanel addFields = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        addFields.setOpaque(false);
        addFields.add(createLabelPair("Nom", nomField));
        addFields.add(createLabelPair("Acceleration (km/h/s)", accelField));
        addFields.add(createLabelPair("Vitesse max (km/h)", vmaxField));
        addFields.add(createLabelPair("Poids NOS (kg)", nitroWeightField));
        addFields.add(createLabelPair("Capacite NOS (kg)", nitroCapacityField));
        addFields.add(createLabelPair("Consommation NOS (kg/min)", nitroConsumptionField));

        JButton addButton = new JButton("Ajouter");
        UiTheme.styleButton(addButton, UiTheme.ACCENT_SOFT, UiTheme.ACCENT_DARK);
        addButton.addActionListener(e -> addCar());

        JPanel addPanel = (JPanel) UiTheme.createCardPanel();
        addPanel.setLayout(new BorderLayout(0, 12));
        addPanel.add(addTitle, BorderLayout.NORTH);
        addPanel.add(addFields, BorderLayout.CENTER);
        addPanel.add(addButton, BorderLayout.SOUTH);

        JPanel main = new BackgroundPanel();
        main.setLayout(new BorderLayout(20, 20));
        main.setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));
        main.add(header, BorderLayout.NORTH);

        JPanel centerStack = new JPanel(new BorderLayout(0, 16));
        centerStack.setOpaque(false);
        centerStack.add(selectionCard, BorderLayout.NORTH);
        centerStack.add(addPanel, BorderLayout.CENTER);

        main.add(centerStack, BorderLayout.CENTER);

        setContentPane(main);
    }

    private void openSimulation() {
        Voiture selected = (Voiture) comboBox.getSelectedItem();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Aucune voiture selectionnee.");
            return;
        }
        Double trackLength = parseDouble(trackLengthField.getText());
        Double countdownSeconds = parseDouble(countdownField.getText());
        if (trackLength == null || trackLength <= 0.0 || countdownSeconds == null) {
            JOptionPane.showMessageDialog(this, "Verifier la piste et le depart.");
            return;
        }
        if (countdownSeconds > 0.0) {
            countdownSeconds = -countdownSeconds;
        }
        long countdownMs = Math.round(countdownSeconds * 1000.0);

        FenetreSimulation sim = new FenetreSimulation(selected, trackLength, countdownMs);
        sim.setVisible(true);
    }

    private void addCar() {
        String nom = nomField.getText().trim();
        Double accel = parseDouble(accelField.getText());
        Double vmax = parseDouble(vmaxField.getText());
        Double nitroWeight = parseDouble(nitroWeightField.getText());
        Double nitroCapacity = parseDouble(nitroCapacityField.getText());
        Double nitroConsumption = parseDouble(nitroConsumptionField.getText());

        if (nom.isEmpty() || accel == null || vmax == null
                || nitroWeight == null || nitroCapacity == null || nitroConsumption == null
                || nitroWeight < 0.0 || nitroCapacity < 0.0 || nitroConsumption < 0.0) {
            JOptionPane.showMessageDialog(this, "Verifier les champs.");
            return;
        }

        Voiture voiture = new Voiture(nom, accel, vmax, nitroWeight, nitroCapacity, nitroConsumption);
        try {
            repository.append(voiture);
            comboModel.addElement(voiture);
            comboBox.setSelectedItem(voiture);
            nomField.setText("");
            accelField.setText("");
            vmaxField.setText("");
            nitroWeightField.setText("10");
            nitroCapacityField.setText("40");
            nitroConsumptionField.setText("120");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erreur d'ecriture: " + e.getMessage());
        }
    }

    private Double parseDouble(String text) {
        String trimmed = text.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        try {
            return Double.valueOf(trimmed.replace(',', '.'));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("RaceSim");
        title.setFont(UiTheme.TITLE);
        title.setForeground(UiTheme.TEXT);

        JLabel subtitle = new JLabel("Configure la voiture, la piste et le depart avant de lancer la simulation.");
        subtitle.setFont(UiTheme.SUBTITLE);
        subtitle.setForeground(UiTheme.MUTED);

        JPanel text = new JPanel(new BorderLayout(0, 6));
        text.setOpaque(false);
        text.add(title, BorderLayout.NORTH);
        text.add(subtitle, BorderLayout.SOUTH);

        JLabel badge = new JLabel("Simulation de course");
        badge.setOpaque(true);
        badge.setBackground(UiTheme.ACCENT_SOFT);
        badge.setForeground(UiTheme.ACCENT_DARK);
        badge.setFont(UiTheme.SECTION);
        badge.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));

        header.add(text, BorderLayout.WEST);
        header.add(badge, BorderLayout.EAST);
        return header;
    }

    private JPanel createLabelPair(String labelText, java.awt.Component component) {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setOpaque(false);
        JLabel label = new JLabel(labelText);
        label.setFont(UiTheme.SUBTITLE);
        label.setForeground(UiTheme.MUTED);
        panel.add(label, BorderLayout.NORTH);
        panel.add(component, BorderLayout.SOUTH);
        return panel;
    }

    private static class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint paint = new GradientPaint(0, 0, UiTheme.BG_TOP, 0, getHeight(), UiTheme.BG_BOTTOM);
            g2.setPaint(paint);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setColor(new Color(0, 123, 255, 30));
            g2.fillOval(getWidth() - 220, -60, 260, 260);
            g2.fillOval(-120, getHeight() - 180, 240, 240);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
