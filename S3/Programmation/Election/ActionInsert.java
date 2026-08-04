package acte;

import objet.*;
import data.*;
import affichage.TablePanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class ActionInsert implements ActionListener {
    JComboBox<Faritany> lsFaritany;
    JComboBox<Faritra> lsFaritra;
    JComboBox<Distrika> lsDistrika;
    JComboBox<Bv> lsBV;
    JComboBox<Candidat> lsCandidat;
    JTextField vote;
    JButton bottAj;
    Stocker stock;

    public ActionInsert(JComboBox<Faritany> lsFaritany,JComboBox<Faritra> lsFaritra,JComboBox<Distrika> lsDistrika,JComboBox<Bv> lsBV,JComboBox<Candidat> lsCandidat,JTextField vote,JButton aj,Stocker stock){
        this.lsFaritany=lsFaritany;
        this.lsFaritra=lsFaritra;
        this.lsDistrika=lsDistrika;
        this.lsBV=lsBV;
        this.lsCandidat=lsCandidat;
        this.vote=vote;
        this.bottAj=bottAj;
        this.stock=stock;
    }

    @Override
    public void actionPerformed(ActionEvent e){ 
        Faritany FaritanySelected= (Faritany) lsFaritany.getSelectedItem();
        Faritra FaritraSelected= (Faritra) lsFaritra.getSelectedItem();
        Distrika DistrikaSelected= (Distrika) lsDistrika.getSelectedItem();
        Bv BvSelected= (Bv) lsBV.getSelectedItem();
        Candidat CandidatSelected= (Candidat) lsCandidat.getSelectedItem();
        String ent_vote= vote.getText();
         
        if(ent_vote.trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "Le champ est vide !");
            return;
        }else{
            try{
                
                int nbvote = Integer.parseInt(ent_vote);
                Vote vote= new Vote(nbvote,CandidatSelected);
               stock.AjouterFaritany(FaritanySelected);
               stock.AjouterFaritra(FaritraSelected);
               stock.AjouterDistrika(DistrikaSelected);
               stock.AjouterBv(BvSelected);
               stock.AjouterCandidat(CandidatSelected);
               stock.AjouterVote(vote);

                JOptionPane.showMessageDialog(null, "Vote ajouté avec succès !");
            }
            catch(NumberFormatException ex){
              JOptionPane.showMessageDialog(null, "Veuillez Entrer un nombre valide", "Erreur", JOptionPane.ERROR_MESSAGE);
              return;
            }
        }

        vote.setText("");

    }

}