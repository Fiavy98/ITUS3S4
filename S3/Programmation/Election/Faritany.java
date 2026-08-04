package objet;

import java.util.*;

public class Faritany{
    String nom;
    List<Faritra> faritra = new ArrayList<>();

    public Faritany(String nom){
        this.nom=nom;
    }
    
    public String  getNom(){
        return nom;
    }

    public void AjouteFaritra(Faritra f){
        faritra.add(f);
    }

    public List<Faritra> getfaritra(){
        return faritra;
    }

    @Override
    public String toString(){
        return nom;
    }

}