package com.gestion.stock.core.service;

import com.gestion.stock.core.entity.BaseEntity;
import com.gestion.stock.core.repository.GenericRepository;
import com.gestion.stock.exception.BusinessException;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public abstract class GenericService<T extends BaseEntity> {
    protected final GenericRepository<T> repository;
    
    public GenericService(GenericRepository<T> repository) {
        this.repository = repository;
    }
    
    public T save(T entity) throws BusinessException {
        try {
            validateBeforeSave(entity);
            return repository.save(entity);
        } catch (SQLException e) {
            throw new BusinessException("Erreur lors de la sauvegarde", e);
        }
    }
    
    public Optional<T> findById(int id) throws BusinessException {
        try {
            return repository.findById(id);
        } catch (SQLException e) {
            throw new BusinessException("Erreur lors de la recherche", e);
        }
    }
    
    public List<T> findAll() throws BusinessException {
        try {
            return repository.findAll();
        } catch (SQLException e) {
            throw new BusinessException("Erreur lors du chargement", e);
        }
    }
    
    public void deleteById(int id) throws BusinessException {
        try {
            repository.deleteById(id);
        } catch (SQLException e) {
            throw new BusinessException("Erreur lors de la suppression", e);
        }
    }
    
    protected abstract void validateBeforeSave(T entity) throws BusinessException;
}