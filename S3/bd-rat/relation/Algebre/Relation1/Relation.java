package relation;

import attribute.Attribute;
import row.Row;
import java.util.ArrayList;
import java.util.List;

public class Relation {
    private List<Attribute> attributes;
    private List<Row> rows;

    public Relation() {
        attributes = new ArrayList<>();
        rows = new ArrayList<>();
    }

    public List<Row> getRows() {
        return rows;
    }

    public static Attribute createAttribute(String attributeName, Class<?> attributeType) {
        return new Attribute(attributeName, attributeType);
    }

    public Attribute addAttribute(Attribute attribute) {
        attributes.add(attribute);
        return attribute;
    }

    public Relation projection(String... attributeNames) {
        Relation result = new Relation();
        for (String name : attributeNames) {
            int index = indexAttribute(name);
            if (index != -1) {
                result.addAttribute(attributes.get(index));
            }
        }
        for (Row row : rows) {  
            Row newRow = new Row();
            for (String name : attributeNames) {
                int index = indexAttribute(name);
                if (index != -1) {
                    newRow.addValue(row.getValue(index));
                }
            }
            result.addRow(newRow.getValues().toArray());
        }
        return result;
    }

    public Relation selection(String attributeName, Object value) {
        Relation result = new Relation();
        result.attributes = new ArrayList<>(this.attributes);
        for (Row row : rows) {
            if (row.getValue(indexAttribute(attributeName)).equals(value)) {
                result.addRow(row.getValues().toArray());
            }
        }
        return result;
    }

    public Relation union(Relation other) {
        Relation result = new Relation();
        result.attributes = new ArrayList<>(this.attributes);
        result.rows.addAll(this.rows);
        for (Row row : other.rows) {
            if (!this.rows.contains(row)) {
                result.addRow(row.getValues().toArray());
            }
        }
        return result;
    }

    public Relation difference(Relation other) {
        Relation result = new Relation();
        result.attributes = new ArrayList<>(this.attributes);
        for (Row row : this.rows) {
            if (!other.rows.contains(row)) {
                result.addRow(row.getValues().toArray());
            }
        }
        return result;
    }

    public Relation intersection(Relation other) {
        Relation result = new Relation();
        result.attributes = new ArrayList<>(this.attributes);
        for (Row row : this.rows) {
            if (other.rows.contains(row)) {
                result.addRow(row.getValues().toArray());
            }
        }
        return result;
    }

    public Relation cartesianProduct(Relation other) {
        Relation result = new Relation();
        for (Attribute attr : this.attributes) {
            result.addAttribute(attr);
        }
        for (Attribute attr : other.attributes) {
            result.addAttribute(attr);
        }
        for (Row rowA : this.rows) {
            for (Row rowB : other.rows) {
                Row newRow = new Row();
                newRow.addValues(rowA.getValues()); // Ajout des valeurs de la première ligne
                newRow.addValues(rowB.getValues()); // Ajout des valeurs de la deuxième ligne
                result.addRow(newRow.getValues().toArray());
            }
        }
        return result;
    }

    public int indexAttribute(String attributeName) {
        for (int i = 0; i < attributes.size(); i++) {
            if (attributes.get(i).getAttributeName().equals(attributeName)) {
                return i;
            }
        }
        return -1;
    }

    public void addRow(Object... values) {
        Row row = new Row();
        for (Object value : values) {
            row.addValue(value);
        }
        rows.add(row);
    }

    public void afficheRow() {
        for (Attribute attribute : attributes) {
            System.out.print("\t" + attribute.getAttributeName() + " |");
        }
        System.out.println("\n ================================");
        for (Row row : rows) {
            System.out.print("Row: ");
            for (int i = 0; i < attributes.size(); i++) {
                System.out.print(row.getValue(i) + "\t");
            }
            System.out.println();
        }
    }
}
