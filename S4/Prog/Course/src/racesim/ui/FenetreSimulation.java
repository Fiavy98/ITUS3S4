package racesim.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import racesim.model.Chrono;
import racesim.model.Simulation;
import racesim.model.Voiture;
import racesim.util.TimeFormat;

public class FenetreSimulation extends JFrame {
    private final Simulation simulation;
    private final Chrono chronoCourse;
    private final JLabel chronoCourseLabel;
    private Chrono chronoVoiture;
    private final JLabel chronoVoitureLabel;
    private final VueCockpit cockpit;
    private final Timer timer;
    private long lastTickNs;
    private boolean raceRunning;
    private boolean carChronoStarted;
    private final long countdownStartMs;
    private final double initialTrackLengthM;

    public FenetreSimulation(Voiture voiture, double trackLengthM, long countdownStartMs) {
        this.simulation = new Simulation(voiture, trackLengthM);
        this.chronoCourse = new Chrono(0L);
        this.countdownStartMs = countdownStartMs;
        this.initialTrackLengthM = trackLengthM;

        setTitle("ETU 004373  " + voiture.getNom() + " - " + formatDistance(trackLengthM));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        VuePiste piste = new VuePiste(simulation);
        cockpit = new VueCockpit(simulation);

        JPanel chronoPanel = (JPanel) UiTheme.createCardPanel();
        chronoPanel.setLayout(new BoxLayout(chronoPanel, BoxLayout.Y_AXIS));
        chronoPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel courseTitle = new JLabel("Chrono course");
        courseTitle.setFont(UiTheme.SECTION);
        courseTitle.setForeground(UiTheme.TEXT);
        chronoCourseLabel = new JLabel(TimeFormat.formatMillis(0));
        chronoCourseLabel.setFont(UiTheme.VALUE);
        chronoCourseLabel.setForeground(UiTheme.ACCENT_DARK);

        JLabel voitureTitle = new JLabel("Chrono voiture");
        voitureTitle.setFont(UiTheme.SECTION);
        voitureTitle.setForeground(UiTheme.TEXT);
        chronoVoitureLabel = new JLabel(TimeFormat.formatMillis(countdownStartMs));
        chronoVoitureLabel.setFont(UiTheme.VALUE);
        chronoVoitureLabel.setForeground(UiTheme.SUCCESS);

        JButton startCourse = new JButton("Start");
        JButton stopCourse = new JButton("Stop");
        JButton newRaceButton = new JButton("Nouvelle course");
        UiTheme.styleButton(startCourse, UiTheme.SUCCESS, Color.WHITE);
        UiTheme.styleButton(stopCourse, UiTheme.DANGER, Color.WHITE);
        UiTheme.styleButton(newRaceButton, UiTheme.ACCENT_SOFT, UiTheme.ACCENT_DARK);
        startCourse.addActionListener(e -> startRace());
        stopCourse.addActionListener(e -> stopRace());
        newRaceButton.addActionListener(e -> resetRace());

        JPanel courseButtons = new JPanel(new GridLayout(1, 3, 6, 6));
        courseButtons.setOpaque(false);
        courseButtons.add(startCourse);
        courseButtons.add(stopCourse);
        courseButtons.add(newRaceButton);

        chronoPanel.add(courseTitle);
        chronoPanel.add(chronoCourseLabel);
        chronoPanel.add(Box.createVerticalStrut(12));
        chronoPanel.add(voitureTitle);
        chronoPanel.add(chronoVoitureLabel);
        chronoPanel.add(Box.createVerticalStrut(6));
        chronoPanel.add(courseButtons);

        JPanel dashboard = new JPanel(new BorderLayout(12, 12));
        dashboard.setOpaque(false);
        dashboard.add(cockpit, BorderLayout.CENTER);
        dashboard.add(chronoPanel, BorderLayout.EAST);

        JPanel header = createHeaderPanel(voiture.getNom(), trackLengthM);

        JPanel trackCard = (JPanel) UiTheme.createCardPanel();
        trackCard.setLayout(new BorderLayout());
        trackCard.add(piste, BorderLayout.CENTER);

        JPanel main = new BackgroundPanel();
        main.setLayout(new BorderLayout(18, 18));
        main.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        main.add(header, BorderLayout.NORTH);
        main.add(trackCard, BorderLayout.CENTER);
        main.add(dashboard, BorderLayout.SOUTH);

        setContentPane(main);
        setSize(1100, 720);
        setLocationRelativeTo(null);

        cockpit.setAccelerationEnabled(false);
        cockpit.setArrivalSpeedKmh(null);

        timer = new Timer(16, e -> onTick(piste));
        timer.start();
    }

    private void onTick(VuePiste piste) {
        long now = System.nanoTime();
        double deltaSeconds = (now - lastTickNs) / 1_000_000_000.0;
        lastTickNs = now;
        if (raceRunning) {
            simulation.update(deltaSeconds);
            if (!carChronoStarted && simulation.isAccelerating()) {
                long countdownMs = getCountdownMs();
                chronoVoiture = new Chrono(countdownMs);
                chronoVoiture.start();
                carChronoStarted = true;
            }
        }

        if (simulation.isFinished()) {
            chronoCourse.stop();
            if (chronoVoiture != null && chronoVoiture.isRunning()) {
                chronoVoiture.stop();
            }
            simulation.setAccelerating(false);
            cockpit.setAccelerationEnabled(false);
            cockpit.setArrivalSpeedKmh(simulation.getVitesseKmH());
        }

        cockpit.setSpeedKmh(simulation.getVitesseKmH());
        cockpit.setNitroPercent(simulation.getNitroPercent());
        chronoCourseLabel.setText(TimeFormat.formatMillis(chronoCourse.getElapsedMs()));
        chronoVoitureLabel.setText(TimeFormat.formatMillis(getVoitureChronoMs()));
        piste.repaint();
    }

    private void startRace() {
        if (raceRunning) {
            return;
        }
        lastTickNs = System.nanoTime();
        raceRunning = true;
        carChronoStarted = false;
        chronoVoiture = null;
        chronoCourse.start();
        cockpit.setAccelerationEnabled(true);
    }

    private void stopRace() {
        if (!raceRunning) {
            return;
        }
        raceRunning = false;
        chronoCourse.stop();
        if (chronoVoiture != null && chronoVoiture.isRunning()) {
            chronoVoiture.stop();
        }
        simulation.setAccelerating(false);
        cockpit.setAccelerationEnabled(false);
    }

    private void resetRace() {
        raceRunning = false;
        carChronoStarted = false;
        chronoCourse.reset();
        chronoVoiture = null;
        simulation.reset();
        lastTickNs = System.nanoTime();
        cockpit.setAccelerationEnabled(false);
        cockpit.setArrivalSpeedKmh(null);
        cockpit.setSpeedKmh(0.0);
        chronoCourseLabel.setText(TimeFormat.formatMillis(0));
        chronoVoitureLabel.setText(TimeFormat.formatMillis(countdownStartMs));
        simulation.setTrackLengthM(initialTrackLengthM);
        timer.restart();
    }

    private long getCountdownMs() {
        long ms = countdownStartMs + chronoCourse.getElapsedMs();
        return Math.min(ms, 0L);
    }

    private long getVoitureChronoMs() {
        if (carChronoStarted && chronoVoiture != null) {
            return chronoVoiture.getElapsedMs();
        }
        return getCountdownMs();
    }

    private String formatDistance(double distanceM) {
        if (Math.abs(distanceM - Math.round(distanceM)) < 0.001) {
            return String.format("%d m", Math.round(distanceM));
        }
        return String.format("%.1f m", distanceM);
    }

    private JPanel createHeaderPanel(String voitureName, double trackLengthM) {
        JPanel header = new JPanel(new BorderLayout(12, 12));
        header.setOpaque(false);

        JLabel title = new JLabel("Course en direct");
        title.setFont(UiTheme.TITLE);
        title.setForeground(UiTheme.TEXT);

        JLabel subtitle = new JLabel(voitureName + " · " + formatDistance(trackLengthM) + " · compte a rebours configurable");
        subtitle.setFont(UiTheme.SUBTITLE);
        subtitle.setForeground(UiTheme.MUTED);

        JPanel text = new JPanel(new BorderLayout(0, 4));
        text.setOpaque(false);
        text.add(title, BorderLayout.NORTH);
        text.add(subtitle, BorderLayout.SOUTH);

        JLabel live = new JLabel("Live");
        live.setOpaque(true);
        live.setBackground(UiTheme.ACCENT_SOFT);
        live.setForeground(UiTheme.ACCENT_DARK);
        live.setFont(UiTheme.SECTION);
        live.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));

        header.add(text, BorderLayout.WEST);
        header.add(live, BorderLayout.EAST);
        return header;
    }

    private static class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint paint = new GradientPaint(0, 0, UiTheme.BG_TOP, 0, getHeight(), UiTheme.BG_BOTTOM);
            g2.setPaint(paint);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setColor(new Color(0, 123, 255, 25));
            g2.fillOval(-120, -80, 260, 260);
            g2.fillOval(getWidth() - 180, getHeight() - 220, 280, 280);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
