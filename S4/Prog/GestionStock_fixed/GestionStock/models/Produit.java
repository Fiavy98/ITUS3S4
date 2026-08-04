package models;

public class Produit {

    private int id;
    private String nom;
    private MethodeValuation methodeValuation;
    private Double prixVenteDefaut;
    private String unite;

    public Produit() {}

    public Produit(int id, String nom, MethodeValuation methodeValuation,
                   Double prixVenteDefaut, String unite) {
        this.id = id;
        this.nom = nom;
        this.methodeValuation = methodeValuation;
        this.prixVenteDefaut = prixVenteDefaut;
        this.unite = unite;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public MethodeValuation getMethodeValuation() { return methodeValuation; }

    public void setMethodeValuation(MethodeValuation methodeValuation) {
        this.methodeValuation = methodeValuation;
    }

    public Double getPrixVenteDefaut() { return prixVenteDefaut; }
    public void setPrixVenteDefaut(Double prixVenteDefaut) {
        this.prixVenteDefaut = prixVenteDefaut;
    }

    public String getUnite() { return unite; }
    public void setUnite(String unite) { this.unite = unite; }

    @Override
    public String toString() {
        return nom;
    }
}