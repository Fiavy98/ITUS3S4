package ui;

import javax.swing.*;

import db.*;
import acte.*;
import java.awt.*;
import java.util.Vector;

public class PanelPrincipal extends JPanel{
    JPanel insert;
    JPanel un;
    JPanel menu;
    JTextField id,nom,date,sal;
    JButton aff,crh;
     
    public PanelPrincipal(MaFenetre frame) {
        setLayout(new BorderLayout(15, 15));
            
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
        add(menu, BorderLayout.NORTH); 

        id = new JTextField();
        nom = new JTextField();
        date = new JTextField();
        sal = new JTextField();
        aff = new JButton("Modifier");

        insert = new JPanel(new GridLayout(5, 2, 10, 10));
        insert.setBorder(BorderFactory.createTitledBorder("Modifier un employé"));
        insert.add(new JLabel("Date (yyyy-MM-dd) : "));
        insert.add(date);
        insert.add(new JLabel("EmpNo : "));
        insert.add(id);
        insert.add(new JLabel("Nom : "));
        insert.add(nom);
        insert.add(new JLabel("Salaire : "));
        insert.add(sal);
        insert.add(new JLabel());
        insert.add(aff);

        aff.addActionListener(new ActionModif(date, id, nom, sal));

        un = new JPanel(new BorderLayout(10, 10));
        un.add(new EmpPanel(), BorderLayout.CENTER);
        un.add(insert, BorderLayout.SOUTH);

        JPanel tous = new JPanel(new GridLayout(1, 2, 15, 15));
        tous.add(un);

        add(menu, BorderLayout.NORTH);
        add(tous, BorderLayout.CENTER);
    }
}
