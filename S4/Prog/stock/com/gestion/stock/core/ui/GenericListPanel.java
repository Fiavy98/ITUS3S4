package com.gestion.stock.core.ui;

import com.gestion.stock.core.entity.*;
import com.gestion.stock.core.service.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class GenericListPanel<T> extends JPanel {
    private EntityMetadata metadata;
    private GenericService service;

    private JTable table;
    private GenericTableModel tableModel;

    private JButton btnAdd;
    private JButton btnEdit;
    private JButton btnDelete;
    private JButton btnRefresh;

    public GenericListPanel(EntityMetadata metadata,
                            GenericService<T> service) {

        this.metadata = metadata;
        this.service = service;

        setLayout(new BorderLayout());

        initComponents();
        loadData();
    }

    // initialisation de l'ui

    public void initComponents(){

        // ToolBar
        JToolBar toolBar = new JToolBar();
        
        btnAdd = new JButton("Ajouter");
        btnEdit = new JButton("Modifier");
        btnDelete = new JButton("Supprimer");
        btnRefresh = new JButton("Actualiser");

        toolBar.add(btnAdd);
        toolBar.add(btnEdit);
        toolBar.add(btnDelete);
        toolBar.add(btnRefresh);

        add(toolBar, BorderLayout.NORTH);

        // Table
        table = new JTable();
        add(new JScrollPane(table),BorderLayout.CENTER);

        // events
        btnRefresh.addActionListener(e -> loadData());
        btnAdd.addActionListener(e -> openAddForm());
        btnDelete.addActionListener(e -> deleteSelected());

    }

    public void loadData(){
        try {
            List<T> data = service.findAlll();
            tableModel = new GenericTableModel<>(metadata, data);
            table.setModel(tableModel);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Ajout
    public void openAddForm(){
        GenericFormPanel<T> form = new GenericFormPanel<>(metadata);

        int result = JOptionPane.showConfirmDialog(
            this,
            form,
            "Ajouter",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {

            Map<String, Object> values = form.getAllValues();     
            try {
                service.save(values);
            
                loadData();
            
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Erreur sauvegarde");
            }
        }
    }

    // delate
    public void deleteSelected(){
        int row = table.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Sélectionnez une ligne"
            );

            return;
        }

        JOptionPane.showMessageDialog(
                this,
                "Suppression à implémenter"
        );

    }
}
