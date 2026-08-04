package com.gestion.stock.core.repository;

public class CritereRecherche {
    private String field;
    private String operator; // (=, >, <, LIKE, >=, <=)
    private Object value; // Valeur a comparer

    public CritereRecherche() {
    }

    public CritereRecherche(String field, String operator, Object value) {
        this.field = field;
        this.operator = operator;
        this.value = value;
    }

    
}
