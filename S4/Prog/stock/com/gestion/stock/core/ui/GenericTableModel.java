package com.gestion.stock.core.ui;

import javax.swing.table.AbstractTableModel;

import com.gestion.stock.core.entity.*;

import java.util.List;


public class GenericTableModel<T> extends AbstractTableModel {
    private EntityMetadata metadata;
    private List<T> data;
    

    public GenericTableModel(EntityMetadata metadata, List<T> data){
        this.metadata=metadata;
        this.data=data;
    }

    // nb de lignes
    @Override
    public  int getRowCount(){
        return data.size();
    } 

    // nb colonne
    @Override
    public int getColumnCount() {
    
        int count = 0;
    
        for (ColumnDef col : metadata.getColumns()) {
            if (col.isVisible()) {
                count++;
            }
        }
    
        return count;
    }
        // nom de colonne
    @Override
    public String getColumnName(int column) {
    
        return getVisibleColumns().get(column).getLabel();
    }

    // Valeur d'une cellule
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        T entity = data.get(rowIndex);

        ColumnDef col = getVisibleColumns().get(columnIndex);

        return getFieldValue(entity, col.getFieldName());
    }

    // ================= REFLECTION =================
    private Object getFieldValue(T entity, String fieldName) {
        try {
            var field = entity.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(entity);
        } catch (Exception e) {
            return null;
        }
    }

    private List<ColumnDef> getVisibleColumns() {

    return metadata.getColumns()
            .stream()
            .filter(ColumnDef::isVisible)
            .toList();
    }

}
