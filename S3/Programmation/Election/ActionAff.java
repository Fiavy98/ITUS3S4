package acte;

import data.Data;
import objet.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ActionAff {
    JComboBox<Faritany> lsFaritany;
    JComboBox<Faritra> lsFaritra;
    JComboBox<Distrika> lsDistrika;
    JComboBox<Bv> lsBV;

    public ActionAff(JComboBox<Faritany> lsFaritany,JComboBox<Faritra> lsFaritra,JComboBox<Distrika> lsDistrika,JComboBox<Bv> lsBV){
    lsFaritany.addActionListener(e ->{
        Faritany FaritanySelected= (Faritany) lsFaritany.getSelectedItem();
        lsFaritra.removeAllItems();
        lsDistrika.removeAllItems();
        lsBV.removeAllItems();
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
        if(FaritraSelected !=null){
            for(Distrika d : FaritraSelected.getDistrika()){
                lsDistrika.addItem(d);
            }
        }
    });

    lsDistrika.addActionListener(e ->{
        Distrika DistrikaSelected = (Distrika) lsDistrika.getSelectedItem();
        lsBV.removeAllItems();
        if(DistrikaSelected !=null){
            for(Bv bv : DistrikaSelected.getBvList()){
                lsBV.addItem(bv);
            }
        }
    });

    }
} 