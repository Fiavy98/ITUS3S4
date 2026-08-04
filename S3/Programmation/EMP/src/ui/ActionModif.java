package acte;

import ui.*;
import db.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.time.*;
import java.time.format.*;

/*ActionModif(date,id,nom,job,sal,dept)); {} */
public class ActionModif implements ActionListener {
    JTextField id,nom,date,sal;
    public ActionModif(JTextField date,JTextField id,JTextField nom,JTextField sal){
        this.date=date;
        this.id=id;
        this.nom=nom;
        this.sal=sal;
    }
 
    @Override
    public void actionPerformed(ActionEvent e){
        String form_id = id.getText();
        String form_nom = nom.getText();
        String form_sal = sal.getText();
        String form_date= date.getText();

        if(form_id.trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "Le champ est vide !");
            return;
        }else{
            try{
                int id = Integer.parseInt(form_id);
                double salaire = Double.parseDouble(form_sal);
                
                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date = LocalDate.parse(form_date, format);
                java.sql.Date ins_Date = java.sql.Date.valueOf(date);
                
                From_hystorique.modify(id,form_nom,ins_Date,salaire);
                
            }catch(NumberFormatException ex){
              JOptionPane.showMessageDialog(null, "Veuillez Entrer un nombre valide", "Erreur", JOptionPane.ERROR_MESSAGE);
              return;
            }
        }
        id.setText("");
        nom.setText("");
        sal.setText("");
        date.setText("");
    }
    

}