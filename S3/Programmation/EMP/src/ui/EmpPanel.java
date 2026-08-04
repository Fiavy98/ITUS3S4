package ui;

import db.From_Emp;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.Vector;


class EmpPanel extends JPanel {
    JTable table;
    DefaultTableModel model;

    public EmpPanel() {
        setLayout(new BorderLayout(10, 10));

        model = new DefaultTableModel();
        table = new JTable(model);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder("Liste des Employés"));
        add(scroll, BorderLayout.CENTER);

        JButton aff = new JButton("Actualiser");
        JPanel btnPanel = new JPanel();
        btnPanel.add(aff);
        add(btnPanel, BorderLayout.SOUTH);

        aff.addActionListener(e -> load());
        load();
    }

    private void load() {
        try {
            Vector<Vector<Object>> data = From_Emp.lsEmp();
            Vector<String> colonne = From_Emp.getColumnNames();
            model.setDataVector(data, colonne);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(),
                    "Erreur SQL", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}