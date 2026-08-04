package model;

public class Voiture {

    private int id;
    private int idType;      // nouvel attribut : id du type de voiture
    private String nomType;  // nouvel attribut : nom du type de voiture (Goudron, Pavée, etc.)
    private double vMax;
    private double vMin;
    private double logueur;
    private double largeur;

    // --- Constructeur vide ---
    public Voiture() {}

    // --- Getters et Setters ---
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdType() {
        return idType;
    }

    public void setIdType(int idType) {
        this.idType = idType;
    }

    public String getNomType() {
        return nomType;
    }

    public void setNomType(String nomType) {
        this.nomType = nomType;
    }

    public double getVMax() {
        return vMax;
    }

    public void setVMax(double vMax) {
        this.vMax = vMax;
    }

    public double getVMin() {
        return vMin;
    }

    public void setVMin(double vMin) {
        this.vMin = vMin;
    }

    public double getLogueur() {
        return logueur;
    }

    public void setLogueur(double logueur) {
        this.logueur = logueur;
    }

    public double getLargeur() {
        return largeur;
    }

    public void setLargeur(double largeur) {
        this.largeur = largeur;
    }

    // --- Pour l'affichage dans JComboBox ---
    @Override
    public String toString() {
        return nomType + " (id=" + id + ")";
    }
}
