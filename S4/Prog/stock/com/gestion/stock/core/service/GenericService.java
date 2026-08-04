package com.gestion.stock.core.service;

import java.util.List;

import com.gestion.stock.core.repository.*;

public class GenericService<T> {
    private GenericRepository<T> repository;

    public GenericService(GenericRepository<T> repository) {
        this.repository = repository;
    }


    public void save(T entity) throws Exception{
        repository.save(entity);
    }

    public void update(T entity) throws Exception {
        repository.update(entity);
    }

    public void delete(Object id) throws Exception {
        repository.delete(id);
    }

    public List<T> findAlll() throws Exception {
        return repository.findAll();
    }

    
    public T findById(Object id) throws Exception {
        return repository.findById(id);
    }
    
}
