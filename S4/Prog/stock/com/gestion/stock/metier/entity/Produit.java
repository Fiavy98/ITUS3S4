package com.gestion.stock.metier.entity;

import com.gestion.stock.core.entity.BaseEntity;

public class Produit extends BaseEntity{
    private String nom;
    private String methodeValuation;
    private Double prixVenteDefaut;
    private String unite;

        public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getMethodeValuation() {
        return methodeValuation;
    }

    public void setMethodeValuation(String methodeValuation) {
        this.methodeValuation = methodeValuation;
    }

    public Double getPrixVenteDefaut() {
        return prixVenteDefaut;
    }

    public void setPrixVenteDefaut(Double prixVenteDefaut) {
        this.prixVenteDefaut = prixVenteDefaut;
    }

    public String getUnite() {
        return unite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }
}
