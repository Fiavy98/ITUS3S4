package models;

import java.time.LocalDateTime;

public class Mouvement {

    private int id;
    private int produitId;
    private TypeMouvement type;
    private int quantite;
    private double prixUnitaire;
    private LocalDateTime date;

    // Constructeur vide
    public Mouvement() {}

    // Constructeur complet
    public Mouvement(int id, int produitId, TypeMouvement type,
                     int quantite, double prixUnitaire, LocalDateTime date) {
        this.id = id;
        this.produitId = produitId;
        this.type = type;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.date = date;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getProduitId() { return produitId; }
    public void setProduitId(int produitId) { this.produitId = produitId; }

    public TypeMouvement getType() { return type; }
    public void setType(TypeMouvement type) { this.type = type; }

    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }

    public double getPrixUnitaire() { return prixUnitaire; }
    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
}