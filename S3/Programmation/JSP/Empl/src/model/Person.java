package model;

public class Person {
    private String nom;

    public Person(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public String saluer() {
        return "Bonjour, " + nom + " !";
    }
}
