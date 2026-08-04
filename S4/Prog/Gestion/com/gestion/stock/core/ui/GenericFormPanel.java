package com.gestion.stock.core.ui;

import com.gestion.stock.core.entity.BaseEntity;
import com.gestion.stock.core.entity.ColumnDef;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.toedter.calendar.JDateChooser;

public class GenericFormPanel<T extends BaseEntity> extends JPanel {
    protected T entity;
    protected List<ColumnDef> columns;
    protected Map<String, JComponent> fieldComponents = new HashMap<>();
    protected Class<T> entityClass;
    
    public GenericFormPanel(T entity, List<ColumnDef> columns, Class<T> entityClass) {
        this.entity = entity;
        this.columns = columns;
        this.entityClass = entityClass;
        initUI();
        bindEntityToForm();
    }
    
    private void initUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        int row = 0;
        for (ColumnDef col : columns) {
            if (!col.isEditable()) continue;
            
            gbc.gridx = 0; gbc.gridy = row;
            JLabel label = new JLabel(col.getLabel() + (col.isRequired() ? " *" : " :"));
            add(label, gbc);
            
            gbc.gridx = 1;
            JComponent field = createField(col);
            fieldComponents.put(col.getFieldName(), field);
            
            // Largeur préférée pour les champs de texte
            if (field instanceof JTextField) {
                ((JTextField) field).setColumns(20);
            } else if (field instanceof JComboBox) {
                ((JComboBox<?>) field).setPreferredSize(new Dimension(200, 25));
            }
            add(field, gbc);
            
            row++;
        }
    }
    
    private JComponent createField(ColumnDef col) {
        if (col.getComboValues() != null) {
            JComboBox<Object> combo = new JComboBox<>(col.getComboValues().toArray());
            return combo;
        }
        Class<?> type = col.getType();
        if (type == String.class) {
            JTextField tf = new JTextField();
            if (!col.isEditable()) tf.setEditable(false);
            return tf;
        } else if (type == Integer.class || type == int.class) {
            JSpinner spinner = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
            return spinner;
        } else if (type == BigDecimal.class) {
            JFormattedTextField ftf = new JFormattedTextField();
            ftf.setColumns(15);
            return ftf;
        } else if (type == LocalDate.class) {
            return new JDateChooser();
        } else if (type == Boolean.class || type == boolean.class) {
            return new JCheckBox();
        } else {
            return new JTextField(15);
        }
    }
    
    protected void bindEntityToForm() {
        for (ColumnDef col : columns) {
            if (!col.isEditable()) continue;
            JComponent field = fieldComponents.get(col.getFieldName());
            Object value = getFieldValue(entity, col.getFieldName());
            setFieldValue(field, value);
        }
    }
    
    public void updateEntityFromForm() {
        for (ColumnDef col : columns) {
            if (!col.isEditable()) continue;
            JComponent field = fieldComponents.get(col.getFieldName());
            Object value = getFieldValueFromComponent(field);
            setFieldValue(entity, col.getFieldName(), value);
        }
    }
    
    private Object getFieldValue(T entity, String fieldName) {
        try {
            Field f = resolveField(entityClass, fieldName);
            if (f == null) return null;
            f.setAccessible(true);
            return f.get(entity);
        } catch (Exception e) {
            return null;
        }
    }
    
    private void setFieldValue(T entity, String fieldName, Object value) {
        try {
            Field f = resolveField(entityClass, fieldName);
            if (f == null) return;
            f.setAccessible(true);
            f.set(entity, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void setFieldValue(JComponent field, Object value) {
        if (value == null) return;
        if (field instanceof JTextField) {
            ((JTextField) field).setText(value.toString());
        } else if (field instanceof JSpinner) {
            ((JSpinner) field).setValue(value);
        } else if (field instanceof JFormattedTextField) {
            ((JFormattedTextField) field).setText(value.toString());
        } else if (field instanceof JDateChooser && value instanceof LocalDate) {
            java.util.Date date = java.sql.Date.valueOf((LocalDate) value);
            ((JDateChooser) field).setDate(date);
        } else if (field instanceof JCheckBox) {
            ((JCheckBox) field).setSelected((Boolean) value);
        } else if (field instanceof JComboBox) {
            ((JComboBox<?>) field).setSelectedItem(value);
        }
    }
    
    private Object getFieldValueFromComponent(JComponent field) {
        if (field instanceof JTextField) {
            return ((JTextField) field).getText();
        } else if (field instanceof JSpinner) {
            return ((JSpinner) field).getValue();
        } else if (field instanceof JFormattedTextField) {
            String text = ((JFormattedTextField) field).getText();
            try {
                return new BigDecimal(text);
            } catch (NumberFormatException e) {
                return BigDecimal.ZERO;
            }
        } else if (field instanceof JDateChooser) {
            java.util.Date date = ((JDateChooser) field).getDate();
            if (date != null) return new java.sql.Date(date.getTime()).toLocalDate();
            return null;
        } else if (field instanceof JCheckBox) {
            return ((JCheckBox) field).isSelected();
        } else if (field instanceof JComboBox) {
            return ((JComboBox<?>) field).getSelectedItem();
        }
        return null;
    }
    
    public boolean validateForm() {
        for (ColumnDef col : columns) {
            if (col.isRequired()) {
                JComponent field = fieldComponents.get(col.getFieldName());
                Object value = getFieldValueFromComponent(field);
                if (value == null || (value instanceof String && ((String) value).trim().isEmpty())) {
                    JOptionPane.showMessageDialog(this, "Le champ " + col.getLabel() + " est obligatoire.", "Validation", JOptionPane.WARNING_MESSAGE);
                    return false;
                }
            }
        }
        return true;
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