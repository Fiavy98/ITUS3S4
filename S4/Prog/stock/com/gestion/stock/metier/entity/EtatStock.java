package com.gestion.stock.metier.entity;

public class EtatStock {
    private String date;
    private String produit;
    private String type;
    private String methode;
    private Integer quantite;
    private Double pu;
    private Double valeur;
    private Integer stockQte;
    private Double valeurStock;

        public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProduit() {
        return produit;
    }

    public void setProduit(String produit) {
        this.produit = produit;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMethode() {
        return methode;
    }

    public void setMethode(String methode) {
        this.methode = methode;
    }

    public Integer getQuantite() {
        return quantite;
    }


}
