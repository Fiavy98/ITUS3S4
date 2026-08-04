package ui;

import db.From_Emp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.Vector;


public class EmpPanel extends JPanel{
    JTable table;
    DefaultTableModel model;


    public EmpPanel(){ 
         setLayout(new BorderLayout());

         model = new DefaultTableModel();
         table = new JTable(model);
         add(new JScrollPane(table),BorderLayout.CENTER);

        JButton aff = new JButton("Afficher la liste des employer");
        aff.addActionListener(e -> load());
        add(aff,BorderLayout.SOUTH);
       // load();
     }   
     private void load(){
          try{
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