package row;

import java.util.ArrayList;
import java.util.List;

public class Row {
    private List<Object> values;

    public Row() {
        values = new ArrayList<>();
    }

    public List<Object> getValues() {
        return values;
    }

    public void addValues(List<Object> values) {
        this.values.addAll(values);
    }

    public Object getValue(int index) {
        return values.get(index);
    }
    public void addValue(Object value) {
        values.add(value);
    }
    
}
