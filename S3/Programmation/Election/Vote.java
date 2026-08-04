package objet;

import java.util.*;
public class Vote{
    int vote;
    Candidat c;

    public Vote(int vote, Candidat c){
        this.vote=vote;
        this.c=c;
    }
    public int getVote(){
        return vote;

    }

    public Candidat getCandidat(){
        return c;
    }

}