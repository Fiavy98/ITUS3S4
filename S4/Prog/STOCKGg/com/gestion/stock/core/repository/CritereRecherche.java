package com.gestion.stock.core.repository;

import java.util.ArrayList;
import java.util.List;

public class CritereRecherche {
    private List<Condition> conditions = new ArrayList<>();
    
    public CritereRecherche addEquals(String field, Object value) {
        conditions.add(new Condition(field, "=", value));
        return this;
    }
    
    public CritereRecherche addLike(String field, String value) {
        conditions.add(new Condition(field, "LIKE", "%" + value + "%"));
        return this;
    }
    
    public CritereRecherche addGreaterThan(String field, Object value) {
        conditions.add(new Condition(field, ">", value));
        return this;
    }
    
    public String getWhereClause() {
        if (conditions.isEmpty()) return "";
        StringBuilder sb = new StringBuilder(" WHERE ");
        for (int i = 0; i < conditions.size(); i++) {
            if (i > 0) sb.append(" AND ");
            sb.append(conditions.get(i).field).append(" ").append(conditions.get(i).operator).append(" ?");
        }
        return sb.toString();
    }
    
    public Object[] getParameters() {
        return conditions.stream().map(c -> c.value).toArray();
    }
    
    private static class Condition {
        String field;
        String operator;
        Object value;
        Condition(String field, String operator, Object value) {
            this.field = field; this.operator = operator; this.value = value;
        }
    }
}