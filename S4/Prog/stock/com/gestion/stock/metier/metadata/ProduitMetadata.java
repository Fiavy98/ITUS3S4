package com.gestion.stock.metier.metadata;
import java.util.List;

import com.gestion.stock.core.entity.ColumnDef;
import com.gestion.stock.core.entity.EntityMetadata;

public class ProduitMetadata {
    public static EntityMetadata build(){
        EntityMetadata metadata = new EntityMetadata();
        metadata.setTableName("produits");
        metadata.setIdField("id");

        metadata.setColumns(List.of(
            new ColumnDef("id", "ID",Integer.class,false, false),
        
            new ColumnDef("nom", "Nom Produit", String.class),
        
            new ColumnDef(
                "methodeValuation",
                "Méthode",
                List.of("FIFO", "LIFO", "CUMP")
            ),
        
            new ColumnDef("unite", "Unité", String.class)
        ));


        return metadata;
    }    
}
