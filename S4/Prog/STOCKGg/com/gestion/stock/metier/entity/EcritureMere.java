package com.gestion.stock.metier.entity;

import com.gestion.stock.core.entity.BaseEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EcritureMere extends BaseEntity {
    private LocalDate dateEcriture;
    private String libelle;
    private String journal;
    private Integer mouvementId; // Reference to source movement
    private List<EcritureFils> ecritureFils = new ArrayList<>();

    public EcritureMere() {
    }

    public EcritureMere(LocalDate dateEcriture, String libelle, String journal) {
        this.dateEcriture = dateEcriture;
        this.libelle = libelle;
        this.journal = journal;
    }

    public EcritureMere(LocalDate dateEcriture, String libelle, String journal, Integer mouvementId) {
        this.dateEcriture = dateEcriture;
        this.libelle = libelle;
        this.journal = journal;
        this.mouvementId = mouvementId;
    }

    public LocalDate getDateEcriture() {
        return dateEcriture;
    }

    public void setDateEcriture(LocalDate dateEcriture) {
        this.dateEcriture = dateEcriture;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }

    public Integer getMouvementId() {
        return mouvementId;
    }

    public void setMouvementId(Integer mouvementId) {
        this.mouvementId = mouvementId;
    }

    public List<EcritureFils> getEcritureFils() {
        return ecritureFils;
    }

    public void setEcritureFils(List<EcritureFils> ecritureFils) {
        this.ecritureFils = ecritureFils;
    }

    public void addEcritureFils(EcritureFils fils) {
        this.ecritureFils.add(fils);
        fils.setEcritureMere(this);
    }

    @Override
    public String getTableName() {
        return "ecriture_mere";
    }

    @Override
    public String getIdColumnName() {
        return "id";
    }

    public BigDecimal getTotalDebit() {
        return ecritureFils.stream()
                .map(EcritureFils::getDebit)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalCredit() {
        return ecritureFils.stream()
                .map(EcritureFils::getCredit)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean isEquilibree() {
        return getTotalDebit().compareTo(getTotalCredit()) == 0;
    }

    @Override
    public String toString() {
        return "EcritureMere{" +
                "id=" + id +
                ", dateEcriture=" + dateEcriture +
                ", libelle='" + libelle + '\'' +
                ", journal='" + journal + '\'' +
                ", totalDebit=" + getTotalDebit() +
                ", totalCredit=" + getTotalCredit() +
                '}';
    }
}
