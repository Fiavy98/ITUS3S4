package com.gestion.stock.core.entity;

import java.util.List;

public class EntityMetadata {
    private String tableName;
    private String idColumn;
    private List<ColumnDef> columns;
    
    public EntityMetadata(String tableName, String idColumn, List<ColumnDef> columns) {
        this.tableName = tableName;
        this.idColumn = idColumn;
        this.columns = columns;
    }
    
    // Getters
    public String getTableName() { return tableName; }
    public String getIdColumn() { return idColumn; }
    public List<ColumnDef> getColumns() { return columns; }
}