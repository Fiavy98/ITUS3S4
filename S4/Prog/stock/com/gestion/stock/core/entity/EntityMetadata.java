package com.gestion.stock.core.entity;
import java.util.List;

public class EntityMetadata {
    private String tableName;
    // Titre dans ui
    private String label; 
    
    private String IdField;
    // ls de colonne
    private List<ColumnDef> columns;


    public EntityMetadata(){}

    public EntityMetadata(String tableName,String  label,String IdField,List<ColumnDef> columns){
        this.tableName=tableName;
        this.label=label;
        this.IdField=IdField;
        this.columns=columns;
    }

        public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getIdField() {
        return IdField;
    }

    public void setIdField(String IdField) {
        this.IdField = IdField;
    }

    public List<ColumnDef> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnDef> columns) {
        this.columns = columns;
    }

    // Retourner le clonne par son nom de champ
    public ColumnDef getColumn(String FieldName){
        for(ColumnDef col : columns){
            if(col.getFieldName().equals(FieldName)){
                return col;
            }
        }

        return null;
    }


}
