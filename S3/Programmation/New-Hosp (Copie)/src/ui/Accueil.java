package ui;

import java.awt.*;

import javax.swing.*;

public class Accueil extends JPanel{
    private JLabel welcome;
    JPanel menu;
    JButton acc,btnDecon,LsOrd,LsInv,stock,tabStk;


    public Accueil(MaFenetre frame) {
        setLayout(new BorderLayout());

        //========MENU======
        JPanel topPanel = new JPanel(new BorderLayout());

        welcome = new JLabel("", SwingConstants.LEFT);
        welcome.setFont(new Font("Arial", Font.BOLD, 18));

        menu = new JPanel(new FlowLayout(FlowLayout.CENTER));

        acc = new JButton("Accueil");
        acc.addActionListener(e -> {
            frame.showPage("accueil");
        });

        btnDecon = new JButton("Déconnecter");
        btnDecon.addActionListener(e -> {
            Session.destroy();
            frame.showPage("login");
        });

        LsOrd = new JButton("Liste Ordonance");
        LsOrd.addActionListener(e -> {
            frame.showPage("Ordo");
        });
        
        LsInv = new JButton("Inventaire");
        LsInv.addActionListener(e -> {
            frame.showPage("inv");
        });


        stock = new JButton("Stock");
        stock.addActionListener(e -> {
            frame.showPage("stock");
        });

        stock = new JButton("Stocker");
        stock.addActionListener(e -> {
            frame.showPage("stock");
        });

        tabStk = new JButton("Stock Etat");
        tabStk.addActionListener(e -> {
            frame.showPage("tbStk"); 
        });

        menu.add(acc);
        menu.add(LsOrd);
        menu.add(tabStk);
        menu.add(stock);
        menu.add(btnDecon);


        topPanel.add(welcome, BorderLayout.WEST);
        topPanel.add(menu, BorderLayout.CENTER);

        //============PanelClass==============
        
        add(topPanel, BorderLayout.NORTH);
        add(new PanelOrdonnance(),BorderLayout.CENTER);
    }

    public void updateWelcome() {
        welcome.setText(Session.getUsername()+" id : "+Session.getUserId());
    }
}
