package racesim.model;

public class Voiture {
    private final String nom;
    private final double accelerationKmHPerSec;
    private final double vitesseMaxKmH;
    private final double nosse;

    public Voiture(String nom, double accelerationKmHPerSec, double vitesseMaxKmH, double nosse ) {
        this.nom = nom;
        this.accelerationKmHPerSec = accelerationKmHPerSec;
        this.vitesseMaxKmH = vitesseMaxKmH;
        this.nosse=nosse;
        this
    }

    public String getNom() {
        return nom;
    }

    public double getAccelerationKmHPerSec() {
        return accelerationKmHPerSec;
    }

    

    public double getVitesseMaxKmH() {
        return vitesseMaxKmH;
    }

    @Override
    public String toString() {
        return nom;
    }

}
