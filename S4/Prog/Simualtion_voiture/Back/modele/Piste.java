package Back.modele;

public class Piste {
    private float longueur;
    private String nom;

    public Piste(float longueur, String nom) {
        this.longueur = longueur;
        this.nom = nom;
    }

    public float getLongueur() {
        return longueur;
    }

    public String getNom() {
        return nom;
    }

    public void setLongueur(float longueur) {
        this.longueur = longueur;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

}
