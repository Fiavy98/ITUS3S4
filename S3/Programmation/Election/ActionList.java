package acte;

import data.Data;
import objet.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ActionList {
    JComboBox<Faritany> lsFaritany;
    JComboBox<Faritra> lsFaritra;
    JComboBox<Distrika> lsDistrika;
    JComboBox<Bv> lsBV;
    JComboBox<Candidat> lsCandidat;

    public ActionList(JComboBox<Faritany> lsFaritany,JComboBox<Faritra> lsFaritra,JComboBox<Distrika> lsDistrika,JComboBox<Bv> lsBV,JComboBox<Candidat> lsCandidat){
    lsFaritany.addActionListener(e ->{
        Faritany FaritanySelected= (Faritany) lsFaritany.getSelectedItem();
        lsFaritra.removeAllItems();
        lsDistrika.removeAllItems();
        lsBV.removeAllItems();
        lsCandidat.removeAllItems();
        if(FaritanySelected != null){
            for(Faritra f : FaritanySelected.getfaritra()){
                lsFaritra.addItem(f);
            }
        }
    });
    lsFaritra.addActionListener(e ->{
        Faritra FaritraSelected = (Faritra) lsFaritra.getSelectedItem();
        lsDistrika.removeAllItems();
        lsBV.removeAllItems();
        lsCandidat.removeAllItems();
        if(FaritraSelected !=null){
            for(Distrika d : FaritraSelected.getDistrika()){
                lsDistrika.addItem(d);
            }
        }
    });

    lsDistrika.addActionListener(e ->{
        Distrika DistrikaSelected = (Distrika) lsDistrika.getSelectedItem();
        lsBV.removeAllItems();
        lsCandidat.removeAllItems();
        if(DistrikaSelected !=null){
            for(Bv bv : DistrikaSelected.getBvList()){
                lsBV.addItem(bv);
            }
        }
    });

    lsBV.addActionListener(e ->{
        Bv BvSelected = (Bv) lsBV.getSelectedItem();
        lsCandidat.removeAllItems();
        if(BvSelected !=null){
            for(Candidat c : BvSelected.getCandidatList()){
                lsCandidat.addItem(c);
            }
        }
    });
    }
} 