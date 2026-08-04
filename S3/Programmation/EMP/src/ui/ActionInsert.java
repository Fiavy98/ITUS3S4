package acte;

import ui.*;
import db.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.time.*;
import java.time.format.*;

public class ActionInsert implements ActionListener{
    JTextField id,nom,job,sal,date;
    JComboBox<String> dept;
    public ActionInsert(JTextField date,JTextField id,JTextField nom,JTextField job,JTextField sal,JComboBox<String> dept){
        this.date=date;
        this.id=id;
        this.nom=nom;
        this.job=job;
        this.sal=sal;
        this.dept=dept;
    }

    @Override
    public void actionPerformed(ActionEvent e){
        String form_id = id.getText();
        String form_nom = nom.getText();
        String form_job = job.getText();
        String form_sal = sal.getText();
        String form_date= date.getText();
        String form_dept = (String) dept.getSelectedItem();

        if(form_id.trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "Le champ est vide !");
            return;
        }else{
            try{
                int id = Integer.parseInt(form_id);
                double salaire = Double.parseDouble(form_sal);
                int deptNO = From_dept.getIdDept(form_dept);
                
                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date = LocalDate.parse(form_date, format);
                java.sql.Date ins_Date = java.sql.Date.valueOf(date);
                From_Emp.insEmp(id, form_nom, form_job, salaire, ins_Date, deptNO);
                JOptionPane.showMessageDialog(null, "Employe ajoute !");

            } 
            catch(NumberFormatException ex){
                JOptionPane.showMessageDialog(null, "Veuillez Entrer un nombre valide", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

        }

        id.setText("");
        nom.setText("");
        job.setText("");
        sal.setText("");
        date.setText("");
    }

}  
