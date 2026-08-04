package mydb.core.request;

import java.util.ArrayList;
import java.util.List;

public class Where {

    private String column;
    private String operator;
    private String value;

    public Where(String column, String operator, String value) {
        this.column = column.toLowerCase().trim(); 
        this.operator = operator;
        this.value = value.replace("'", "").trim();
    }



    public List<String[]> apply(List<String[]> rows, List<String> schemaCols) {
        List<String[]> filtered = new ArrayList<>();

            int colIndex = schemaCols.indexOf(column); // schemaCols doit aussi être en minuscules
            if (colIndex == -1) {
            System.out.println("Colonne WHERE inexistante : " + column);
            return filtered;
        }

    for (String[] row : rows) {
        if (colIndex < row.length && match(row[colIndex])) {
            filtered.add(row);
        }
    }

    return filtered;
}


        private boolean match(String cell) {
        cell = cell.replace("'", "").trim();

        switch (operator) {
            case "=":
                return cell.equals(value);

            case ">":
                return compareInt(cell) > 0;

            case "<":
                return compareInt(cell) < 0;

            default:
                System.out.println("Opérateur WHERE non supporté : " + operator);
                return false;
        }
    }

        private int compareInt(String cell) {
        try {
            int a = Integer.parseInt(cell);
            int b = Integer.parseInt(value);
            return Integer.compare(a, b);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

}
