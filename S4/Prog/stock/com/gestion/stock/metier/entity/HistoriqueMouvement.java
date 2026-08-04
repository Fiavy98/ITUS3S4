package com.gestion.stock.metier.entity;

import com.gestion.stock.core.entity.BaseEntity;

public class HistoriqueMouvement extends BaseEntity {

    private Integer mouvementId;

    private Integer stockQte;

    private Double valeurStock;

    private Double cump;

    private String methode;

    // ========= GETTERS / SETTERS =========

    public Integer getMouvementId() {
        return mouvementId;
    }

    public void setMouvementId(Integer mouvementId) {
        this.mouvementId = mouvementId;
    }

    public Integer getStockQte() {
        return stockQte;
    }

    public void setStockQte(Integer stockQte) {
        this.stockQte = stockQte;
    }

    public Double getValeurStock() {
        return valeurStock;
    }

    public void setValeurStock(Double valeurStock) {
        this.valeurStock = valeurStock;
    }

    public Double getCump() {
        return cump;
    }

    public void setCump(Double cump) {
        this.cump = cump;
    }

    public String getMethode() {
        return methode;
    }

    public void setMethode(String methode) {
        this.methode = methode;
    }
}