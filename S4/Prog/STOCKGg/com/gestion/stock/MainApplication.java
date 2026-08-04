package com.gestion.stock;

import com.gestion.stock.config.DatabaseConfig;
import com.gestion.stock.metier.ui.*;

import javax.swing.*;
import java.awt.*;

public class MainApplication extends JFrame {

    public MainApplication() {
        setTitle("Gestion de Stock Generale - FIFO/LIFO/CUMP");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        initUI();
    }

    private void initUI() {
        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Articles", new ArticleFormPanel());
        tabbedPane.addTab("Mouvements", new MouvementSaisiePanel());
        tabbedPane.addTab("Historique mouvements", new HistoriqueMouvementsPanel());
        tabbedPane.addTab("Etat stock", new EtatStockGeneralPanel());
        tabbedPane.addTab("Écritures Comptables", new EcritureTablePanel());

        add(tabbedPane);

        JLabel statusLabel = new JLabel("Pret");
        add(statusLabel, BorderLayout.SOUTH);

        startAlertWorker(statusLabel);
    }

    private void startAlertWorker(JLabel statusLabel) {
        Timer timer = new Timer(300000, e -> {
            try {
                statusLabel.setText("Verification alertes...");
                statusLabel.setText("Pret");
            } catch (Exception ex) {
                statusLabel.setText("Erreur alerte");
            }
        });
        timer.start();
    }

    public static void main(String[] args) {
        DatabaseConfig.initDataSource();

        SwingUtilities.invokeLater(() -> {
            new MainApplication().setVisible(true);
        });
    }
}
