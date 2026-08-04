package com.gestion.stock.metier.entity;

import java.math.BigDecimal;

import com.gestion.stock.core.entity.BaseEntity;

public class Article extends BaseEntity {
    private Integer categorieId;
    private String code;
    private String uniteMesure;
    private String methodeGestion = "CUMP";
    private BigDecimal stockActuel = BigDecimal.ZERO;
    private BigDecimal valeurStockActuelle = BigDecimal.ZERO;
    private BigDecimal cumpActuel = BigDecimal.ZERO;
    private BigDecimal stockReserve = BigDecimal.ZERO;
    private boolean actif = true;
    
    // Champs de jointure (non persistés directement)
    private String categorieLibelle;
    
    @Override
    public String getTableName() { return "article"; }
    @Override
    public String getIdColumnName() { return "id"; }
    
    // Getters et setters
    public Integer getCategorieId() { return categorieId; }
    public void setCategorieId(Integer categorieId) { this.categorieId = categorieId; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getUniteMesure() { return uniteMesure; }
    public void setUniteMesure(String uniteMesure) { this.uniteMesure = uniteMesure; }
    public String getMethodeGestion() { return methodeGestion; }
    public void setMethodeGestion(String methodeGestion) { this.methodeGestion = methodeGestion; }
    public BigDecimal getStockActuel() { return stockActuel; }
    public void setStockActuel(BigDecimal stockActuel) { this.stockActuel = stockActuel; }
    public BigDecimal getValeurStockActuelle() { return valeurStockActuelle; }
    public void setValeurStockActuelle(BigDecimal valeurStockActuelle) { this.valeurStockActuelle = valeurStockActuelle; }
    public BigDecimal getCumpActuel() { return cumpActuel; }
    public void setCumpActuel(BigDecimal cumpActuel) { this.cumpActuel = cumpActuel; }
    public BigDecimal getStockReserve() { return stockReserve; }
    public void setStockReserve(BigDecimal stockReserve) { this.stockReserve = stockReserve; }
    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }
    public String getCategorieLibelle() { return categorieLibelle; }
    public void setCategorieLibelle(String categorieLibelle) { this.categorieLibelle = categorieLibelle; }
    
    @Override
    public String toString() {
        return code;
    }
}