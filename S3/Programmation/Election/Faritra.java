package objet;

import java.util.*;
public class Faritra{
    String nom;
    Faritany faritany;
    List<Distrika> distrika = new ArrayList<>();

    public Faritra(String nom,Faritany faritany){
        this.nom=nom;
        this.faritany=faritany;
    }

    public String getNom(){
        return nom;
    }

    public Faritany getFaritany(){
        return faritany;
    }

    public void AjoutDistrika(Distrika d){
        distrika.add(d);
    }

    public List<Distrika> getDistrika() { return distrika; }


    @Override
    public String toString(){
        return nom;
    }
}