package com.gestion.stock.core.ui;

import com.gestion.stock.core.entity.ColumnDef;

import javax.swing.table.AbstractTableModel;
import java.lang.reflect.Field;
import java.util.List;

public class GenericTableModel<T> extends AbstractTableModel {
    private List<T> data;
    private List<ColumnDef> columns;
    private Class<T> entityClass;
    
    public GenericTableModel(List<T> data, List<ColumnDef> columns, Class<T> entityClass) {
        this.data = data;
        this.columns = columns.stream().filter(ColumnDef::isVisible).toList();
        this.entityClass = entityClass;
    }
    
    @Override
    public int getRowCount() { return data.size(); }
    
    @Override
    public int getColumnCount() { return columns.size(); }
    
    @Override
    public String getColumnName(int column) { return columns.get(column).getLabel(); }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        T entity = data.get(rowIndex);
        String fieldName = columns.get(columnIndex).getFieldName();
        try {
            Field field = resolveField(entityClass, fieldName);
            if (field == null) return null;
            field.setAccessible(true);
            return field.get(entity);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }
    
    public T getEntityAt(int row) { return data.get(row); }
    
    public void setData(List<T> newData) {
        this.data = newData;
        fireTableDataChanged();
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
}