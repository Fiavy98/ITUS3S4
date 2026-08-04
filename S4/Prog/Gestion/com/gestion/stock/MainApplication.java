package com.gestion.stock;

import com.gestion.stock.config.DatabaseConfig;
import com.gestion.stock.metier.ui.*;

import javax.swing.*;
import java.awt.*;

public class MainApplication extends JFrame {
    
    public MainApplication() {
        setTitle("Gestion de Stock Généralisée - FIFO/LIFO/CUMP");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        initUI();
    }
    
    private void initUI() {
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Panneau Gestion des articles
        tabbedPane.addTab("Articles", new ArticleFormPanel());
        
        // Panneau Saisie mouvement
        tabbedPane.addTab("Mouvements", new MouvementSaisiePanel());
        
        // Panneau État stock général
        tabbedPane.addTab("État stock", new EtatStockGeneralPanel());
        
        add(tabbedPane);
        
        // Barre de statut avec alerte stock
        JLabel statusLabel = new JLabel("Prêt");
        add(statusLabel, BorderLayout.SOUTH);
        
        // Démarrage du worker d'alerte
        startAlertWorker(statusLabel);
    }
    
    private void startAlertWorker(JLabel statusLabel) {
        Timer timer = new Timer(300000, e -> {
            // Vérifier alertes stock
            try {
                // Implémentation simple
                statusLabel.setText("Vérification alertes...");
                // TODO: appeler service d'alerte
                statusLabel.setText("Prêt");
            } catch (Exception ex) {
                statusLabel.setText("Erreur alerte");
            }
        });
        timer.start();
    }
    
    public static void main(String[] args) {
        // Initialisation de la base de données
        DatabaseConfig.initDataSource();
        
        SwingUtilities.invokeLater(() -> {
            new MainApplication().setVisible(true);
        });
    }
}