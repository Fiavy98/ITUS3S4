package com.gestion.stock.core.entity;

import java.util.List;

public class ColumnDef {

    private String fieldName;
    private String label;

    private Class<?> type;

    private boolean visible = true;
    private boolean editable = true;

    // pour JComboBox
    private List<String> comboValues;

    // ================= CONSTRUCTOR SIMPLE =================
    public ColumnDef(String fieldName, String label, Class<?> type) {
        this.fieldName = fieldName;
        this.label = label;
        this.type = type;
    }

    // ================= FULL CONSTRUCTOR =================
    public ColumnDef(String fieldName,
                     String label,
                     Class<?> type,
                     boolean visible,
                     boolean editable) {
        this.fieldName = fieldName;
        this.label = label;
        this.type = type;
        this.visible = visible;
        this.editable = editable;
    }

    // ================= COMBO CONSTRUCTOR =================
    public ColumnDef(String fieldName,
                     String label,
                     List<String> comboValues) {
        this.fieldName = fieldName;
        this.label = label;
        this.comboValues = comboValues;
        this.type = String.class; // important
    }

    // ================= GETTERS =================

    public String getFieldName() {
        return fieldName;
    }

    public String getLabel() {
        return label;
    }

    public Class<?> getType() {
        return type;
    }

    public boolean isVisible() {
        return visible;
    }

    public boolean isEditable() {
        return editable;
    }

    public List<String> getComboValues() {
        return comboValues;
    }

    // ================= HELPERS =================

    public boolean isCombo() {
        return comboValues != null && !comboValues.isEmpty();
    }
}