package com.gestion.stock.metier.entity;

import com.gestion.stock.core.entity.BaseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Mouvement extends BaseEntity {
    private Integer articleId;
    private LocalDate dateMouvement;
    private String typeMouvement; // ENTREE, SORTIE, AJUSTEMENT
    private BigDecimal quantite;
    private BigDecimal prixUnitaireCalcule;
    private BigDecimal valeurMouvement;
    private BigDecimal stockQteApres;
    private BigDecimal valeurStockApres;
    private BigDecimal cumpApres;
    private String methodeValorisation;
    private String sourceReference;
    private String sourceType;
    private String sourceTiers;
    private String motif;
    
    @Override
    public String getTableName() { return "mouvement"; }
    @Override
    public String getIdColumnName() { return "id"; }
    
    // Getters et setters (générés automatiquement)
    public Integer getArticleId() { return articleId; }
    public void setArticleId(Integer articleId) { this.articleId = articleId; }
    public LocalDate getDateMouvement() { return dateMouvement; }
    public void setDateMouvement(LocalDate dateMouvement) { this.dateMouvement = dateMouvement; }
    public String getTypeMouvement() { return typeMouvement; }
    public void setTypeMouvement(String typeMouvement) { this.typeMouvement = typeMouvement; }
    public BigDecimal getQuantite() { return quantite; }
    public void setQuantite(BigDecimal quantite) { this.quantite = quantite; }
    public BigDecimal getPrixUnitaireCalcule() { return prixUnitaireCalcule; }
    public void setPrixUnitaireCalcule(BigDecimal prixUnitaireCalcule) { this.prixUnitaireCalcule = prixUnitaireCalcule; }
    public BigDecimal getValeurMouvement() { return valeurMouvement; }
    public void setValeurMouvement(BigDecimal valeurMouvement) { this.valeurMouvement = valeurMouvement; }
    public BigDecimal getStockQteApres() { return stockQteApres; }
    public void setStockQteApres(BigDecimal stockQteApres) { this.stockQteApres = stockQteApres; }
    public BigDecimal getValeurStockApres() { return valeurStockApres; }
    public void setValeurStockApres(BigDecimal valeurStockApres) { this.valeurStockApres = valeurStockApres; }
    public BigDecimal getCumpApres() { return cumpApres; }
    public void setCumpApres(BigDecimal cumpApres) { this.cumpApres = cumpApres; }
    public String getMethodeValorisation() { return methodeValorisation; }
    public void setMethodeValorisation(String methodeValorisation) { this.methodeValorisation = methodeValorisation; }
    public String getSourceReference() { return sourceReference; }
    public void setSourceReference(String sourceReference) { this.sourceReference = sourceReference; }
    public String getSourceType() { return sourceType; }
    public void setSourceType(String sourceType) { this.sourceType = sourceType; }
    public String getSourceTiers() { return sourceTiers; }
    public void setSourceTiers(String sourceTiers) { this.sourceTiers = sourceTiers; }
    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }
}