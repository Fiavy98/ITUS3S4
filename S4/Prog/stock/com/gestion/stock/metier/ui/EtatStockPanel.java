package com.gestion.stock.metier.ui;

import javax.swing.*;

import com.gestion.stock.config.DatabaseConfig;
import com.gestion.stock.core.entity.*;
import com.gestion.stock.core.ui.*;
import com.gestion.stock.metier.entity.*;
import com.gestion.stock.metier.metadata.EtatStockMetadata;
import com.gestion.stock.metier.repository.EtatStockRepository;
import com.gestion.stock.metier.repository.ProduitRepository;

import java.awt.*;
import java.sql.Connection;
import java.util.Date;
import java.util.List;

public class EtatStockPanel extends JPanel {

    private JTable table;

    private GenericTableModel<EtatStock> model;

    private EtatStockRepository repo;

    private GenericFormPanel filterForm;


    public EtatStockPanel() {

        Connection conn = DatabaseConfig.getConnection();

            repo = new EtatStockRepository(conn);

        this.repo = new EtatStockRepository(conn);

        setLayout(new BorderLayout());

        initFilter();

        initTable();

        loadData();
    }

    // ================= FILTRE =================
    private void initFilter() {

        JPanel topPanel = new JPanel(new BorderLayout());

        // Metadata du formulaire filtre
        EntityMetadata filterMeta = new EntityMetadata();

        filterMeta.setColumns(List.of(
                new ColumnDef(
                        "dateFilter",
                        "Date",
                        Date.class
                )
        ));

        filterForm = new GenericFormPanel<>(filterMeta);

        JButton btnFilter = new JButton("Filtrer");

        btnFilter.addActionListener(e -> loadData());

        topPanel.add(filterForm, BorderLayout.CENTER);
        topPanel.add(btnFilter, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
    }

    // ================= TABLE =================
    private void initTable() {

        model = new GenericTableModel<>(
                EtatStockMetadata.build(),
                List.of()
        );

        table = new JTable(model);

        JScrollPane scroll = new JScrollPane(table);

        add(scroll, BorderLayout.CENTER);
    }

    // ================= LOAD DATA =================
    private void loadData() {

        try {

            Object date = filterForm.getValue("dateFilter");

            System.out.println("Filtre date = " + date);

            // récupération données
            List<EtatStock> data = repo.findAll();

            // refresh model
            model = new GenericTableModel<>(
                    EtatStockMetadata.build(),
                    data
            );

            table.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Erreur chargement état stock"
            );
        }
    }
}