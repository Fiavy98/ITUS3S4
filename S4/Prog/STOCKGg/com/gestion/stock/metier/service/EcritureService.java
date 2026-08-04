package com.gestion.stock.metier.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.sql.Connection;
import java.sql.SQLException;

import com.gestion.stock.exception.BusinessException;
import com.gestion.stock.metier.entity.Article;
import com.gestion.stock.metier.entity.EcritureMere;
import com.gestion.stock.metier.entity.EcritureFils;
import com.gestion.stock.metier.entity.Mouvement;
import com.gestion.stock.metier.repository.EcritureMereRepository;
import com.gestion.stock.metier.repository.EcritureFilsRepository;

/**
 * Service responsible for generating automatic accounting entries (écritures comptables)
 * for stock movements (Entrée, Sortie, Ajustement).
 * 
 * Each stock movement generates:
 * - One header entry (EcritureMere) with date, description, and journal
 * - Multiple detail entries (EcritureFils) with account numbers, debits, and credits
 */
public class EcritureService {
    private final EcritureMereRepository ecritureMereRepository = new EcritureMereRepository();
    private final EcritureFilsRepository ecritureFilsRepository = new EcritureFilsRepository();
    
    // Default accounting configuration
    private static final String JOURNAL = "Stock";
    private static final String COMPTE_STOCK = "3100"; // Stock account
    private static final String COMPTE_VARIATION_STOCK = "6031"; // Stock variation account
    private static final String COMPTE_FOURNISSEUR = "4011"; // Supplier account
    private static final String COMPTE_CLIENT = "4111"; // Customer account
    private static final String COMPTE_TIERS_DIVERS = "4190"; // Miscellaneous third party account
    
    /**
     * Generates accounting entries for a stock movement.
     * This method is generic and reusable for all movement types.
     * 
     * @param mouvement The stock movement
     * @param article The associated article
     * @param sourceCompte The source account (supplier, customer, etc.)
     * @param conn Active database connection
     * @throws BusinessException if entry generation fails
     */
    public void generaliserEcriture(Mouvement mouvement, Article article, 
                                   String sourceCompte, Connection conn) throws BusinessException {
        try {
            // 1. Create header entry (Ecriture Mère)
            EcritureMere ecritureMere = new EcritureMere();
            ecritureMere.setDateEcriture(mouvement.getDateMouvement());
            ecritureMere.setJournal(JOURNAL);
            ecritureMere.setMouvementId(mouvement.getId());
            
            // Generate header description based on movement type
            String libelleMere = generateLibelleMere(mouvement, article);
            ecritureMere.setLibelle(libelleMere);
            
            // Save header entry
            Integer ecritureMereId = ecritureMereRepository.saveWithConnection(ecritureMere, conn);
            if (ecritureMereId == null) {
                throw new BusinessException("Failed to create header entry");
            }
            ecritureMere.setId(ecritureMereId);
            
            // 2. Create detail entries (Ecritures Fils) based on movement type
            switch (mouvement.getTypeMouvement()) {
                case "ENTREE":
                    createEntryForEntree(ecritureMere, mouvement, article, sourceCompte, conn);
                    break;
                case "SORTIE":
                    createEntryForSortie(ecritureMere, mouvement, article, sourceCompte, conn);
                    break;
                case "AJUSTEMENT":
                    createEntryForAjustement(ecritureMere, mouvement, article, sourceCompte, conn);
                    break;
                default:
                    throw new BusinessException("Unknown movement type: " + mouvement.getTypeMouvement());
            }
            
            // 3. Validate that entry is balanced (total debits = total credits)
            validateEntryBalance(ecritureMere, conn);
            
        } catch (SQLException e) {
            throw new BusinessException("Database error while generating entries: " + e.getMessage(), e);
        }
    }
    
    /**
     * Creates accounting entries for a stock entry (ENTREE).
     * Stock entry: Debit Stock account, Credit source account (supplier or cash)
     */
    private void createEntryForEntree(EcritureMere ecritureMere, Mouvement mouvement, 
                                     Article article, String sourceCompte, Connection conn) throws SQLException {
        BigDecimal montant = mouvement.getValeurMouvement();
        
        // 1. Debit: Stock account
        EcritureFils debitStock = new EcritureFils();
        debitStock.setEcritureMere(ecritureMere);
        debitStock.setNumeroCompte(COMPTE_STOCK);
        debitStock.setLibelle("Entrée " + article.getCode() + " - " + mouvement.getQuantite() + " " + mouvement.getSourceType());
        debitStock.setDebit(montant);
        debitStock.setCredit(BigDecimal.ZERO);
        debitStock.setMouvementId(mouvement.getId());
        ecritureFilsRepository.saveWithConnection(debitStock, conn);
        ecritureMere.addEcritureFils(debitStock);
        
        // 2. Credit: Source account
        EcritureFils creditSource = new EcritureFils();
        creditSource.setEcritureMere(ecritureMere);
        creditSource.setNumeroCompte(sourceCompte != null ? sourceCompte : COMPTE_FOURNISSEUR);
        creditSource.setLibelle("Fournisseur/Tiers: " + mouvement.getSourceTiers());
        creditSource.setDebit(BigDecimal.ZERO);
        creditSource.setCredit(montant);
        creditSource.setMouvementId(mouvement.getId());
        ecritureFilsRepository.saveWithConnection(creditSource, conn);
        ecritureMere.addEcritureFils(creditSource);
    }
    
    /**
     * Creates accounting entries for a stock exit (SORTIE).
     * Stock exit: Debit variation account, Credit stock account
     */
    private void createEntryForSortie(EcritureMere ecritureMere, Mouvement mouvement, 
                                     Article article, String sourceCompte, Connection conn) throws SQLException {
        BigDecimal montant = mouvement.getValeurMouvement();
        
        // 1. Debit: Stock variation account (expense)
        EcritureFils debitVariation = new EcritureFils();
        debitVariation.setEcritureMere(ecritureMere);
        debitVariation.setNumeroCompte(COMPTE_VARIATION_STOCK);
        debitVariation.setLibelle("Sortie " + article.getCode() + " - " + mouvement.getQuantite() + " unités");
        debitVariation.setDebit(montant);
        debitVariation.setCredit(BigDecimal.ZERO);
        debitVariation.setMouvementId(mouvement.getId());
        ecritureFilsRepository.saveWithConnection(debitVariation, conn);
        ecritureMere.addEcritureFils(debitVariation);
        
        // 2. Credit: Stock account (reduction)
        EcritureFils creditStock = new EcritureFils();
        creditStock.setEcritureMere(ecritureMere);
        creditStock.setNumeroCompte(COMPTE_STOCK);
        creditStock.setLibelle("Réduction stock: " + (sourceCompte != null ? sourceCompte : "Sortie générale"));
        creditStock.setDebit(BigDecimal.ZERO);
        creditStock.setCredit(montant);
        creditStock.setMouvementId(mouvement.getId());
        ecritureFilsRepository.saveWithConnection(creditStock, conn);
        ecritureMere.addEcritureFils(creditStock);
    }
    
    /**
     * Creates accounting entries for a stock adjustment (AJUSTEMENT).
     * Can be an increase or decrease depending on the movement quantity.
     */
    private void createEntryForAjustement(EcritureMere ecritureMere, Mouvement mouvement, 
                                        Article article, String sourceCompte, Connection conn) throws SQLException {
        BigDecimal montant = mouvement.getValeurMouvement();
        
        if (mouvement.getQuantite().compareTo(BigDecimal.ZERO) > 0) {
            // Adjustment increase: similar to entry
            EcritureFils debitStock = new EcritureFils();
            debitStock.setEcritureMere(ecritureMere);
            debitStock.setNumeroCompte(COMPTE_STOCK);
            debitStock.setLibelle("Ajustement + " + article.getCode() + " - " + mouvement.getQuantite() + " unités");
            debitStock.setDebit(montant);
            debitStock.setCredit(BigDecimal.ZERO);
            debitStock.setMouvementId(mouvement.getId());
            ecritureFilsRepository.saveWithConnection(debitStock, conn);
            ecritureMere.addEcritureFils(debitStock);
            
            EcritureFils creditVariation = new EcritureFils();
            creditVariation.setEcritureMere(ecritureMere);
            creditVariation.setNumeroCompte(COMPTE_VARIATION_STOCK);
            creditVariation.setLibelle("Correction inventaire: " + mouvement.getMotif());
            creditVariation.setDebit(BigDecimal.ZERO);
            creditVariation.setCredit(montant);
            creditVariation.setMouvementId(mouvement.getId());
            ecritureFilsRepository.saveWithConnection(creditVariation, conn);
            ecritureMere.addEcritureFils(creditVariation);
        } else {
            // Adjustment decrease: similar to exit
            EcritureFils debitVariation = new EcritureFils();
            debitVariation.setEcritureMere(ecritureMere);
            debitVariation.setNumeroCompte(COMPTE_VARIATION_STOCK);
            debitVariation.setLibelle("Ajustement - " + article.getCode() + " - " + mouvement.getQuantite().abs() + " unités");
            debitVariation.setDebit(montant);
            debitVariation.setCredit(BigDecimal.ZERO);
            debitVariation.setMouvementId(mouvement.getId());
            ecritureFilsRepository.saveWithConnection(debitVariation, conn);
            ecritureMere.addEcritureFils(debitVariation);
            
            EcritureFils creditStock = new EcritureFils();
            creditStock.setEcritureMere(ecritureMere);
            creditStock.setNumeroCompte(COMPTE_STOCK);
            creditStock.setLibelle("Correction inventaire: " + mouvement.getMotif());
            creditStock.setDebit(BigDecimal.ZERO);
            creditStock.setCredit(montant);
            creditStock.setMouvementId(mouvement.getId());
            ecritureFilsRepository.saveWithConnection(creditStock, conn);
            ecritureMere.addEcritureFils(creditStock);
        }
    }
    
    /**
     * Validates that the accounting entry is balanced (debits = credits).
     * This is a fundamental accounting principle.
     */
    private void validateEntryBalance(EcritureMere ecritureMere, Connection conn) throws SQLException, BusinessException {
        if (!ecritureMere.isEquilibree()) {
            throw new BusinessException(
                String.format("Unbalanced entry: Total debits (%s) != Total credits (%s)",
                    ecritureMere.getTotalDebit(), ecritureMere.getTotalCredit())
            );
        }
    }
    
    /**
     * Generates a descriptive label for the header entry based on movement type.
     */
    private String generateLibelleMere(Mouvement mouvement, Article article) {
        String type = mouvement.getTypeMouvement();
        LocalDate date = mouvement.getDateMouvement();
        
        return String.format("%s - %s %s (%s) - %s",
            type,
            mouvement.getQuantite(),
            mouvement.getSourceType() != null ? mouvement.getSourceType() : "unités",
            article.getCode(),
            date
        );
    }
}
