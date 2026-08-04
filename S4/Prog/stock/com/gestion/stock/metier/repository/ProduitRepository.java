package com.gestion.stock.metier.repository;

import java.sql.Connection;

import com.gestion.stock.core.repository.GenericRepository;
import com.gestion.stock.metier.entity.Produit;
import com.gestion.stock.metier.metadata.ProduitMetadata;


public class ProduitRepository extends GenericRepository<Produit>{
    public ProduitRepository(Connection conn){
        super(conn, ProduitMetadata.build(), Produit.class);
    }
}
