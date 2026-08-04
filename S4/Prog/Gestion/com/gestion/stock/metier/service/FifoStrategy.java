package com.gestion.stock.metier.service;

import com.gestion.stock.exception.StockInsuffisantException;
import com.gestion.stock.metier.entity.Lot;
import com.gestion.stock.metier.entity.MouvementLotSource;
import com.gestion.stock.metier.repository.LotRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class FifoStrategy implements ValorizationStrategy {
    private final LotRepository lotRepository = new LotRepository();
    
    @Override
    public ResultatSortie calculerSortie(int articleId, BigDecimal quantite, Connection conn) 
            throws StockInsuffisantException {
        return traiterSortie(articleId, quantite, conn, true);
    }
    
    @Override
    public ResultatSortie simulerSortie(int articleId, BigDecimal quantite, Connection conn) 
            throws StockInsuffisantException {
        return traiterSortie(articleId, quantite, conn, false);
    }
    
    private ResultatSortie traiterSortie(int articleId, BigDecimal quantite, Connection conn, boolean applyUpdate) 
            throws StockInsuffisantException {
        ResultatSortie result = new ResultatSortie();
        List<MouvementLotSource> sources = new ArrayList<>();
        BigDecimal quantiteRestante = quantite;
        BigDecimal valeurTotale = BigDecimal.ZERO;
        
        try {
            List<Lot> lots = lotRepository.findActiveLotsByArticle(articleId, "date_entree ASC, id ASC", conn);
            if (lots.isEmpty()) {
                throw new StockInsuffisantException("Aucun lot disponible pour l'article " + articleId);
            }
            
            int ordre = 1;
            for (Lot lot : lots) {
                if (quantiteRestante.compareTo(BigDecimal.ZERO) <= 0) break;
                
                BigDecimal prelevement = lot.getQuantiteRestante().min(quantiteRestante);
                BigDecimal valeurPrelevee = prelevement.multiply(lot.getPrixUnitaire());
                valeurTotale = valeurTotale.add(valeurPrelevee);
                
                MouvementLotSource mls = new MouvementLotSource();
                mls.setLotId(lot.getId());
                mls.setQuantitePrelevee(prelevement);
                mls.setPrixUnitaireLot(lot.getPrixUnitaire());
                mls.setSourceLotReference(lot.getSourceReference());
                mls.setSourceLotType(lot.getSourceType());
                mls.setOrdreConsommation(ordre++);
                sources.add(mls);
                
                if (applyUpdate) {
                    BigDecimal newQteRestante = lot.getQuantiteRestante().subtract(prelevement);
                    boolean epuise = newQteRestante.compareTo(BigDecimal.ZERO) == 0;
                    lotRepository.updateQuantiteRestante(lot.getId(), newQteRestante, epuise, conn);
                }
                
                quantiteRestante = quantiteRestante.subtract(prelevement);
            }
            
            if (quantiteRestante.compareTo(BigDecimal.ZERO) > 0) {
                throw new StockInsuffisantException("Stock insuffisant. Manque " + quantiteRestante + " unités");
            }
            
            BigDecimal prixMoyen = valeurTotale.divide(quantite, 4, RoundingMode.HALF_UP);
            result.setPrixUnitaireMoyen(prixMoyen);
            result.setValeurTotale(valeurTotale);
            result.setLotsConsommes(sources);
            
        } catch (StockInsuffisantException e) {
            throw e;
        } catch (Exception e) {
            throw new StockInsuffisantException("Erreur lors du calcul FIFO: " + e.getMessage(), e);
        }
        return result;
    }
}