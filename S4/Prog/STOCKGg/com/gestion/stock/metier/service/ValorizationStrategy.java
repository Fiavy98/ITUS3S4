package com.gestion.stock.metier.service;

import com.gestion.stock.exception.StockInsuffisantException;
import com.gestion.stock.metier.entity.MouvementLotSource;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

public interface ValorizationStrategy {
    ResultatSortie calculerSortie(int articleId, BigDecimal quantite, Connection conn) 
            throws StockInsuffisantException;
    
    // Pour simulation sans écriture
    ResultatSortie simulerSortie(int articleId, BigDecimal quantite, Connection conn) 
            throws StockInsuffisantException;
}