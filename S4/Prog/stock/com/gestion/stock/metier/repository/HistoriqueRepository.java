package com.gestion.stock.metier.repository;

import java.sql.Connection;

import com.gestion.stock.core.repository.GenericRepository;
import com.gestion.stock.metier.entity.HistoriqueMouvement;
import com.gestion.stock.metier.metadata.MouvementMetadata;


public class HistoriqueRepository extends GenericRepository<HistoriqueMouvement>{
    public HistoriqueRepository(Connection conn){
        super(conn, MouvementMetadata.build(), HistoriqueMouvement.class);
    }
}
