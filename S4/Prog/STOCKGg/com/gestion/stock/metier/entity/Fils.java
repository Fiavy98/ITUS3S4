package com.gestion.stock.metier.entity;

import com.gestion.stock.core.entity.BaseEntity;

public class Fils extends BaseEntity {
    private Integer numCompte;
    private String libelle;
    private boolean isDebit;
    private boolean isCredit;
    private Integer sourceMvntId;

    public Integer getNumCompte() {
        return numCompte;
    }

    public void setNumCompte(Integer numCompte) {
        this.numCompte = numCompte;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public boolean isDebit() {
        return isDebit;
    }

    public void setDebit(boolean debit) {
        isDebit = debit;
    }

    public boolean isCredit() {
        return isCredit;
    }

    public void setCredit(boolean credit) {
        isCredit = credit;
    }

    public Integer getSourceMvntId() {
        return sourceMvntId;
    }

    public void setSourceMvntId(Integer sourceMvntId) {
        this.sourceMvntId = sourceMvntId;
    }

    @Override
    public String getTableName() {
        return "fils";
    }

    @Override
    public String getIdColumnName() {
        return "id";
    }
}