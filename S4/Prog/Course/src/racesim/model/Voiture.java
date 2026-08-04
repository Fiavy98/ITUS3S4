package racesim.model;

public class Voiture {
    private final String nom;
    private final double accelerationKmHPerSec;
    private final double vitesseMaxKmH;
    private final double nitroMassKg;
    private final double nitroCapacityK;
    private final double nitroConsumptionKPerMinute;

    public Voiture(String nom, double accelerationKmHPerSec, double vitesseMaxKmH,
                   double nitroMassKg, double nitroCapacityK, double nitroConsumptionKPerMinute) {
        this.nom = nom;
        this.accelerationKmHPerSec = accelerationKmHPerSec;
        this.vitesseMaxKmH = vitesseMaxKmH;
        this.nitroMassKg = nitroMassKg;
        this.nitroCapacityK = nitroCapacityK;
        this.nitroConsumptionKPerMinute = nitroConsumptionKPerMinute;
    }

    public Voiture(String nom, double accelerationKmHPerSec, double vitesseMaxKmH) {
        this(nom, accelerationKmHPerSec, vitesseMaxKmH, 10.0, 40.0, 120.0);
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

    public double getNitroMassKg() {
        return nitroMassKg;
    }

    public double getNitroCapacityK() {
        return nitroCapacityK;
    }

    public double getNitroConsumptionKPerMinute() {
        return nitroConsumptionKPerMinute;
    }

    @Override
    public String toString() {
        return nom;
    }
}
