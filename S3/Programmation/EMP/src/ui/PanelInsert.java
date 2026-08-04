package ui;

import db.*;
import acte.*;
import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class PanelInsert extends JPanel{
    JPanel menu;
    JPanel form;
    JTextField id,nom,job,sal,date;
    Vector<String> lsdept;
    JComboBox<String> dept;
    JButton ajt;

    public PanelInsert(MaFenetre frame) {
      setLayout(new BorderLayout());
      menu = new JPanel();

      JButton acc = new JButton("Accueil");
      acc.addActionListener(e -> frame.changePanel("principale"));
      menu.add(acc);

      JButton hist = new JButton("Historique");
      hist.addActionListener(e -> frame.changePanel("secondaire"));
      menu.add(hist);
      add(menu, BorderLayout.NORTH);

      JButton ins = new JButton("Inserer");
      ins.addActionListener(e -> frame.changePanel("insert"));
      menu.add(ins);

      lsdept= From_dept.lsDept();
      dept = new JComboBox<>(lsdept);
      ajt=new JButton("Valider");


        id=new JTextField();
        nom=new JTextField();
        date=new JTextField();
        job=new JTextField();
        sal=new JTextField();

      form = new JPanel(new GridLayout(8, 2, 5, 5));  
      form.add(new JLabel("Date (yyyy-MM-dd) : "));
      form.add(date);
      form.add(new JLabel("EmpNo : "));
      form.add(id);
      form.add(new JLabel("Nom: "));
      form.add(nom);
      form.add(new JLabel("Job : "));
      form.add(job);   
      form.add(new JLabel("salaire "));
      form.add(sal);
      form.add(new JLabel("Departement : "));
      form.add(dept);  
      form.add(ajt);

      ajt.addActionListener(new ActionInsert(date,id,nom,job,sal,dept));



      add(menu, BorderLayout.NORTH);
      add(form,BorderLayout.CENTER);


        
    }
}
