package com.gestion.stock.core.repository;

import com.gestion.stock.config.DatabaseConfig;
import com.gestion.stock.core.entity.BaseEntity;
import com.gestion.stock.core.entity.ColumnDef;

import java.lang.reflect.Field;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class GenericRepository<T extends BaseEntity> {
    protected static final Logger LOGGER = Logger.getLogger(GenericRepository.class.getName());
    protected final Class<T> entityClass;
    
    public GenericRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }
    
    protected abstract T mapResultSetToEntity(ResultSet rs) throws SQLException;
    protected abstract void setInsertParameters(PreparedStatement ps, T entity) throws SQLException;
    protected abstract void setUpdateParameters(PreparedStatement ps, T entity) throws SQLException;
    protected abstract String getTableName();
    protected abstract String getIdColumnName();
    protected abstract List<ColumnDef> getColumns();
    
    // CRUD générique de base
    public T save(T entity) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection()) {
            return save(entity, conn);
        }
    }
    
    public T insert(T entity) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection()) {
            return insert(entity, conn);
        }
    }
    
    public T save(T entity, Connection conn) throws SQLException {
        if (entity.getId() == null) {
            return insert(entity, conn);
        }
        update(entity, conn);
        return entity;
    }
    
    public T insert(T entity, Connection conn) throws SQLException {
        StringBuilder sql = new StringBuilder("INSERT INTO " + getTableName() + " (");
        StringBuilder values = new StringBuilder(" VALUES (");
        List<ColumnDef> columns = getColumns();
        List<Object> valuesList = new ArrayList<>();
        boolean hasCreatedAt = hasColumnName(columns, "created_at");
        
        for (ColumnDef col : columns) {
            if (!col.getFieldName().equals(getIdColumnName()) && col.isEditable()) {
                sql.append(col.getColumnName()).append(",");
                values.append("?,");
                valuesList.add(getFieldValue(entity, col.getFieldName()));
            }
        }
        // Ajout createdAt
        if (hasCreatedAt) {
            sql.append("created_at)");
            values.append("?)");
            valuesList.add(LocalDateTime.now());
        } else {
            sql.append(")");
            values.append(")");
        }
        
        String finalSql = sql.toString().replace(",)", ")") + values.toString().replace(",)", ")");

        try (PreparedStatement ps = conn.prepareStatement(finalSql, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < valuesList.size(); i++) {
                ps.setObject(i + 1, valuesList.get(i));
            }
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    entity.setId(rs.getInt(1));
                }
            }
        }
        return entity;
    }
    
    public void update(T entity) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection()) {
            update(entity, conn);
        }
    }
    
    public void update(T entity, Connection conn) throws SQLException {
        StringBuilder sql = new StringBuilder("UPDATE " + getTableName() + " SET ");
        List<ColumnDef> columns = getColumns();
        List<Object> valuesList = new ArrayList<>();
        boolean hasUpdatedAt = hasColumnName(columns, "updated_at");
        
        for (ColumnDef col : columns) {
            if (!col.getFieldName().equals(getIdColumnName()) && col.isEditable()) {
                sql.append(col.getColumnName()).append(" = ?,");
                valuesList.add(getFieldValue(entity, col.getFieldName()));
            }
        }
        if (hasUpdatedAt) {
            sql.append("updated_at = ? WHERE ").append(getIdColumnName()).append(" = ?");
            valuesList.add(LocalDateTime.now());
        } else {
            sql.append(" WHERE ").append(getIdColumnName()).append(" = ?");
        }
        valuesList.add(entity.getId());
        
        String finalSql = sql.toString().replace(", WHERE", " WHERE");

        try (PreparedStatement ps = conn.prepareStatement(finalSql)) {
            for (int i = 0; i < valuesList.size(); i++) {
                ps.setObject(i + 1, valuesList.get(i));
            }
            ps.executeUpdate();
        }
    }
    
    public Optional<T> findById(int id) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection()) {
            return findById(id, conn);
        }
    }
    
    public Optional<T> findById(int id, Connection conn) throws SQLException {
        String sql = "SELECT * FROM " + getTableName() + " WHERE " + getIdColumnName() + " = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEntity(rs));
                }
            }
        }
        return Optional.empty();
    }
    
    public List<T> findAll() throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection()) {
            return findAll(conn);
        }
    }
    
    public List<T> findAll(Connection conn) throws SQLException {
        List<T> list = new ArrayList<>();
        String sql = "SELECT * FROM " + getTableName() + " ORDER BY " + getIdColumnName();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToEntity(rs));
            }
        }
        return list;
    }
    
    public void deleteById(int id) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection()) {
            deleteById(id, conn);
        }
    }
    
    public void deleteById(int id, Connection conn) throws SQLException {
        String sql = "DELETE FROM " + getTableName() + " WHERE " + getIdColumnName() + " = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
    
    // Helper pour lire les champs via reflection
    private Object getFieldValue(T entity, String fieldName) {
        try {
            Field field = resolveField(entity.getClass(), fieldName);
            if (field == null) {
                return null;
            }
            field.setAccessible(true);
            return field.get(entity);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            LOGGER.log(Level.WARNING, "Cannot access field: " + fieldName, e);
            return null;
        }
    }
    
    private Field resolveField(Class<?> type, String fieldName) throws NoSuchFieldException {
        Class<?> current = type;
        while (current != null) {
            try {
                return current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ignored) {
                current = current.getSuperclass();
            }
        }
        throw new NoSuchFieldException(fieldName);
    }
    
    private boolean hasColumnName(List<ColumnDef> columns, String columnName) {
        for (ColumnDef col : columns) {
            if (columnName.equalsIgnoreCase(col.getColumnName())) return true;
        }
        return false;
    }
}