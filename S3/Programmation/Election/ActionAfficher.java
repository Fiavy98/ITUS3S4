package acte;

import objet.*;
import affichage.TablePanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class ActionAfficher implements ActionListener {
    JComboBox<Faritany> lsFaritany;
    JComboBox<Faritra> lsFaritra;
    JComboBox<Distrika> lsDistrika;
    JComboBox<Bv> lsBV;
    TablePanel table;

    public ActionAfficher(JComboBox<Faritany> lsFaritany,JComboBox<Faritra> lsFaritra,JComboBox<Distrika> lsDistrika,JComboBox<Bv> lsBV,TablePanel table){
        this.lsFaritany=lsFaritany;
        this.lsFaritra=lsFaritra;
        this.lsDistrika=lsDistrika;
        this.lsBV=lsBV;
        this.table=table;

    }

    @Override
    public void actionPerformed(ActionEvent e){ 
        Faritany FaritanySelected= (Faritany) lsFaritany.getSelectedItem();
        Faritra FaritraSelected= (Faritra) lsFaritra.getSelectedItem();
        Distrika DistrikaSelected= (Distrika) lsDistrika.getSelectedItem();
        Bv BvSelected= (Bv) lsBV.getSelectedItem();

       table.afficherVotes(FaritanySelected,FaritraSelected,DistrikaSelected,BvSelected);


    }

}