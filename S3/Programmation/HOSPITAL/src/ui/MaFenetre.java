package ui;

import javax.swing.*;
import java.awt.*;

public class MaFenetre extends JFrame {
    CardLayout cardLayout;
    Container container;
    Login login;
    Accueil acc;


    public MaFenetre() {
        setTitle("HOSPITAL");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        container = new JPanel(cardLayout);

        login = new Login(this);
        acc = new Accueil(this);

        TableOrdo tbOrd = new TableOrdo(this);
        TableLivrer tbLvr = new TableLivrer(this);
        Stock stock=new Stock(this);
        TabbleStock tbStk = new TabbleStock(this);

        container.add(login, "login");
        container.add(tbOrd,"Ordo");
        container.add(acc, "accueil");
        container.add(stock, "stock");
        container.add(tbStk,"tbStk");


        add(container);
    
        setVisible(true);
    }

    public void showPage(String name) {
        cardLayout.show(container, name);
    }


    public Accueil getAccueil() {
        return acc;
    }

}
