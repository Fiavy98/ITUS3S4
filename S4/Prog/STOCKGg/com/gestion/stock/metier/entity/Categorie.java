package com.gestion.stock.metier.entity;

import com.gestion.stock.core.entity.BaseEntity;

public class Categorie extends BaseEntity {

    private Integer moduleId;
    private String code;
    private String libelle;
    private Integer categorieParentId;
    private String description;

    @Override
    public String getTableName() {
        return "categorie";
    }

    @Override
    public String getIdColumnName() {
        return "id";
    }

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Integer getCategorieParentId() {
        return categorieParentId;
    }

    public void setCategorieParentId(Integer categorieParentId) {
        this.categorieParentId = categorieParentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return code + " - " + libelle;
    }
}
