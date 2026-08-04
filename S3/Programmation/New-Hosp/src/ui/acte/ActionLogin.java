package acte;
import ui.*;
import db.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ActionLogin implements ActionListener{
    JTextField nom;
    JPasswordField mdp;
    MaFenetre frame;

    public ActionLogin(JTextField nom,JPasswordField mdp,MaFenetre frame){
        this.nom=nom;
        this.mdp=mdp;
        this.frame=frame;
    }

    @Override
    public void actionPerformed(ActionEvent e){
        String form_nom=nom.getText();
        String form_mdp=new String(mdp.getPassword());

        int idUser=From_Usr.verifyUser(form_nom, form_mdp);
    
        if(idUser!=-1){
            Session.start(idUser,form_nom);
            frame.getAccueil().updateWelcome();  
            frame.showPage("accueil");

        }else{
            JOptionPane.showMessageDialog(null, "Noo");
        }
        nom.setText("");
        mdp.setText("");
        
    }

}
