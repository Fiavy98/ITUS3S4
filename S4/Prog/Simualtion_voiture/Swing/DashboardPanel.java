package Swing;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;

public class DashboardPanel extends JPanel {
    private final SimulationSwing simulation;
    private final javax.swing.JLabel speedValueLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel realChronoValueLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel theoreticalChronoValueLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel stateValueLabel = new javax.swing.JLabel();

    public DashboardPanel(SimulationSwing simulation) {
        this.simulation = simulation;
        setOpaque(false);
        setBorder(new EmptyBorder(16, 16, 16, 16));
        setPreferredSize(new Dimension(360, 520));
        setLayout(new BorderLayout(0, 14));
        add(buildInfoGrid(), BorderLayout.SOUTH);
    }

    private JPanel buildInfoGrid() {
        JPanel grid = new JPanel(new GridLayout(2, 2, 10, 10));
        grid.setOpaque(false);
        grid.add(metricCard("Vitesse", speedValueLabel));
        grid.add(metricCard("Chrono réel", realChronoValueLabel));
        grid.add(metricCard("Chrono théorique", theoreticalChronoValueLabel));
        grid.add(metricCard("Etat", stateValueLabel));
        return grid;
    }

    public void refreshValues() {
        speedValueLabel.setText(formatSpeed(simulation.getSpeedKmh()));
        realChronoValueLabel.setText(formatTime(simulation.getRealTimeSec()));
        theoreticalChronoValueLabel.setText(formatTime(simulation.getTheoreticalTimeSec()));
        stateValueLabel.setText(simulation.getStateText());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        g2.setColor(Color.decode("#050a12"));
        g2.fillRoundRect(8, 8, w - 16, h - 16, 30, 30);
        g2.setColor(new Color(40, 56, 78));
        g2.setStroke(new BasicStroke(2f));
        g2.drawRoundRect(8, 8, w - 16, h - 16, 30, 30);

        double speed = simulation != null ? simulation.getSpeedKmh() : 0.0;
        drawGauge(g2, w / 2, (int) (h * 0.38), Math.min(120, Math.min(w, h) / 3), 0, 240,
                speed, "240", "km/h", true);

        g2.setColor(new Color(157, 181, 199));
        g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g2.drawString("Tableau de bord", 24, 28);
        g2.dispose();
    }

    private JPanel metricCard(String title, javax.swing.JLabel valueLabel) {
        JPanel card = new JPanel(new BorderLayout());
        card.setOpaque(true);
        card.setBackground(Color.decode("#0a1320"));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(81, 101, 120), 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));

        javax.swing.JLabel label = new javax.swing.JLabel(title, javax.swing.SwingConstants.CENTER);
        label.setForeground(Color.decode("#9db5c7"));
        label.setFont(new Font("DejaVu Sans", Font.PLAIN, 12));

        valueLabel.setText("");
        valueLabel.setForeground(Color.decode("#eadc63"));
        valueLabel.setFont(new Font("DejaVu Sans", Font.BOLD, 18));
        valueLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        card.add(label, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    private void drawGauge(Graphics2D g2, int centerX, int centerY, int radius, double minValue, double maxValue,
            double currentValue, String topLabel, String bottomLabel, boolean withSpeedScale) {
        g2.setColor(Color.BLACK);
        g2.fillOval(centerX - radius - 28, centerY - radius - 28, (radius + 28) * 2, (radius + 28) * 2);

        g2.setColor(new Color(27, 45, 61));
        g2.fillOval(centerX - radius - 10, centerY - radius - 10, (radius + 10) * 2, (radius + 10) * 2);

        g2.setColor(new Color(15, 23, 34));
        g2.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);

        g2.setColor(new Color(234, 220, 99));
        g2.setStroke(new BasicStroke(3f));
        g2.drawOval(centerX - radius, centerY - radius, radius * 2, radius * 2);

        double startAngle = 140;
        double endAngle = 400;
        int majorTicks = withSpeedScale ? 12 : 8;
        int minorTicks = 4;
        int totalTicks = majorTicks * minorTicks;

        g2.setFont(new Font("SansSerif", Font.BOLD, withSpeedScale ? 15 : 12));

        for (int i = 0; i <= totalTicks; i++) {
            double t = (double) i / totalTicks;
            double angle = Math.toRadians(startAngle + (endAngle - startAngle) * t);
            int outer = radius - 2;
            int inner = i % minorTicks == 0 ? radius - 16 : radius - 10;

            int x1 = centerX + (int) Math.round(Math.cos(angle) * inner);
            int y1 = centerY + (int) Math.round(Math.sin(angle) * inner);
            int x2 = centerX + (int) Math.round(Math.cos(angle) * outer);
            int y2 = centerY + (int) Math.round(Math.sin(angle) * outer);

            g2.setStroke(new BasicStroke(i % minorTicks == 0 ? 2f : 1f));
            g2.drawLine(x1, y1, x2, y2);

            if (i % minorTicks == 0 && withSpeedScale) {
                double value = minValue + (maxValue - minValue) * t;
                String text = Integer.toString((int) value);
                int tx = centerX + (int) Math.round(Math.cos(angle) * (radius - 34));
                int ty = centerY + (int) Math.round(Math.sin(angle) * (radius - 34));
                drawCenteredString(g2, text, tx - 18, ty - 8, 36);
            }
        }

        double ratio = (currentValue - minValue) / (maxValue - minValue);
        ratio = Math.max(0.0, Math.min(1.0, ratio));
        double needleAngle = Math.toRadians(startAngle + (endAngle - startAngle) * ratio);
        int needleX = centerX + (int) Math.round(Math.cos(needleAngle) * (radius - 28));
        int needleY = centerY + (int) Math.round(Math.sin(needleAngle) * (radius - 28));

        g2.setColor(new Color(241, 223, 106));
        g2.setStroke(new BasicStroke(5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(centerX, centerY, needleX, needleY);

        g2.setColor(new Color(10, 18, 32));
        g2.fillOval(centerX - 34, centerY - 34, 68, 68);
        g2.setColor(new Color(38, 56, 78));
        g2.setStroke(new BasicStroke(2f));
        g2.drawOval(centerX - 34, centerY - 34, 68, 68);

        g2.setColor(new Color(234, 220, 99));
        g2.setFont(new Font("SansSerif", Font.BOLD, 20));
        drawCenteredString(g2, topLabel, centerX - 30, centerY - radius + 18, 60);
        if (bottomLabel != null) {
            g2.setFont(new Font("SansSerif", Font.BOLD, 15));
            drawCenteredString(g2, bottomLabel, centerX - 18, centerY + radius - 18, 36);
        }
    }

    private void drawCenteredString(Graphics2D g2, String text, int x, int y, int width) {
        FontMetrics metrics = g2.getFontMetrics();
        int textWidth = metrics.stringWidth(text);
        int tx = x + Math.max(0, (width - textWidth) / 2);
        g2.drawString(text, tx, y);
    }

    private String formatSpeed(double kmh) {
        return String.format(java.util.Locale.US, "%.1f km/h", kmh);
    }

    private String formatTime(double seconds) {
        return String.format(java.util.Locale.US, "%.2f s", seconds);
    }

}
