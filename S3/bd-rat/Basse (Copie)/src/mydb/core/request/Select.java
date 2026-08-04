package mydb.core.request;

import java.util.ArrayList;
import java.util.List;

import mydb.core.TableManager;

public class Select {
    private String user;
    private String database;
    private String tableName;
    private List<String> columns;
    private Where whereClause;

    public Select(String user, String database, String tableName, List<String> columns, Where whereClause) {
        this.user = user;
        this.database = database;
        this.tableName = tableName;
        this.columns = columns;
        this.whereClause = whereClause;
    }

    public void execute() {
    // Charger les colonnes et toutes les lignes de la table
    List<String> schemaCols = TableManager.getColumns(user, database, tableName);
    List<String[]> rows = TableManager.getAllRows(user, database, tableName);

    if (schemaCols.isEmpty()) {
        System.out.println("Table vide ou inexistante : " + tableName);
        return;
    }

    // Where si présent
    if (whereClause != null) {
        // On passe schemaCols en minuscules pour comparaison case-insensitive
        List<String> schemaLower = new ArrayList<>();
        for (String c : schemaCols) schemaLower.add(c.toLowerCase().trim());
        rows = whereClause.apply(rows, schemaLower);
    }

    // Colonnes à afficher
    List<String> displayCols;
    if (columns.isEmpty() || (columns.size() == 1 && columns.get(0).equals("*"))) {
        displayCols = new ArrayList<>(schemaCols); // toutes les colonnes
    } else {
        displayCols = columns;
    }

    int width = 15; // largeur de colonne
    String lineSep = "+" + "-".repeat(displayCols.size() * (width + 1)) + "+";

    // Entête
    System.out.println(lineSep);
    for (String col : displayCols) {
        System.out.printf("| %-"+width+"s", col.trim());
    }
    System.out.println("|");
    System.out.println(lineSep);

    // Données
    for (String[] row : rows) {
        for (String col : displayCols) {
            int index = schemaCols.indexOf(col.trim()); // trouver l'index réel dans schemaCols
            String val = (index >= 0 && index < row.length) ? row[index].trim() : "";
            System.out.printf("| %-"+width+"s", val);
        }
        System.out.println("|");
    }

    System.out.println(lineSep);
}



}
