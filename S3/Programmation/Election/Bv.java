package objet;

import java.util.*;

public class Bv{
    String nom;
    Distrika distrika;
    List<Candidat> candidatList = new ArrayList<>();

    public Bv(String nom,Distrika distrika){
        this.nom=nom;
        this.distrika=distrika;
    }

    public String getNom(){
        return nom;
    }

    public Distrika getDistrika(){
        return distrika;
    }

    public void AjoutCandidat(Candidat c){
        candidatList.add(c);
    }

    public List<Candidat> getCandidatList() { return candidatList; }


    @Override
    public String toString(){
        return nom;
    }
}