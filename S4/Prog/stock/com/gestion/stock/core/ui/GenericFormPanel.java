package com.gestion.stock.core.ui;

import javax.swing.*;

import com.gestion.stock.core.entity.*;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;


public class GenericFormPanel<T> extends JPanel  {
    private EntityMetadata metadata;

    private Map<String , JComponent> fields = new HashMap<>();

    public JComponent getField(String fieldName) {
        return fields.get(fieldName);
    }

    public GenericFormPanel(EntityMetadata metadata) {
        this.metadata = metadata;
        setLayout(new GridLayout(0, 2));

        buildForm();
    }


    // Constructuion du formulaire
    public void buildForm(){
        for(ColumnDef col : metadata.getColumns()){
            if (!col.isVisible()) continue; 

            JLabel label = new JLabel(col.getLabel());
            JComponent input = createComponent(col);

            fields.put(col.getFieldName(), input);

            add(label);
            add(input);
        }

    }


        // Creation des composant
    public JComponent createComponent(ColumnDef col) {
        // Combobox
        if (col.getComboValues() != null && !col.getComboValues().isEmpty()) {

            JComboBox<String> combo = new JComboBox<>();

            for (String v : col.getComboValues()) {
                combo.addItem(v);
            }

            return combo;
        }

        // 2. TYPES CLASSIQUES
        Class<?> type = col.getType();

        if (type == String.class) {
            return new JTextField();

        } else if (type == Integer.class) {
            return new JSpinner(new SpinnerNumberModel());

        } else if (type == Double.class) {
            return new JSpinner(new SpinnerNumberModel());

        } else if (type == Boolean.class) {
            return new JCheckBox();
        }else if (type == java.util.Date.class) {

            return new JSpinner(new SpinnerDateModel());
        }

        return new JTextField();
    }
    // Recuperer les valeur
    public Object getValue(String fieldName){
        
        JComponent comp = fields.get(fieldName);

        if (comp instanceof JTextField) {
            return ((JTextField) comp).getText();

        } else if (comp instanceof JCheckBox) {
            return ((JCheckBox) comp).isSelected();

        } else if (comp instanceof JSpinner) {
            return ((JSpinner) comp).getValue();
        }

        return null;
    }

    public Map<String, Object> getAllValues() {

    Map<String, Object> values = new HashMap<>();

    for (String key : fields.keySet()) {
        values.put(key, getValue(key));
    }

    return values;
}

}
