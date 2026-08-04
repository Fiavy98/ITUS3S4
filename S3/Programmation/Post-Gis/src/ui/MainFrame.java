package ui;

import dao.VilleDAO;
import model.Ville;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.DefaultMapController;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.List;

public class MainFrame extends JFrame {

    private JMapViewer map;

    public MainFrame() {
        setTitle("SIG Madagascar");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        map = new JMapViewer();
        add(map, BorderLayout.CENTER);

        // Ajout du contrôleur pour activer le zoom et le déplacement (pan) avec la souris
        new DefaultMapController(map);

        // Zoom et centre par défaut
        map.setDisplayPosition(new Coordinate(-18.8792, 47.5079), 6);

        // Charger les villes depuis PostGIS
        loadVilles();

        // Écouter le curseur pour afficher lat/lon
        map.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Coordinate coord = map.getPosition(e.getPoint());
                System.out.printf("Curseur → Latitude: %.6f, Longitude: %.6f%n", coord.getLat(), coord.getLon());

                // Tooltip si proche d’un marqueur
                boolean overMarker = false;
                for (MapMarkerDot marker : map.getMapMarkerList()) {
                    Point markerPoint = map.getMapPosition(marker.getCoordinate(), false);
                    if (markerPoint != null && markerPoint.distance(e.getPoint()) < 10) {
                        map.setToolTipText(marker.getName());
                        overMarker = true;
                        break;
                    }
                }
                if (!overMarker) {
                    map.setToolTipText(null);
                }
            }
        });

        // Détecter clic sur un marqueur
        map.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (MapMarkerDot marker : map.getMapMarkerList()) {
                    Point markerPoint = map.getMapPosition(marker.getCoordinate(), false);
                    if (markerPoint != null && markerPoint.distance(e.getPoint()) < 10) {
                        JOptionPane.showMessageDialog(map, "Vous avez cliqué sur : " + marker.getName());
                        return;
                    }
                }
            }
        });
    }

    private void loadVilles() {
        List<Ville> villes = VilleDAO.getAllVilles();
        for (Ville ville : villes) {
            Coordinate coord = new Coordinate(ville.getLatitude(), ville.getLongitude());
            MapMarkerDot marker = new MapMarkerDot(coord);
            marker.setName(ville.getNom());
            map.addMapMarker(marker);
        }

        // Zoom automatique sur toutes les villes
        if (!villes.isEmpty()) {
            double minLat = villes.stream().mapToDouble(Ville::getLatitude).min().getAsDouble();
            double maxLat = villes.stream().mapToDouble(Ville::getLatitude).max().getAsDouble();
            double minLon = villes.stream().mapToDouble(Ville::getLongitude).min().getAsDouble();
            double maxLon = villes.stream().mapToDouble(Ville::getLongitude).max().getAsDouble();

            Coordinate center = new Coordinate((minLat + maxLat) / 2, (minLon + maxLon) / 2);
            map.setDisplayPosition(center, 6);
        }
    }
}
