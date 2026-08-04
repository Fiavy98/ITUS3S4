package com.gestion.stock.metier.service;

import com.gestion.stock.metier.entity.MouvementLotSource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ResultatSortie {
    private BigDecimal prixUnitaireMoyen;
    private BigDecimal valeurTotale;
    private List<MouvementLotSource> lotsConsommes = new ArrayList<>();
    
    public ResultatSortie() {}
    
    public ResultatSortie(BigDecimal prixUnitaireMoyen, BigDecimal valeurTotale) {
        this.prixUnitaireMoyen = prixUnitaireMoyen;
        this.valeurTotale = valeurTotale;
    }
    
    // Getters et setters
    public BigDecimal getPrixUnitaireMoyen() { return prixUnitaireMoyen; }
    public void setPrixUnitaireMoyen(BigDecimal prixUnitaireMoyen) { this.prixUnitaireMoyen = prixUnitaireMoyen; }
    public BigDecimal getValeurTotale() { return valeurTotale; }
    public void setValeurTotale(BigDecimal valeurTotale) { this.valeurTotale = valeurTotale; }
    public List<MouvementLotSource> getLotsConsommes() { return lotsConsommes; }
    public void setLotsConsommes(List<MouvementLotSource> lotsConsommes) { this.lotsConsommes = lotsConsommes; }
}