package com.gestion.stock.metier.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import com.gestion.stock.config.DatabaseConfig;
import com.gestion.stock.exception.BusinessException;
import com.gestion.stock.exception.StockInsuffisantException;
import com.gestion.stock.metier.entity.Article;
import com.gestion.stock.metier.entity.Lot;
import com.gestion.stock.metier.entity.Mouvement;
import com.gestion.stock.metier.entity.MouvementLotSource;
import com.gestion.stock.metier.repository.ArticleRepository;
import com.gestion.stock.metier.repository.LotRepository;
import com.gestion.stock.metier.repository.MouvementLotSourceRepository;
import com.gestion.stock.metier.repository.MouvementRepository;

public class StockService {
    private final ArticleRepository articleRepository = new ArticleRepository();
    private final LotRepository lotRepository = new LotRepository();
    private final MouvementRepository mouvementRepository = new MouvementRepository();
    private final MouvementLotSourceRepository mlsRepository = new MouvementLotSourceRepository();
    
    private final Map<String, ValorizationStrategy> strategies = new HashMap<>();
    
    public StockService() {
        strategies.put("FIFO", new FifoStrategy());
        strategies.put("LIFO", new LifoStrategy());
        strategies.put("CUMP", new CumpStrategy());
    }
    
    public void enregistrerEntree(Article article, LocalDate date, BigDecimal quantite, BigDecimal prixUnitaire,
                                  String sourceRef, String sourceType, String sourceTiers, String motif) 
            throws BusinessException {
        Connection conn = null;
        try {
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false);
            
            // 1. Charger l'article à jour
                Article currentArticle = articleRepository.findById(article.getId(), conn)
                    .orElseThrow(() -> new BusinessException("Article non trouvé"));
            
            // 2. Créer le lot
            Lot lot = new Lot();
            lot.setArticleId(article.getId());
            lot.setDateEntree(date);
            lot.setQuantiteInitiale(quantite);
            lot.setQuantiteRestante(quantite);
            lot.setPrixUnitaire(prixUnitaire);
            lot.setSourceReference(sourceRef);
            lot.setSourceType(sourceType);
            lot.setSourceDocument(sourceTiers);
            lot.setEpuise(false);
            lot = lotRepository.insert(lot, conn);
            
            // 3. Créer le mouvement d'entrée
            Mouvement mouvement = new Mouvement();
            mouvement.setArticleId(article.getId());
            mouvement.setDateMouvement(date);
            mouvement.setTypeMouvement("ENTREE");
            mouvement.setQuantite(quantite);
            mouvement.setPrixUnitaireCalcule(prixUnitaire);
            BigDecimal valeurEntree = quantite.multiply(prixUnitaire);
            mouvement.setValeurMouvement(valeurEntree);
            mouvement.setSourceReference(sourceRef);
            mouvement.setSourceType(sourceType);
            mouvement.setSourceTiers(sourceTiers);
            mouvement.setMotif(motif);
            mouvement.setMethodeValorisation(null);
            
            // 4. Mettre à jour l'article (stock, valeur, CUMP)
            BigDecimal ancienStock = currentArticle.getStockActuel();
            BigDecimal ancienneValeur = currentArticle.getValeurStockActuelle();
            BigDecimal nouveauStock = ancienStock.add(quantite);
            BigDecimal nouvelleValeur = ancienneValeur.add(valeurEntree);
            BigDecimal nouveauCump;
            if (nouveauStock.compareTo(BigDecimal.ZERO) == 0) {
                nouveauCump = BigDecimal.ZERO;
            } else {
                nouveauCump = nouvelleValeur.divide(nouveauStock, 4, RoundingMode.HALF_UP);
            }
            
            mouvement.setStockQteApres(nouveauStock);
            mouvement.setValeurStockApres(nouvelleValeur);
            mouvement.setCumpApres(nouveauCump);
            
            // Sauvegarde mouvement
            mouvement = mouvementRepository.insert(mouvement, conn);
            
            // Mettre à jour le lot avec l'ID du mouvement
            lot.setMouvementEntreeId(mouvement.getId());
            lotRepository.update(lot, conn);
            
            // Mettre à jour l'article
            articleRepository.updateStockAndCump(article.getId(), nouveauStock, nouvelleValeur, nouveauCump, conn);
            
            conn.commit();
        } catch (SQLException | StockInsuffisantException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            throw new BusinessException("Erreur lors de l'entrée: " + e.getMessage(), e);
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
    
    // public void enregistrerSortie(Article article, LocalDate date, BigDecimal quantite, String methodeChoisie,
    //                               String sourceRef, String sourceType, String sourceTiers, String motif)
    //         throws BusinessException, StockInsuffisantException {
    //     Connection conn = null;
    //     try {
    //         conn = DatabaseConfig.getConnection();
    //         conn.setAutoCommit(false);
            
    //             Article currentArticle = articleRepository.findById(article.getId(), conn)
    //                 .orElseThrow(() -> new BusinessException("Article non trouvé"));
            
    //         String methode = methodeChoisie;
    //         ValorizationStrategy strategy = strategies.get(methode);
    //         if (strategy == null) {
    //             throw new BusinessException("Méthode de valorisation inconnue: " + methode);
    //         }
            
    //         BigDecimal disponible = currentArticle.getStockActuel().subtract(currentArticle.getStockReserve());
    //         if (disponible.compareTo(quantite) < 0) {
    //             throw new StockInsuffisantException("Stock disponible insuffisant. Disponible: " + disponible + ", réserve: " + currentArticle.getStockReserve());
    //         }
            
    //         // Calcul de la sortie (consomme les lots et met à jour les quantités restantes)
    //         ResultatSortie result = strategy.calculerSortie(article.getId(), quantite, conn);
            
    //         // Créer le mouvement
    //         Mouvement mouvement = new Mouvement();
    //         mouvement.setArticleId(article.getId());
    //         mouvement.setDateMouvement(date);
    //         mouvement.setTypeMouvement("SORTIE");
    //         mouvement.setQuantite(quantite);
    //         mouvement.setPrixUnitaireCalcule(result.getPrixUnitaireMoyen());
    //         mouvement.setValeurMouvement(result.getValeurTotale());
    //         mouvement.setSourceReference(sourceRef);
    //         mouvement.setSourceType(sourceType);
    //         mouvement.setSourceTiers(sourceTiers);
    //         mouvement.setMotif(motif);
    //         mouvement.setMethodeValorisation(methode);
            
    //         // Mettre à jour article après sortie
    //         BigDecimal nouveauStock = currentArticle.getStockActuel().subtract(quantite);
    //         BigDecimal nouvelleValeur = currentArticle.getValeurStockActuelle().subtract(result.getValeurTotale());
    //         BigDecimal nouveauCump = BigDecimal.ZERO;
    //         if (nouveauStock.compareTo(BigDecimal.ZERO) > 0) {
    //             nouveauCump = nouvelleValeur.divide(nouveauStock, 4, RoundingMode.HALF_UP);
    //         }
            
    //         mouvement.setStockQteApres(nouveauStock);
    //         mouvement.setValeurStockApres(nouvelleValeur);
    //         mouvement.setCumpApres(nouveauCump);
            
    //         mouvement = mouvementRepository.insert(mouvement, conn);
            
    //         // Enregistrer les MouvementLotSource
    //         for (MouvementLotSource mls : result.getLotsConsommes()) {
    //             mls.setMouvementId(mouvement.getId());
    //             mlsRepository.insert(mls, conn);
    //         }
            
    //         // Mettre à jour l'article
    //         articleRepository.updateStockAndCump(article.getId(), nouveauStock, nouvelleValeur, nouveauCump, conn);
            
    //         conn.commit();
    //     } catch (SQLException e) {
    //         if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
    //         throw new BusinessException("Erreur lors de la sortie: " + e.getMessage(), e);
    //     } finally {
    //         if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
    //     }
    // }



    public void enregistrerSortie(Article article, LocalDate date, BigDecimal quantite,
                               BigDecimal puSortie, String sourceRef, String sourceType,
                               String sourceTiers, String motif)
        throws BusinessException, StockInsuffisantException {
    Connection conn = null;
    try {
        conn = DatabaseConfig.getConnection();
        conn.setAutoCommit(false);

        Article currentArticle = articleRepository.findById(article.getId(), conn)
            .orElseThrow(() -> new BusinessException("Article non trouvé"));

        String methode = currentArticle.getMethodeGestion() != null
                ? currentArticle.getMethodeGestion().trim().toUpperCase()
                : "CUMP";
        ValorizationStrategy strategy = strategies.get(methode);
        if (strategy == null) {
            throw new BusinessException("Méthode inconnue: " + methode);
        }

        // ✅ BUG 1 CORRIGÉ — vérification simple sans stockReserve
        if (currentArticle.getStockActuel().compareTo(quantite) < 0) {
            throw new StockInsuffisantException(
                "Stock insuffisant. Stock: " + currentArticle.getStockActuel()
                + ", demandé: " + quantite
            );
        }

        // Calcul de la sortie
        ResultatSortie result = strategy.calculerSortie(article.getId(), quantite, conn);

        // Créer le mouvement
        Mouvement mouvement = new Mouvement();
        mouvement.setArticleId(article.getId());
        mouvement.setDateMouvement(date);
        mouvement.setTypeMouvement("SORTIE");
        mouvement.setQuantite(quantite);
        if (puSortie != null && ("FIFO".equals(methode) || "LIFO".equals(methode))) {
            // PU manuel decoratif en FIFO/LIFO, la valeur stock vient des lots consommes.
            mouvement.setPrixUnitaireCalcule(puSortie);
        } else {
            mouvement.setPrixUnitaireCalcule(result.getPrixUnitaireMoyen());
        }
        mouvement.setValeurMouvement(result.getValeurTotale());
        mouvement.setSourceReference(sourceRef);
        mouvement.setSourceType(sourceType);
        mouvement.setSourceTiers(sourceTiers);
        mouvement.setMotif(motif);
        mouvement.setMethodeValorisation(methode);

        // Nouveau stock et valeur
        BigDecimal nouveauStock   = currentArticle.getStockActuel().subtract(quantite);
        BigDecimal nouvelleValeur = currentArticle.getValeurStockActuelle()
                                                  .subtract(result.getValeurTotale());

        // ✅ BUG 3 CORRIGÉ — CUMP selon la méthode
        BigDecimal nouveauCump;
        if ("CUMP".equals(methode)) {
            // CUMP ne change PAS après une sortie CUMP
            nouveauCump = nouveauStock.compareTo(BigDecimal.ZERO) == 0
                ? BigDecimal.ZERO
                : currentArticle.getCumpActuel();
        } else {
            // FIFO/LIFO → recalculer le CUMP depuis la valeur restante
            nouveauCump = nouveauStock.compareTo(BigDecimal.ZERO) == 0
                ? BigDecimal.ZERO
                : nouvelleValeur.divide(nouveauStock, 4, RoundingMode.HALF_UP);
        }

        mouvement.setStockQteApres(nouveauStock);
        mouvement.setValeurStockApres(nouvelleValeur);
        mouvement.setCumpApres(nouveauCump);
        mouvement = mouvementRepository.insert(mouvement, conn);

        // Enregistrer les lignes source
        for (MouvementLotSource mls : result.getLotsConsommes()) {
            mls.setMouvementId(mouvement.getId());
            mlsRepository.insert(mls, conn);
        }

        // ✅ BUG 2 CORRIGÉ — UNE SEULE mise à jour article ici
        // CumpStrategy ne doit plus appeler updateStockAndCump (voir ci-dessous)
        articleRepository.updateStockAndCump(
            article.getId(), nouveauStock, nouvelleValeur, nouveauCump, conn
        );

        conn.commit();

    } catch (StockInsuffisantException e) {
        if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
        throw e;
    } catch (SQLException e) {
        if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
        throw new BusinessException("Erreur lors de la sortie: " + e.getMessage(), e);
    } finally {
        if (conn != null) try {
            conn.setAutoCommit(true); conn.close();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
    
    public ResultatSortie simulerSortie(Article article, BigDecimal quantite, String methode) 
            throws StockInsuffisantException {
        Connection conn = null;
        try {
            conn = DatabaseConfig.getConnection();
            Article currentArticle = articleRepository.findById(article.getId(), conn)
                    .orElseThrow(() -> new StockInsuffisantException("Article non trouvé"));
            BigDecimal disponible = currentArticle.getStockActuel().subtract(currentArticle.getStockReserve());
            if (disponible.compareTo(quantite) < 0) {
                throw new StockInsuffisantException("Stock disponible insuffisant. Disponible: " + disponible + ", réserve: " + currentArticle.getStockReserve());
            }
            ValorizationStrategy strategy = strategies.get(methode);
            if (strategy == null) throw new StockInsuffisantException("Méthode inconnue");
            return strategy.simulerSortie(article.getId(), quantite, conn);
        } catch (SQLException e) {
            throw new StockInsuffisantException("Erreur simulation: " + e.getMessage(), e);
        } finally {
            if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}