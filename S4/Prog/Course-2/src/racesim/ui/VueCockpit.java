package racesim.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import racesim.model.Simulation;

public class VueCockpit extends JPanel {
    private final SpeedometerPanel speedometer;
    private final JLabel speedLabel;
    private final JLabel arrivalSpeedLabel;
    private final JButton accelButton;

    public VueCockpit(Simulation simulation) {
        setLayout(new BorderLayout(12, 12));
        setOpaque(false);

        speedometer = new SpeedometerPanel(simulation.getVitesseMaxKmH());
        speedLabel = new JLabel("Vitesse: 0 km/h");
        speedLabel.setFont(UiTheme.VALUE);
        speedLabel.setForeground(UiTheme.TEXT);

        arrivalSpeedLabel = new JLabel("Arrivee: -- km/h");
        arrivalSpeedLabel.setFont(UiTheme.SECTION);
        arrivalSpeedLabel.setForeground(UiTheme.ACCENT_DARK);

        accelButton = new JButton("Accelerer");
        UiTheme.styleButton(accelButton, UiTheme.ACCENT, Color.WHITE);
        accelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                simulation.setAccelerating(true);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                simulation.setAccelerating(false);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                simulation.setAccelerating(false);
            }
        });

        JPanel infoCard = (JPanel) UiTheme.createCardPanel();
        infoCard.setLayout(new BorderLayout(0, 8));

        JLabel infoTitle = new JLabel("Pilotage");
        infoTitle.setFont(UiTheme.SECTION);
        infoTitle.setForeground(UiTheme.TEXT);

        JPanel values = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 0));
        values.setOpaque(false);
        values.add(speedLabel);
        values.add(arrivalSpeedLabel);
        values.add(accelButton);

        infoCard.add(infoTitle, BorderLayout.NORTH);
        infoCard.add(values, BorderLayout.CENTER);

        add(speedometer, BorderLayout.CENTER);
        add(infoCard, BorderLayout.SOUTH);
    }

    public void setSpeedKmh(double speedKmh) {
        speedometer.setSpeedKmh(speedKmh);
        speedLabel.setText(String.format("Vitesse: %.0f km/h", speedKmh));
    }

    public void setArrivalSpeedKmh(Double speedKmh) {
        if (speedKmh == null) {
            arrivalSpeedLabel.setText("Arrivee: -- km/h");
            return;
        }
        arrivalSpeedLabel.setText(String.format("Arrivee: %.0f km/h", speedKmh));
    }

    public void setAccelerationEnabled(boolean enabled) {
        accelButton.setEnabled(enabled);
    }
}
