package com.gestion.stock.metier.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;

import com.gestion.stock.exception.StockInsuffisantException;
import com.gestion.stock.metier.entity.Article;
import com.gestion.stock.metier.repository.ArticleRepository;

public class CumpStrategy implements ValorizationStrategy {
    private final ArticleRepository articleRepository = new ArticleRepository();
    
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
        try {
                Article article = articleRepository.findById(articleId, conn)
                    .orElseThrow(() -> new StockInsuffisantException("Article non trouvé"));
                if(article.getStockActuel().compareTo(quantite)<0 ){
                    throw  new StockInsuffisantException("stock disponible insuffisant stock :" + article.getStockActuel() + "demander" + quantite);
                }

            BigDecimal cump = article.getCumpActuel();
            BigDecimal valeurSortie = quantite.multiply(cump).setScale(4, RoundingMode.HALF_UP);
            
            ResultatSortie result = new ResultatSortie(cump, valeurSortie);
            // Pas de lots consommes en CUMP, mais on crée un enregistrement fictif pour la trace
            // MouvementLotSource mls = new MouvementLotSource();
            // mls.setLotId(article.getId());
            // mls.setQuantitePrelevee(quantite);
            // mls.setPrixUnitaireLot(cump);
            // mls.setSourceLotReference("CUMP virtuel");
            // mls.setOrdreConsommation(1);
            // List<MouvementLotSource> list = new ArrayList<>();
            // list.add(mls);
            // result.setLotsConsommes(list);
            return result;
        } catch (Exception e) {
            throw new StockInsuffisantException("Erreur CUMP: " + e.getMessage(), e);
        }
    }
}