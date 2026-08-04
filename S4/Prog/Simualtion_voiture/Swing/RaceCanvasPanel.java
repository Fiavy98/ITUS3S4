package Swing;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Font;

public class RaceCanvasPanel extends JPanel {
    private final SimulationSwing simulation;

    public RaceCanvasPanel(SimulationSwing simulation) {
        this.simulation = simulation;
        setOpaque(true);
        setBackground(Color.decode("#04070d"));
        setPreferredSize(new Dimension(900, 520));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        g2.setPaint(new GradientPaint(0, 0, Color.decode("#04070d"), 0, h, Color.decode("#0a1320")));
        g2.fillRect(0, 0, w, h);

        int roadMargin = Math.max(48, w / 18);
        int roadY = (int) (h * 0.60);
        int roadHeight = Math.max(84, h / 6);
        int roadWidth = w - roadMargin * 2;

        g2.setColor(new Color(6, 12, 20));
        g2.fillRoundRect(roadMargin - 8, roadY - roadHeight / 2 - 8, roadWidth + 16, roadHeight + 16, 28, 28);

        g2.setColor(new Color(18, 29, 44));
        g2.fillRoundRect(roadMargin, roadY - roadHeight / 2, roadWidth, roadHeight, 22, 22);

        g2.setStroke(new BasicStroke(2f));
        g2.setColor(new Color(81, 101, 120));
        g2.drawRoundRect(roadMargin, roadY - roadHeight / 2, roadWidth, roadHeight, 22, 22);

        g2.setStroke(new BasicStroke(4f));
        g2.setColor(new Color(234, 220, 99));
        int dashW = 26;
        int gapW = 20;
        for (int x = roadMargin + 30; x < roadMargin + roadWidth - 30; x += dashW + gapW) {
            g2.drawLine(x, roadY, Math.min(x + dashW, roadMargin + roadWidth - 30), roadY);
        }

        g2.setColor(new Color(234, 220, 99));
        g2.fillRect(roadMargin + 8, roadY - roadHeight / 2, 8, roadHeight);
        g2.fillRect(roadMargin + roadWidth - 16, roadY - roadHeight / 2, 8, roadHeight);

        drawCar(g2, roadMargin, roadY, roadWidth);

        g2.setColor(new Color(157, 181, 199));
        g2.setFont(new Font("DejaVu Sans", Font.PLAIN, 12));
        g2.drawString("Piste droite - " + simulation.getPiste().getNom(), 20, 24);
        g2.dispose();
    }

    private void drawCar(Graphics2D g2, int roadX, int roadY, int roadWidth) {
        double progress = simulation.getPiste().getLongueur() <= 0
                ? 0
                : Math.min(1.0, simulation.getDistanceKm() / simulation.getPiste().getLongueur());

        int carWidth = Math.max(120, roadWidth / 9);
        int carHeight = Math.max(36, carWidth / 4);
        int available = Math.max(1, roadWidth - carWidth - 80);
        int carX = roadX + 40 + (int) (available * progress);
        int carY = roadY - carHeight / 2;

        g2.setColor(new Color(0, 0, 0, 80));
        g2.fillOval(carX - 6, carY + carHeight - 2, carWidth + 12, 12);

        g2.setColor(new Color(8, 17, 29));
        g2.fillRoundRect(carX, carY + 5, carWidth, carHeight - 5, 18, 18);

        g2.setColor(new Color(32, 163, 158));
        g2.fillRoundRect(carX + 8, carY, carWidth - 16, carHeight - 6, 18, 18);

        int roofW = (int) (carWidth * 0.45);
        int roofX = carX + carWidth / 3;
        int roofY = carY - carHeight / 3;
        g2.fillRoundRect(roofX, roofY, roofW, carHeight / 2 + 2, 12, 12);

        g2.setColor(new Color(7, 37, 42));
        g2.fillRoundRect(roofX + 8, roofY + 3, roofW - 16, carHeight / 2 - 4, 10, 10);

        g2.setColor(new Color(239, 244, 248));
        g2.fillRoundRect(carX + carWidth - 12, carY + 9, 10, carHeight / 3, 3, 3);
        g2.fillRoundRect(carX + 2, carY + 9, 10, carHeight / 3, 3, 3);

        int wheelD = Math.max(18, carHeight / 2);
        int wheelY = carY + carHeight - wheelD / 3;
        g2.setColor(new Color(12, 18, 25));
        g2.fillOval(carX + 14, wheelY, wheelD, wheelD);
        g2.fillOval(carX + carWidth - 14 - wheelD, wheelY, wheelD, wheelD);
        g2.setColor(new Color(81, 101, 120));
        g2.setStroke(new BasicStroke(2f));
        g2.drawOval(carX + 14, wheelY, wheelD, wheelD);
        g2.drawOval(carX + carWidth - 14 - wheelD, wheelY, wheelD, wheelD);

        g2.setColor(new Color(255, 231, 130));
        g2.fillOval(carX + carWidth - 8, carY + 8, 6, 6);
    }
}
