package acte;
import ui.*;

import db.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ActionInsUser implements ActionListener{
    JTextField nom,prenom;
    JPasswordField mdp;
    JComboBox poste;

    public  ActionInsUser(JTextField nom,JTextField prenom,JPasswordField mdp,JComboBox poste){
        this.nom=nom;
        this.prenom=prenom;
        this.mdp=mdp;
        this.poste=poste;

    }

    @Override
    public void actionPerformed(ActionEvent e){
        String form_name=nom.getText();
        String form_pren=prenom.getText();
        String form_mdp=new String(mdp.getPassword());
        String form_post=(String) poste.getSelectedItem();
        String id_role=From_Role.getRoleIdByName(form_post);

        if(form_name.trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "Le champ est vide !");
            return;
        }else {
            try {
                From_Usr.insUser(form_name,form_pren,form_mdp, id_role);

                if(id_role.equals("medecin")){
                    int idUser=From_Usr.verifyUser(form_name, form_mdp);
                    From_Medecin.insMedecin(form_name,form_pren,idUser);
                }
            }catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erreur se produite : " + ex.getMessage());
            }
            
        }

        nom.setText("");
        prenom.setText("");
        mdp.setText("");

    }

}
