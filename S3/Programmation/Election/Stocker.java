package data;

import objet.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Stocker{
    List<Faritany> lsFaritany = new ArrayList<>();
    List<Faritra> lsFaritra = new ArrayList<>();
    List<Distrika> lsDistrika = new ArrayList<>();
    List<Bv> lsBv = new ArrayList<>();
    List<Candidat> lsCandidat = new ArrayList<>();
    List<Vote> lsvote = new ArrayList<>();

    public void AjouterFaritany(Faritany f){
        lsFaritany.add(f);
    }

    public void AjouterFaritra(Faritra faritra){
        lsFaritra.add(faritra);
    }

    public void AjouterDistrika(Distrika ds){
        lsDistrika.add(ds);
    }

    public void AjouterBv(Bv bv){
        lsBv.add(bv);
    }

    public void AjouterCandidat(Candidat c){
        lsCandidat.add(c);
    }

    public void AjouterVote(Vote v){
        lsvote.add(v);
    }
}