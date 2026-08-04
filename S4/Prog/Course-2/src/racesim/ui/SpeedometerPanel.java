package racesim.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import javax.swing.JPanel;
import racesim.util.ImageAssets;

public class SpeedometerPanel extends JPanel {
    private double speedKmh;
    private double maxSpeedKmh;
    private final Image background;

    public SpeedometerPanel(double maxSpeedKmh) {
        this.maxSpeedKmh = maxSpeedKmh;
        this.background = ImageAssets.loadSpeedometerImage();
        setPreferredSize(new Dimension(320, 320));
        setOpaque(false);
    }

    public void setSpeedKmh(double speedKmh) {
        this.speedKmh = speedKmh;
        repaint();
    }

    public void setMaxSpeedKmh(double maxSpeedKmh) {
        this.maxSpeedKmh = maxSpeedKmh;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int size = Math.min(w, h);
        int x = (w - size) / 2;
        int y = (h - size) / 2;

        if (background != null) {
            g2.drawImage(background, x, y, size, size, this);
        }

        g2.setColor(new Color(255, 255, 255, 200));
        g2.fillOval(x + 10, y + 10, size - 20, size - 20);

        drawTicksAndLabels(g2, w, h, size);

        double ratio = maxSpeedKmh > 0.0 ? speedKmh / maxSpeedKmh : 0.0;
        ratio = Math.max(0.0, Math.min(1.0, ratio));

        double startDeg = -120.0;
        double sweepDeg = 240.0;
        double angleRad = Math.toRadians(startDeg + ratio * sweepDeg);

        int cx = w / 2;
        int cy = h / 2;
        int r = size / 2 - 16;

        int x2 = cx + (int) (Math.cos(angleRad) * r);
        int y2 = cy + (int) (Math.sin(angleRad) * r);

        g2.setStroke(new BasicStroke(3f));
        g2.setColor(UiTheme.ACCENT_DARK);
        g2.drawLine(cx, cy, x2, y2);
        g2.fillOval(cx - 5, cy - 5, 10, 10);

        String speedText = String.format("%.0f", speedKmh);
        String unitText = "km/h";
        g2.setFont(getFont().deriveFont(26f));
        int speedWidth = g2.getFontMetrics().stringWidth(speedText);
        int speedHeight = g2.getFontMetrics().getAscent();
        g2.setColor(UiTheme.TEXT);
        g2.drawString(speedText, cx - speedWidth / 2, cy + speedHeight / 3);
        g2.setFont(getFont().deriveFont(12f));
        int unitWidth = g2.getFontMetrics().stringWidth(unitText);
        g2.setColor(UiTheme.MUTED);
        g2.drawString(unitText, cx - unitWidth / 2, cy + speedHeight / 3 + 18);

        g2.dispose();
    }

    private void drawTicksAndLabels(Graphics2D g2, int w, int h, int size) {
        int cx = w / 2;
        int cy = h / 2;
        int rOuter = size / 2 - 10;
        int rInner = rOuter - 12;

        g2.setColor(UiTheme.MUTED);
        g2.setStroke(new BasicStroke(2f));

        int tickCount = 10;
        double startDeg = -120.0;
        double sweepDeg = 240.0;
        for (int i = 0; i <= tickCount; i++) {
            double ratio = (double) i / tickCount;
            double angleRad = Math.toRadians(startDeg + ratio * sweepDeg);
            int x1 = cx + (int) (Math.cos(angleRad) * rInner);
            int y1 = cy + (int) (Math.sin(angleRad) * rInner);
            int x2 = cx + (int) (Math.cos(angleRad) * rOuter);
            int y2 = cy + (int) (Math.sin(angleRad) * rOuter);
            g2.drawLine(x1, y1, x2, y2);
        }

        g2.setFont(getFont().deriveFont(13f));
        for (int i = 0; i <= 4; i++) {
            double ratio = (double) i / 4.0;
            double value = maxSpeedKmh * ratio;
            double angleRad = Math.toRadians(startDeg + ratio * sweepDeg);
            int labelR = rInner - 12;
            int lx = cx + (int) (Math.cos(angleRad) * labelR);
            int ly = cy + (int) (Math.sin(angleRad) * labelR);
            String label = String.format("%.0f", value);
            int textW = g2.getFontMetrics().stringWidth(label);
            int textH = g2.getFontMetrics().getAscent();
            g2.setColor(UiTheme.TEXT);
            g2.drawString(label, lx - textW / 2, ly + textH / 2);
        }
    }
}
