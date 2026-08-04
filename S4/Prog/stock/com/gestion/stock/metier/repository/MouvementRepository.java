package com.gestion.stock.metier.repository;

import java.sql.Connection;

import com.gestion.stock.core.repository.GenericRepository;
import com.gestion.stock.metier.entity.Mouvement;
import com.gestion.stock.metier.metadata.MouvementMetadata;


public class MouvementRepository extends GenericRepository<Mouvement>{
    public MouvementRepository(Connection conn){
        super(conn, MouvementMetadata.build(), Mouvement.class);
    }
}
