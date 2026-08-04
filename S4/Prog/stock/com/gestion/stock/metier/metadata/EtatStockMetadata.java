package com.gestion.stock.metier.metadata;

import java.util.List;

import com.gestion.stock.core.entity.ColumnDef;
import com.gestion.stock.core.entity.EntityMetadata;

public class EtatStockMetadata {

    public static EntityMetadata build() {

        EntityMetadata meta = new EntityMetadata();

        meta.setTableName("v_etat_stock");

        meta.setColumns(List.of(

            new ColumnDef("date", "Date", String.class),

            new ColumnDef("produit", "Nom du produit", String.class),

            new ColumnDef("type", "Type", String.class),

            new ColumnDef("methode", "Méthode", String.class),

            new ColumnDef("quantite", "Quantité", Integer.class),

            new ColumnDef("pu", "PU", Double.class),

            new ColumnDef("valeur", "Valeur", Double.class),

            new ColumnDef("stockQte", "Stock", Integer.class),

            new ColumnDef("valeurStock", "Valeur Stock", Double.class),

            new ColumnDef("cump", "CUMP", Double.class)

        ));

        return meta;
    }
}