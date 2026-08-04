package com.gestion.stock.metier.entity;

import com.gestion.stock.core.entity.BaseEntity;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Lot extends BaseEntity {

    private Integer articleId;
    private Integer mouvementEntreeId;
    private LocalDate dateEntree;
    private BigDecimal quantiteInitiale;
    private BigDecimal quantiteRestante;
    private BigDecimal prixUnitaire;
    private BigDecimal valeurRestante; // calculé
    private boolean epuise;
    private String sourceReference;
    private String sourceType;
    private String sourceDocument;

    @Override
    public String getTableName() {
        return "lot";
    }

    @Override
    public String getIdColumnName() {
        return "id";
    }

    // Getters et setters
    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public Integer getMouvementEntreeId() {
        return mouvementEntreeId;
    }

    public void setMouvementEntreeId(Integer mouvementEntreeId) {
        this.mouvementEntreeId = mouvementEntreeId;
    }

    public LocalDate getDateEntree() {
        return dateEntree;
    }

    public void setDateEntree(LocalDate dateEntree) {
        this.dateEntree = dateEntree;
    }

    public BigDecimal getQuantiteInitiale() {
        return quantiteInitiale;
    }

    public void setQuantiteInitiale(BigDecimal quantiteInitiale) {
        this.quantiteInitiale = quantiteInitiale;
    }

    public BigDecimal getQuantiteRestante() {
        return quantiteRestante;
    }

    public void setQuantiteRestante(BigDecimal quantiteRestante) {
        this.quantiteRestante = quantiteRestante;
    }

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public BigDecimal getValeurRestante() {
        return valeurRestante;
    }

    public void setValeurRestante(BigDecimal valeurRestante) {
        this.valeurRestante = valeurRestante;
    }

    public boolean isEpuise() {
        return epuise;
    }

    public void setEpuise(boolean epuise) {
        this.epuise = epuise;
    }

    public String getSourceReference() {
        return sourceReference;
    }

    public void setSourceReference(String sourceReference) {
        this.sourceReference = sourceReference;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceDocument() {
        return sourceDocument;
    }

    public void setSourceDocument(String sourceDocument) {
        this.sourceDocument = sourceDocument;
    }
}
