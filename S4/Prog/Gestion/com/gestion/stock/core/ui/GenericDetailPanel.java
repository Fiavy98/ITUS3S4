package com.gestion.stock.core.ui;

import com.gestion.stock.core.entity.BaseEntity;
import com.gestion.stock.core.entity.ColumnDef;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.List;

public class GenericDetailPanel<T extends BaseEntity> extends JPanel {
	private final List<ColumnDef> columns;
	private final Class<T> entityClass;
	private T entity;
    
	public GenericDetailPanel(List<ColumnDef> columns, Class<T> entityClass) {
		this.columns = columns;
		this.entityClass = entityClass;
		setLayout(new GridBagLayout());
	}
    
	public void setEntity(T entity) {
		this.entity = entity;
		removeAll();
		render();
		revalidate();
		repaint();
	}
    
	private void render() {
		if (entity == null) return;
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(4, 6, 4, 6);
		gbc.anchor = GridBagConstraints.WEST;
		int row = 0;
		for (ColumnDef col : columns) {
			if (!col.isVisible()) continue;
			gbc.gridx = 0; gbc.gridy = row;
			add(new JLabel(col.getLabel() + " :"), gbc);
			gbc.gridx = 1;
			add(new JLabel(String.valueOf(getFieldValue(col.getFieldName()))), gbc);
			row++;
		}
	}
    
	private Object getFieldValue(String fieldName) {
		try {
			Field f = resolveField(entityClass, fieldName);
			if (f == null) return null;
			f.setAccessible(true);
			return f.get(entity);
		} catch (Exception e) {
			return null;
		}
	}
    
	private Field resolveField(Class<?> type, String fieldName) {
		Class<?> current = type;
		while (current != null) {
			try {
				return current.getDeclaredField(fieldName);
			} catch (NoSuchFieldException ignored) {
				current = current.getSuperclass();
			}
		}
		return null;
	}
}
