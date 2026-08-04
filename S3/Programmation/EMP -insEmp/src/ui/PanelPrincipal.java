package ui;

import javax.swing.*;

import db.*;
import acte.*;
import java.awt.*;
import java.util.Vector;

public class PanelPrincipal extends JPanel{
    JPanel insert;
    JPanel cherche;
    JTextField id,nom,job,sal,date;
    Vector<String> lsdept;
    JComboBox<String> dept;
    JButton aff,crh;

    JTextField hst_id,hst_nom,hst_sal,hst_date;

    public PanelPrincipal(){
        setLayout(new BorderLayout());

        id=new JTextField();
        nom=new JTextField();
        date=new JTextField();
        job=new JTextField();
        sal=new JTextField();

        lsdept= From_dept.lsDept();

        dept = new JComboBox<>(lsdept);
        aff=new JButton("Valider");

        insert = new JPanel(new GridLayout(8, 2, 5, 5));
        insert.add(new JLabel("Date (yyyy-MM-dd) : "));
        insert.add(date);
        insert.add(new JLabel("EmpNo : "));
        insert.add(id);
        insert.add(new JLabel("Nom: "));
        insert.add(nom);
        insert.add(new JLabel("Job : "));
        insert.add(job);   
        insert.add(new JLabel("salaire "));
        insert.add(sal);
        insert.add(new JLabel("Departement : "));
        insert.add(dept);  
        insert.add(aff);

        aff.addActionListener(new ActionInsert(date,id,nom,job,sal,dept));
        hst_id=new JTextField();
        hst_nom=new JTextField();
        hst_date=new JTextField();
        hst_sal=new JTextField();
        crh=new JButton("Modifier");


        cherche = new JPanel(new GridLayout(6, 12, 8, 8));
        cherche.add(new JLabel("Date (yyyy-MM-dd) : "));
        cherche.add(hst_date);
        cherche.add(new JLabel("EmpNo : "));
        cherche.add(hst_id);
        cherche.add(new JLabel("Nom: "));
        cherche.add(hst_nom);
        cherche.add(new JLabel("Salaire : "));
        cherche.add(hst_sal);   
         cherche.add(crh);



        add(insert,BorderLayout.WEST);
        add(cherche,BorderLayout.EAST);
        add(new EmpPanel(),BorderLayout.SOUTH);




    }
    
}
