package Back.modele;

public class Voiture {
    private String nom;
    private float vitesseMax;
    private float acceleration;

    public Voiture(String nom, float vitesseMax, float acceleration) {
        this.nom = nom;
        this.vitesseMax = vitesseMax;
        this.acceleration = acceleration;
    }

    public float getAcceleration() {
        return acceleration;
    }

    public String getNom() {
        return nom;
    }

    public float getVitesseMax() {
        return vitesseMax;
    }

    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setVitesseMax(float vitesseMax) {
        this.vitesseMax = vitesseMax;
    }

}
