package com.gestion.stock.metier.metadata;

import java.util.List;

import com.gestion.stock.core.entity.ColumnDef;
import com.gestion.stock.core.entity.EntityMetadata;

public class MouvementMetadata {
    public static EntityMetadata build() {
       
        EntityMetadata meta = new EntityMetadata();
        meta.setTableName("mouvements");
        meta.setIdField("id");

        meta.setColumns(List.of(
            new ColumnDef("id", "ID", Integer.class,false, false),
        
            new ColumnDef(
                "produitId",
                "Produit",
                List.of("P001 - Riz", "P002 - Sucre", "P003 - Huile")),
        
            new ColumnDef(
                "type",
                "Type",
                List.of("ENTREE", "SORTIE")
            ),
        
            new ColumnDef("quantite", "Quantité", Integer.class),
        
            new ColumnDef("quantiteRestante", "Quantité restante", Integer.class),
        
            new ColumnDef("prixUnitaire", "Prix unitaire", Double.class),
        
            new ColumnDef("date", "Date", java.util.Date.class)
        ));

        return meta;
    }      
}
