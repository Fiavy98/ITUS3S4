package com.gestion.stock.metier.metadata;

import java.util.List;

import com.gestion.stock.core.entity.ColumnDef;
import com.gestion.stock.core.entity.EntityMetadata;


public class HistoriqueMouvementMetadata {
    public static EntityMetadata build() {
       
        EntityMetadata meta = new EntityMetadata();
        meta.setTableName("historique_mouvements");
        meta.setIdField("id");

        meta.setColumns(List.of(
            new ColumnDef("id", "ID", Integer.class,false, false),
        
            new ColumnDef("mouvementId", "Mouvement", String.class),
        
            new ColumnDef("stockQte", "Stock après", Integer.class),
        
            new ColumnDef("valeurStock", "Valeur stock", Double.class),
        
            new ColumnDef("cump", "CUMP", Double.class),
        
            new ColumnDef(
                "methode",
                "Méthode",
                List.of("FIFO", "LIFO", "CUMP")
            )
        ));

        return meta;
    }  
}
