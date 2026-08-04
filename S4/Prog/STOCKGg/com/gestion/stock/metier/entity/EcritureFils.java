package com.gestion.stock.metier.entity;

import com.gestion.stock.core.entity.BaseEntity;
import java.math.BigDecimal;

public class EcritureFils extends BaseEntity {
    private EcritureMere ecritureMere;
    private String numeroCompte; // Account number
    private String libelle;
    private BigDecimal debit;
    private BigDecimal credit;
    private Integer mouvementId;

    public EcritureFils() {
        this.debit = BigDecimal.ZERO;
        this.credit = BigDecimal.ZERO;
    }

    public EcritureFils(String numeroCompte, String libelle, BigDecimal debit, BigDecimal credit, Integer mouvementId) {
        this.numeroCompte = numeroCompte;
        this.libelle = libelle;
        this.debit = debit != null ? debit : BigDecimal.ZERO;
        this.credit = credit != null ? credit : BigDecimal.ZERO;
        this.mouvementId = mouvementId;
    }

    public EcritureMere getEcritureMere() {
        return ecritureMere;
    }

    public void setEcritureMere(EcritureMere ecritureMere) {
        this.ecritureMere = ecritureMere;
    }

    public String getNumeroCompte() {
        return numeroCompte;
    }

    public void setNumeroCompte(String numeroCompte) {
        this.numeroCompte = numeroCompte;
    }

    public String getCompte() {
        return numeroCompte;
    }

    public void setCompte(String compte) {
        this.numeroCompte = compte;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public BigDecimal getDebit() {
        return debit;
    }

    public void setDebit(BigDecimal debit) {
        this.debit = debit != null ? debit : BigDecimal.ZERO;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit != null ? credit : BigDecimal.ZERO;
    }

    public Integer getMouvementId() {
        return mouvementId;
    }

    public void setMouvementId(Integer mouvementId) {
        this.mouvementId = mouvementId;
    }

    @Override
    public String getTableName() {
        return "ecriture_fils";
    }

    @Override
    public String getIdColumnName() {
        return "id";
    }

    @Override
    public String toString() {
        return "EcritureFils{" +
                "id=" + id +
                ", numeroCompte='" + numeroCompte + '\'' +
                ", libelle='" + libelle + '\'' +
                ", debit=" + debit +
                ", credit=" + credit +
                ", mouvementId=" + mouvementId +
                '}';
    }
}
