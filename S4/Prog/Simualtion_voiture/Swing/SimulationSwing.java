package Swing;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;

import Back.modele.Piste;
import Back.modele.Voiture;
import Back.logique.SimulationEngine;

public class SimulationSwing extends JPanel {
    private static final Color BG_DARK = Color.decode("#04070d");
    private static final Color BG_PANEL = Color.decode("#0a1320");
    private static final Color GOLD = Color.decode("#eadc63");
    private static final Color TEXT = Color.decode("#9db5c7");

    private final SimulationEngine engine;
    private final DashboardPanel dashboardPanel;
    private final RaceCanvasPanel raceCanvasPanel;
    private final JLabel stateLabel = new JLabel("Prêt à lancer");
    private final JLabel speedLabel = new JLabel("0.0 km/h");
    private final JLabel distanceLabel = new JLabel("0.000 km");
    private final JLabel realChronoLabel = new JLabel("0.00 s");
    private final JLabel theoreticalChronoLabel;
    private final javax.swing.JButton startButton = new javax.swing.JButton("Start");
    private final javax.swing.JButton accelButton = new javax.swing.JButton("Accélérer");
    private final javax.swing.JButton stopButton = new javax.swing.JButton("Stop");
    private final javax.swing.JButton resetButton = new javax.swing.JButton("Reset");
    private final DecimalFormat oneDecimal = new DecimalFormat("0.0");
    private final DecimalFormat twoDecimals = new DecimalFormat("0.00");
    private final DecimalFormat threeDecimals = new DecimalFormat("0.000");
    private final Timer updateTimer;
    private final Timer countdownTimer;

    public SimulationSwing(Voiture voiture, Piste piste) {
        this.engine = new SimulationEngine(voiture, piste);
        this.theoreticalChronoLabel = new JLabel(formatTime(engine.getTheoreticalTimeSec()));
        this.dashboardPanel = new DashboardPanel(this);
        this.raceCanvasPanel = new RaceCanvasPanel(this);

        setLayout(new BorderLayout());
        setBackground(BG_DARK);

        add(buildHeader(), BorderLayout.NORTH);
        add(buildCenterPane(), BorderLayout.CENTER);
        add(buildControls(), BorderLayout.SOUTH);

        updateTimer = new Timer(16, e -> onUpdateTick());
        countdownTimer = new Timer(1000, e -> onCountdownTick());

        refreshLabels();
    }

    private javax.swing.JPanel buildHeader() {
        javax.swing.JPanel header = new javax.swing.JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(18, 24, 12, 24));

        javax.swing.JLabel title = new javax.swing.JLabel("Simulation voiture - Swing");
        title.setForeground(GOLD);
        title.setFont(new Font("DejaVu Sans", Font.BOLD, 24));

        javax.swing.JLabel subtitle = new javax.swing.JLabel(
                engine.getPiste().getNom() + "  •  " + engine.getVoiture().getNom());
        subtitle.setForeground(TEXT);
        subtitle.setFont(new Font("DejaVu Sans", Font.PLAIN, 14));

        javax.swing.JPanel left = new javax.swing.JPanel(new GridLayout(2, 1));
        left.setOpaque(false);
        left.add(title);
        left.add(subtitle);

        stateLabel.setForeground(GOLD);
        stateLabel.setFont(new Font("DejaVu Sans", Font.BOLD, 18));
        javax.swing.JLabel stateTitle = new javax.swing.JLabel("Etat");
        stateTitle.setForeground(TEXT);
        stateTitle.setFont(new Font("DejaVu Sans", Font.PLAIN, 12));

        javax.swing.JPanel right = new javax.swing.JPanel(new GridLayout(2, 1));
        right.setOpaque(false);
        right.add(stateTitle);
        right.add(stateLabel);

        header.add(left, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);
        return header;
    }

    private javax.swing.JSplitPane buildCenterPane() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, raceCanvasPanel, dashboardPanel);
        splitPane.setResizeWeight(0.70);
        splitPane.setDividerSize(8);
        splitPane.setBorder(null);
        splitPane.setContinuousLayout(true);
        splitPane.setBackground(BG_DARK);
        splitPane.setOpaque(true);
        splitPane.setLeftComponent(raceCanvasPanel);
        splitPane.setRightComponent(dashboardPanel);
        splitPane.setDividerLocation(0.70);
        return splitPane;
    }

    private javax.swing.JPanel buildControls() {
        javax.swing.JPanel root = new javax.swing.JPanel(new BorderLayout());
        root.setOpaque(false);
        root.setBorder(BorderFactory.createEmptyBorder(10, 24, 18, 24));

        javax.swing.JPanel metrics = new javax.swing.JPanel(new GridLayout(2, 4, 14, 10));
        metrics.setOpaque(false);
        metrics.add(makeMetricCard("Vitesse", speedLabel));
        metrics.add(makeMetricCard("Distance", distanceLabel));
        metrics.add(makeMetricCard("Chrono réel", realChronoLabel));
        metrics.add(makeMetricCard("Chrono théorique", theoreticalChronoLabel));
        metrics.add(makeMetricCard("Voiture", new JLabel(engine.getVoiture().getNom(), SwingConstants.CENTER)));
        metrics.add(makeMetricCard("Piste", new JLabel(engine.getPiste().getNom(), SwingConstants.CENTER)));
        metrics.add(makeMetricCard("Vmax",
                new JLabel(oneDecimal.format(engine.getVoiture().getVitesseMax()) + " km/h", SwingConstants.CENTER)));
        metrics.add(makeMetricCard("Accélération",
                new JLabel(oneDecimal.format(engine.getVoiture().getAcceleration()) + " km/h/s",
                        SwingConstants.CENTER)));

        javax.swing.JPanel buttons = new javax.swing.JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        buttons.setOpaque(false);

        stylePrimaryButton(startButton, GOLD, BG_DARK);
        stylePrimaryButton(accelButton, new Color(32, 163, 158), Color.WHITE);
        stylePrimaryButton(stopButton, new Color(180, 62, 62), Color.WHITE);
        stylePrimaryButton(resetButton, new Color(81, 101, 120), Color.WHITE);

        accelButton.setEnabled(false);
        stopButton.setEnabled(false);

        startButton.addActionListener(e -> startCountdown());
        stopButton.addActionListener(e -> stopRace());
        resetButton.addActionListener(e -> resetRace());
        accelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if ((engine.isRunning() || engine.isCountdownActive()) && !engine.isFinished()) {
                    engine.setAccelerating(true);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                engine.setAccelerating(false);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                engine.setAccelerating(false);
            }
        });

        buttons.add(startButton);
        buttons.add(accelButton);
        buttons.add(stopButton);
        buttons.add(resetButton);

        root.add(metrics, BorderLayout.CENTER);
        root.add(buttons, BorderLayout.SOUTH);
        return root;
    }

    private javax.swing.JPanel makeMetricCard(String title, JLabel valueLabel) {
        javax.swing.JPanel card = new javax.swing.JPanel(new BorderLayout());
        card.setOpaque(true);
        card.setBackground(BG_PANEL);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(81, 101, 120), 1),
                BorderFactory.createEmptyBorder(10, 14, 10, 14)));

        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setForeground(TEXT);
        label.setFont(new Font("DejaVu Sans", Font.PLAIN, 12));

        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        valueLabel.setForeground(GOLD);
        valueLabel.setFont(new Font("DejaVu Sans", Font.BOLD, 18));

        card.add(label, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    private void stylePrimaryButton(javax.swing.JButton button, Color background, Color foreground) {
        button.setFont(new Font("DejaVu Sans", Font.BOLD, 14));
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void startCountdown() {
        engine.startCountdown();
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
        accelButton.setEnabled(true);
        countdownTimer.start();
        updateTimer.start();
        refreshView();
    }

    private void onCountdownTick() {
        engine.onCountdownTick();
        if (!engine.isCountdownActive() && engine.isCountdownActive() == false) {
            countdownTimer.stop();
        }
        refreshView();
    }

    private void onUpdateTick() {
        engine.onUpdateTick();
        if (engine.isFinished()) {
            updateTimer.stop();
            startButton.setEnabled(true);
            accelButton.setEnabled(false);
            stopButton.setEnabled(false);
        }
        refreshView();
    }

    private void stopRace() {
        engine.stop();
        countdownTimer.stop();
        updateTimer.stop();
        startButton.setEnabled(true);
        accelButton.setEnabled(false);
        stopButton.setEnabled(false);
        refreshView();
    }

    private void resetRace() {
        countdownTimer.stop();
        updateTimer.stop();
        engine.reset();
        startButton.setEnabled(true);
        accelButton.setEnabled(false);
        stopButton.setEnabled(false);
        refreshView();
    }

    private void refreshLabels() {
        speedLabel.setText(oneDecimal.format(engine.getSpeedKmh()) + " km/h");
        distanceLabel.setText(threeDecimals.format(engine.getDistanceKm()) + " km");
        realChronoLabel.setText(twoDecimals.format(engine.getRealTimeSec()) + " s");
        theoreticalChronoLabel.setText(formatTime(engine.getTheoreticalTimeSec()));
        stateLabel.setText(engine.getStateText());
    }

    private void refreshView() {
        refreshLabels();
        dashboardPanel.refreshValues();
        dashboardPanel.repaint();
        raceCanvasPanel.repaint();
    }

    private String formatTime(double seconds) {
        if (Double.isNaN(seconds) || Double.isInfinite(seconds)) {
            return "--";
        }
        return twoDecimals.format(seconds) + " s";
    }

    // Getters to pass data to panels
    public double getSpeedKmh() {
        return engine.getSpeedKmh();
    }

    public double getDistanceKm() {
        return engine.getDistanceKm();
    }

    public double getRealTimeSec() {
        return engine.getRealTimeSec();
    }

    public double getTheoreticalTimeSec() {
        return engine.getTheoreticalTimeSec();
    }

    public Voiture getVoiture() {
        return engine.getVoiture();
    }

    public Piste getPiste() {
        return engine.getPiste();
    }

    public String getStateText() {
        return engine.getStateText();
    }
}
