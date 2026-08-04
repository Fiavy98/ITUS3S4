package mydb.core.request;

import java.util.ArrayList;
import java.util.List;
import mydb.core.TableManager;

public class Join {

    private String user;
    private String database;
    private String table1;
    private String table2;
    private String col1;
    private String col2;
    private List<String> displayCols;

    public Join(String user, String database, String table1, String table2,
                String col1, String col2, List<String> displayCols) {
        this.user = user;
        this.database = database;
        this.table1 = table1;
        this.table2 = table2;
        this.col1 = col1.toLowerCase();
        this.col2 = col2.toLowerCase();
        this.displayCols = displayCols;
    } 

    public void execute() {
    // Charger les tables
    List<String> cols1 = TableManager.getColumns(user, database, table1);
    List<String> cols2 = TableManager.getColumns(user, database, table2);

    List<String[]> rows1 = TableManager.getAllRows(user, database, table1);
    List<String[]> rows2 = TableManager.getAllRows(user, database, table2);

    // Colonnes en minuscules et trim
    List<String> cols1Lower = new ArrayList<>();
    for (String c : cols1) cols1Lower.add(c.toLowerCase().trim());

    List<String> cols2Lower = new ArrayList<>();
    for (String c : cols2) cols2Lower.add(c.toLowerCase().trim());

    int idx1 = cols1Lower.indexOf(col1.toLowerCase().trim());
    int idx2 = cols2Lower.indexOf(col2.toLowerCase().trim());

    if (idx1 == -1 || idx2 == -1) {
        System.out.println("Colonne de jointure inexistante");
        return;
    }

    // Si displayCols est vide, prendre toutes les colonnes
    if (displayCols == null || displayCols.isEmpty()) {
        displayCols = new ArrayList<>();
        for (String c : cols1) displayCols.add(table1 + "." + c);
        for (String c : cols2) displayCols.add(table2 + "." + c);
    }

    int width = 15;

    // En-tête
    System.out.println("+" + "-".repeat(displayCols.size() * (width + 1)) + "+");
    for (String c : displayCols) {
        System.out.printf("| %-"+width+"s", c);
    }
    System.out.println("|");
    System.out.println("+" + "-".repeat(displayCols.size() * (width + 1)) + "+");

    // INNER JOIN
    for (String[] r1 : rows1) {
        for (String[] r2 : rows2) {
            if (r1[idx1].trim().equals(r2[idx2].trim())) {
                for (String col : displayCols) {
                    String[] parts = col.split("\\.");
                    String tbl = parts[0];
                    String cname = parts[1];
                    int i = tbl.equalsIgnoreCase(table1) ?
                            cols1Lower.indexOf(cname.toLowerCase().trim()) :
                            cols2Lower.indexOf(cname.toLowerCase().trim());
                    System.out.printf("| %-"+width+"s", i >= 0 ? (tbl.equalsIgnoreCase(table1) ? r1[i].trim() : r2[i].trim()) : "");
                }
                System.out.println("|");
            }
        }
    }

    System.out.println("+" + "-".repeat(displayCols.size() * (width + 1)) + "+");
}

}


