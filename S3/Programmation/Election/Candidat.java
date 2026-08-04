package objet;

import java.util.*;
public class Candidat{
    String nom;
    int num;
    int vote;
    Bv bv;

    public Candidat(String nom,int num,Bv bv,int vote){
        this.nom=nom;
        this.num=num;
        this.bv=bv;
        this.vote=vote;
    }

    public String getNom(){
        return nom;
    }

    public int getNum(){
        return num;

    }

    public Bv getBv(){
        return bv;
    }

    public int nbVote(){
        return vote;
    }

    @Override
    public String toString(){
        return nom;
    }

}