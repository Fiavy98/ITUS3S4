package ui;

import db.DBConnection;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MapApp extends JFrame {

    private JMapViewer mapViewer;

    public MapApp() {
        setTitle("Projet SIG - Madagascar");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
    }

    private void initUI() {
        // 1. Initialisation de la carte (JMapViewer)
        mapViewer = new JMapViewer();
        
        // Centrer la carte sur Madagascar par défaut
        mapViewer.setDisplayPosition(new Coordinate(-18.8792, 47.5079), 6);

        // 2. Création du panneau de contrôle (Boutons)
        JPanel controlPanel = new JPanel();
        JButton btnLoadData = new JButton("Charger Points PostGIS");
        JButton btnClear = new JButton("Effacer");

        // Action pour charger les données
        btnLoadData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadPostGISData();
            }
        });

        // Action pour effacer la carte
        btnClear.addActionListener(e -> mapViewer.removeAllMapMarkers());

        controlPanel.add(btnLoadData);
        controlPanel.add(btnClear);

        // 3. Ajout des composants à la fenêtre
        setLayout(new BorderLayout());
        add(mapViewer, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    /**
     * Connecte à la BDD, récupère les géométries et les affiche sur la carte.
     */
    private void loadPostGISData() {
        // Requête SQL pour récupérer le nom et les coordonnées X/Y
        // ST_X correspond à la longitude, ST_Y à la latitude
        String sql = "SELECT nom, ST_X(geom) as lon, ST_Y(geom) as lat FROM points_interet";

        List<MapMarker> markers = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            int count = 0;
            while (rs.next()) {
                String nom = rs.getString("nom");
                double lon = rs.getDouble("lon");
                double lat = rs.getDouble("lat");

                // Création d'un marqueur (Point) sur la carte
                // MapMarkerDot est une classe simple fournie par JMapViewer
                MapMarkerDot marker = new MapMarkerDot(lat, lon);
                marker.setName(nom); // Le nom s'affichera au survol (selon config)
                markers.add(marker);
                
                count++;
            }

            // Ajout des marqueurs à la carte
            for (MapMarker m : markers) {
                mapViewer.addMapMarker(m);
            }

            JOptionPane.showMessageDialog(this, count + " points chargés depuis PostGIS !");

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur SQL : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // Lancer l'interface dans le thread Swing
        SwingUtilities.invokeLater(() -> {
            MapApp app = new MapApp();
            app.setVisible(true);
        });
    }
}