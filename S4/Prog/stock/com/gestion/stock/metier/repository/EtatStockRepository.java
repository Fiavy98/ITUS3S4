package com.gestion.stock.metier.repository;

import java.sql.Connection;

import com.gestion.stock.core.repository.GenericRepository;
import com.gestion.stock.metier.entity.EtatStock;
import com.gestion.stock.metier.metadata.EtatStockMetadata;


public class EtatStockRepository extends GenericRepository<EtatStock>{
    public EtatStockRepository(Connection conn){
        super(conn, EtatStockMetadata.build(), EtatStock.class);
    }
}
