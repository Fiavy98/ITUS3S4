package ui;

import db.OracleConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;


public class EmpViewer extends JFrame {
    private JTable table;
    private DefaultTableModel model;

    public EmpViewer() {
        super("Liste des Employés (EMP)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        model = new DefaultTableModel();
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton refresh = new JButton("Rafraîchir");
        refresh.addActionListener(e -> loadEmp());
        add(refresh, BorderLayout.SOUTH);
    }

    private void loadEmp() {
        String sql = "SELECT empno, ename, job, sal, deptno FROM emp";

        try (Connection conn = OracleConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            ResultSetMetaData meta = rs.getMetaData();
            int colCount = meta.getColumnCount();

            Vector<String> colNames = new Vector<>();
            for (int i = 1; i <= colCount; i++) {
                colNames.add(meta.getColumnName(i));
            }

            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                for (int i = 1; i <= colCount; i++) {
                    row.add(rs.getObject(i));
                }
                data.add(row);
            }

            model.setDataVector(data, colNames);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
