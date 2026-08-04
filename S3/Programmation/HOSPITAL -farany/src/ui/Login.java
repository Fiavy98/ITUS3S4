package ui;

import acte.*;
import javax.swing.*;

import db.*;
import java.awt.*;
import java.util.Vector;

public class Login extends JPanel {

    // Connexion
    JTextField nameLogin;
    JPasswordField mdp;  
    JButton btnLogin;

    // Formulaire 
    JTextField nameInsert,prenomIns;
    JPasswordField mdpInsert; 
    Vector<String> lsPost;
    JComboBox<String> posteCombo;
    JButton btnInsert;

    public Login(MaFenetre frame) {

        setLayout(new BorderLayout());

        JLabel titre = new JLabel("AUTHENTIFICATION");
        titre.setFont(new Font("Arial", Font.BOLD, 26));
        titre.setHorizontalAlignment(SwingConstants.CENTER);

        add(titre, BorderLayout.NORTH);

        //========Connection=======
        nameLogin = new JTextField();
        mdp = new JPasswordField();
        btnLogin = new JButton("Se connecter");

        JPanel formLogin = new JPanel(new GridLayout(3, 2, 15, 15));
        formLogin.setPreferredSize(new Dimension(400, 180));

        formLogin.add(new JLabel("Nom :"));
        formLogin.add(nameLogin);

        formLogin.add(new JLabel("Mot de passe  :"));
        formLogin.add(mdp);

        formLogin.add(new JLabel(""));
        formLogin.add(btnLogin);

        btnLogin.addActionListener(new ActionLogin(nameLogin, mdp, frame));

        JPanel centerLogin = new JPanel(new GridBagLayout());
        centerLogin.add(formLogin);

        JSeparator sep = new JSeparator();
        sep.setPreferredSize(new Dimension(400, 1));

        JPanel sepPanel = new JPanel();
        sepPanel.add(sep);

        //========Inscription============

        nameInsert = new JTextField();
        prenomIns=new JTextField();
        mdpInsert = new JPasswordField(); 

        lsPost = From_Role.lsRole();
        posteCombo = new JComboBox<>(lsPost);

        btnInsert = new JButton("S'inscrire");
        
        JPanel formInsert = new JPanel(new GridLayout(4, 2, 15, 15));
        formInsert.setPreferredSize(new Dimension(400, 180));
        
        formInsert.add(new JLabel("Nom :"));
        formInsert.add(nameInsert);
        
        formInsert.add(new JLabel("Prénom :"));
        formInsert.add(prenomIns);
        
        formInsert.add(new JLabel("Mot de passe :"));
        formInsert.add(mdpInsert);
        
        formInsert.add(new JLabel("Poste :"));
        formInsert.add(posteCombo);
        
        JPanel btnPanel = new JPanel();
        btnPanel.add(btnInsert);
        
        btnInsert.addActionListener(new ActionInsUser(nameInsert, prenomIns, mdpInsert, posteCombo));
        
        JPanel centerInsert = new JPanel(new GridBagLayout());
        centerInsert.add(formInsert);
        centerInsert.add(Box.createVerticalStrut(10));
        centerInsert.add(btnPanel);

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        container.add(Box.createVerticalStrut(20));
        container.add(centerLogin);
        container.add(Box.createVerticalStrut(10));
        container.add(sepPanel);
        container.add(Box.createVerticalStrut(10));
        container.add(centerInsert);

        add(container, BorderLayout.CENTER);
    }
}

