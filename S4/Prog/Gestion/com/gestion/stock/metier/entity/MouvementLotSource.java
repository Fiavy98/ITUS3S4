package com.gestion.stock.metier.entity;

import com.gestion.stock.core.entity.BaseEntity;

import java.math.BigDecimal;

public class MouvementLotSource extends BaseEntity {
    private Integer mouvementId;
    private Integer lotId;
    private BigDecimal quantitePrelevee;
    private BigDecimal prixUnitaireLot;
    private BigDecimal valeurPrelevee; // calculé
    private String sourceLotReference;
    private String sourceLotType;
    private Integer ordreConsommation;
    
    @Override
    public String getTableName() { return "mouvement_lot_source"; }
    @Override
    public String getIdColumnName() { return "id"; }
    
    // Getters et setters
    public Integer getMouvementId() { return mouvementId; }
    public void setMouvementId(Integer mouvementId) { this.mouvementId = mouvementId; }
    public Integer getLotId() { return lotId; }
    public void setLotId(Integer lotId) { this.lotId = lotId; }
    public BigDecimal getQuantitePrelevee() { return quantitePrelevee; }
    public void setQuantitePrelevee(BigDecimal quantitePrelevee) { this.quantitePrelevee = quantitePrelevee; }
    public BigDecimal getPrixUnitaireLot() { return prixUnitaireLot; }
    public void setPrixUnitaireLot(BigDecimal prixUnitaireLot) { this.prixUnitaireLot = prixUnitaireLot; }
    public BigDecimal getValeurPrelevee() { return valeurPrelevee; }
    public void setValeurPrelevee(BigDecimal valeurPrelevee) { this.valeurPrelevee = valeurPrelevee; }
    public String getSourceLotReference() { return sourceLotReference; }
    public void setSourceLotReference(String sourceLotReference) { this.sourceLotReference = sourceLotReference; }
    public String getSourceLotType() { return sourceLotType; }
    public void setSourceLotType(String sourceLotType) { this.sourceLotType = sourceLotType; }
    public Integer getOrdreConsommation() { return ordreConsommation; }
    public void setOrdreConsommation(Integer ordreConsommation) { this.ordreConsommation = ordreConsommation; }
}