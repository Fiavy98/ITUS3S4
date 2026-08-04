package ui;

import db.EmpDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.Vector;

public class EmpPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    public EmpPanel() {
        setLayout(new BorderLayout());

        model = new DefaultTableModel();
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton refresh = new JButton("Rafraîchir");
        refresh.addActionListener(e -> loadEmp());
        add(refresh, BorderLayout.SOUTH);

        loadEmp(); // charger au démarrage
    }

    private void loadEmp() {
        try {
            Vector<Vector<Object>> data = EmpDAO.getAllEmployees();
            Vector<String> colNames = EmpDAO.getColumnNames();
            model.setDataVector(data, colNames);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(),
                    "Erreur SQL", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
