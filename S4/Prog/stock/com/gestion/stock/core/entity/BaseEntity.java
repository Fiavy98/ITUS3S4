package com.gestion.stock.core.entity;

import java.time.LocalDateTime;

public abstract class BaseEntity {
    protected Integer id;


    public BaseEntity(){
    }

    public Integer id(){
        return id;
    }

    public void setId(Integer id){
        this.id=id;
    }


    
}
