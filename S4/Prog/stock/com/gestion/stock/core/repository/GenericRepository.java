package com.gestion.stock.core.repository;

import com.gestion.stock.core.entity.ColumnDef;
import com.gestion.stock.core.entity.EntityMetadata;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GenericRepository<T> {

    private Connection connection;
    private EntityMetadata metadata;
    private Class<T> type;

    public GenericRepository(Connection connection,
                             EntityMetadata metadata,
                             Class<T> type) {
        this.connection = connection;
        this.metadata = metadata;
        this.type = type;
    }

    // ================= CREATE =================
    public void save(T entity) throws SQLException {

        // ❌ on ignore les champs non éditables (ex: id SERIAL)
        List<ColumnDef> cols = metadata.getColumns()
                .stream()
                .filter(ColumnDef::isEditable)
                .toList();

        StringBuilder sql = new StringBuilder("INSERT INTO ");
        sql.append(metadata.getTableName()).append(" (");

        for (int i = 0; i < cols.size(); i++) {
            sql.append(cols.get(i).getFieldName());
            if (i < cols.size() - 1) sql.append(", ");
        }

        sql.append(") VALUES (");

        for (int i = 0; i < cols.size(); i++) {
            sql.append("?");
            if (i < cols.size() - 1) sql.append(", ");
        }

        sql.append(")");

        PreparedStatement ps = connection.prepareStatement(sql.toString());

        int index = 1;
        for (ColumnDef col : cols) {
            Object value = getFieldValue(entity, col.getFieldName());
            ps.setObject(index++, value);
        }

        ps.executeUpdate();
    }

    // ================= READ ALL =================
    public List<T> findAll() throws SQLException {

        List<T> list = new ArrayList<>();

        String sql = "SELECT * FROM " + metadata.getTableName();

        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            list.add(mapResultSetToEntity(rs));
        }

        return list;
    }

    // ================= READ BY ID =================
    public T findById(Object id) throws SQLException {

        String sql = "SELECT * FROM " + metadata.getTableName()
                + " WHERE " + metadata.getIdField() + " = ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setObject(1, id);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return mapResultSetToEntity(rs);
        }

        return null;
    }

    // ================= UPDATE =================
    public void update(T entity) throws SQLException {

        List<ColumnDef> cols = metadata.getColumns()
                .stream()
                .filter(ColumnDef::isEditable)
                .toList();

        StringBuilder sql = new StringBuilder("UPDATE ");
        sql.append(metadata.getTableName()).append(" SET ");

        for (int i = 0; i < cols.size(); i++) {
            sql.append(cols.get(i).getFieldName()).append(" = ?");
            if (i < cols.size() - 1) sql.append(", ");
        }

        sql.append(" WHERE ")
                .append(metadata.getIdField())
                .append(" = ?");

        PreparedStatement ps = connection.prepareStatement(sql.toString());

        int i = 1;
        for (ColumnDef col : cols) {
            ps.setObject(i++, getFieldValue(entity, col.getFieldName()));
        }

        ps.setObject(i, getFieldValue(entity, metadata.getIdField()));

        ps.executeUpdate();
    }

    // ================= DELETE =================
    public void delete(Object id) throws SQLException {

        String sql = "DELETE FROM " + metadata.getTableName()
                + " WHERE " + metadata.getIdField() + " = ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setObject(1, id);

        ps.executeUpdate();
    }

    // ================= REFLECTION GET FIELD =================
    private Object getFieldValue(T entity, String fieldName) {
        try {
            var field = type.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(entity);
        } catch (Exception e) {
            throw new RuntimeException("Erreur accès champ: " + fieldName, e);
        }
    }

    // ================= MAP RESULTSET =================
    private T mapResultSetToEntity(ResultSet rs) {

        try {
            T obj = type.getDeclaredConstructor().newInstance();

            for (ColumnDef col : metadata.getColumns()) {

                try {
                    var field = type.getDeclaredField(col.getFieldName());
                    field.setAccessible(true);

                    Object value = rs.getObject(col.getFieldName());

                    field.set(obj, value);

                } catch (NoSuchFieldException ignored) {
                    // si colonne non présente dans entity
                }
            }

            return obj;

        } catch (Exception e) {
            throw new RuntimeException("Mapping error", e);
        }
    }
}