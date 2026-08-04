package com.gestion.stock.core.entity;

import java.util.List;

public class ColumnDef {
    private String fieldName;
    private String columnName;
    private String label;
    private Class<?> type;
    private boolean required;
    private boolean editable;
    private boolean visible;
    private List<?> comboValues; // pour JComboBox
    
    // Constructeurs
    public ColumnDef(String fieldName, String label, Class<?> type) {
        this(fieldName, fieldName, label, type, false, true, true);
    }
    
    public ColumnDef(String fieldName, String columnName, String label, Class<?> type) {
        this(fieldName, columnName, label, type, false, true, true);
    }
    
    public ColumnDef(String fieldName, String label, Class<?> type, boolean required, boolean editable, boolean visible) {
        this(fieldName, fieldName, label, type, required, editable, visible);
    }
    
    public ColumnDef(String fieldName, String columnName, String label, Class<?> type, boolean required, boolean editable, boolean visible) {
        this.fieldName = fieldName;
        this.columnName = columnName;
        this.label = label;
        this.type = type;
        this.required = required;
        this.editable = editable;
        this.visible = visible;
    }
    
    // Getters et setters
    public String getFieldName() { return fieldName; }
    public void setFieldName(String fieldName) { this.fieldName = fieldName; }
    public String getColumnName() { return columnName; }
    public void setColumnName(String columnName) { this.columnName = columnName; }
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public Class<?> getType() { return type; }
    public void setType(Class<?> type) { this.type = type; }
    public boolean isRequired() { return required; }
    public void setRequired(boolean required) { this.required = required; }
    public boolean isEditable() { return editable; }
    public void setEditable(boolean editable) { this.editable = editable; }
    public boolean isVisible() { return visible; }
    public void setVisible(boolean visible) { this.visible = visible; }
    public List<?> getComboValues() { return comboValues; }
    public void setComboValues(List<?> comboValues) { this.comboValues = comboValues; }
}