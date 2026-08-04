package racesim.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import javax.swing.JPanel;
import racesim.model.Simulation;
import racesim.util.ImageAssets;

public class VuePiste extends JPanel {
    private final Simulation simulation;
    private final Image carImage;

    public VuePiste(Simulation simulation) {
        this.simulation = simulation;
        this.carImage = ImageAssets.loadCarImage();
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int margin = 54;
        int trackY = h / 2;
        int trackHeight = 84;

        g2.setPaint(new GradientPaint(0, 0, new Color(50, 50, 50), 0, h, new Color(70, 70, 70)));
        g2.fillRoundRect(0, 0, w, h, 28, 28);

        g2.setColor(new Color(100, 100, 100, 160));
        g2.fillRoundRect(margin - 20, trackY - trackHeight / 2, w - (margin * 2) + 40, trackHeight, 24, 24);

        g2.setColor(new Color(150, 150, 150));
        g2.setStroke(new BasicStroke(5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(margin, trackY, w - margin, trackY);

        g2.setColor(new Color(33, 37, 41, 30));
        g2.setStroke(new BasicStroke(12f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(margin, trackY + 9, w - margin, trackY + 9);

        g2.setColor(UiTheme.DANGER);
        g2.fillRoundRect(margin - 8, trackY - 26, 7, 52, 6, 6);
        g2.fillRoundRect(w - margin + 1, trackY - 26, 7, 52, 6, 6);

        double trackLengthM = simulation.getTrackLengthM();
        double ratio = trackLengthM > 0.0 ? simulation.getPositionM() / trackLengthM : 0.0;
        ratio = Math.max(0.0, Math.min(1.0, ratio));

        int carW = 70;
        int carH = 35;
        int maxX = w - margin - carW;
        int minX = margin;
        int carX = minX + (int) ((maxX - minX) * ratio);
        int carY = trackY - carH - 10;

        g2.drawImage(carImage, carX, carY, carW, carH, this);

        g2.setColor(new Color(18, 24, 33));
        g2.setFont(getFont().deriveFont(13f));
        g2.drawString("Depart", margin - 18, trackY + 44);
        g2.drawString(formatDistance(trackLengthM), w - margin - 18, trackY + 44);

        g2.dispose();
    }

    private String formatDistance(double distanceM) {
        if (Math.abs(distanceM - Math.round(distanceM)) < 0.001) {
            return String.format("%d m", Math.round(distanceM));
        }
        return String.format("%.1f m", distanceM);
    }
}
