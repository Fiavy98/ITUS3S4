package objet;

import java.util.*;
public class Distrika{
    String nom;
    Faritra faritra;
    List<Bv>  bV= new ArrayList<>();

    public Distrika(String nom,Faritra faritra){
        this.nom=nom;
        this.faritra=faritra;
    }

    public String getNom(){
        return nom;
    }

    public Faritra getfaritra(){
        return faritra;
    }

    public void AjoutBv(Bv bv){
        bV.add(bv);
    }

    public List<Bv> getBvList() { return bV; }

    @Override
    public String toString(){
        return nom;
    }
}