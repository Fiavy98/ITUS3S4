package mydb.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;

public class TableManager {
    public static boolean tableExists(String user, String database, String tableName) {
        File tableDir = new File("data/database/" + user + "/" + database + "/" + tableName);
        return tableDir.exists() && tableDir.isDirectory();
    }

    public static boolean createTbl(String user,String database,String tableName,Map<String, String> columns){
        File tableDir = new File("data/database/" + user + "/" + database + "/" + tableName);

        if (tableDir.exists()) {
            return false;
        }

        tableDir.mkdirs();

        File schemaFile = new File(tableDir, "schema.tbl");
        File dataFile = new File(tableDir, "data.tbl");

                try (FileWriter schemaWriter = new FileWriter(schemaFile)) {
            for (Map.Entry<String, String> col : columns.entrySet()) {
                schemaWriter.write(col.getKey() + ":" + col.getValue() + "\n");
            }
        } catch (IOException e) {
            return false;
        }

        try {
            dataFile.createNewFile();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    //Lire le fichier schema.tbl
    public static Map<String, String> readSchema(File schemaFile) throws IOException {
        Map<String, String> schema = new LinkedHashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(schemaFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    schema.put(parts[0].trim(), parts[1].trim().toUpperCase());
                }
            }
        }
        return schema;
    }

    public static boolean validateColumns(Map<String, String> schema, String[] columns, String[] values) {
        if (columns.length != values.length) return false;
        
        for (int i = 0; i < columns.length; i++) {
            String colName = columns[i].trim();
            String value = values[i].trim();
            
            if (!schema.containsKey(colName)) {
                System.out.println("Colonne inexistante : " + colName);
                return false;
            }
        
            String type = schema.get(colName);
        
            if (type.equals("INT")) {
                try {
                    Integer.parseInt(value.replace("'", "")); // enlève les quotes autour des TEXT
                } catch (NumberFormatException e) {
                    System.out.println("Valeur invalide pour INT : " + value);
                    return false;
                }
            }
            // Pour TEXT, on peut accepter n’importe quelle chaîne
        }
    
        return true;
    }



    public static boolean insertRow(String user, String database, String tableName, String[] columns, String[] values) {
        File tableDir = new File("data/database/" + user + "/" + database + "/" + tableName);
        if (!tableDir.exists()) return false;

        File schemaFile = new File(tableDir, "schema.tbl");
        File dataFile = new File(tableDir, "data.tbl");

        try {
        // Lire le schema pour vérifier les colonnes et les types
        Map<String, String> schema = readSchema(schemaFile);
        if (!validateColumns(schema, columns, values)) return false;

        // Créer la ligne
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            line.append(values[i].trim());
            if (i < values.length - 1) line.append("|");
        }

        // Ajouter dans data.tbl
        try (FileWriter writer = new FileWriter(dataFile, true)) {
            writer.write(line.toString() + "\n");
        }

        return true;
    } catch (IOException e) {
        return false;
    }

    }

    public static boolean dropTable(String user, String database, String tableName) {
        File tableDir = new File(
            "data/database/" + user + "/" + database + "/" + tableName
        );

        if (!tableDir.exists()) return false;

        File[] files = tableDir.listFiles();
        if (files != null) {
            for (File f : files) {
                if (!f.delete()) return false;
            }
        }
        return tableDir.delete();
    }

    public static boolean deleteTable(String user, String database, String tableName) {
        File dataFile = new File(
            "data/database/" + user + "/" + database + "/" + tableName + "/data.tbl"
        );

        if (!dataFile.exists()) return false;

        try (FileWriter fw = new FileWriter(dataFile, false)) {
            fw.write("");
            return true;
        } catch (IOException e) {
            return false;
        }
    }


    public static  List<String> listTables(String user,String datadase){
        File dbDir = new File("data/database/" +user+ "/" +datadase+ "/");
        List<String> tables = new ArrayList<>();

        if (!dbDir.exists() || !dbDir.isDirectory()) {
            return tables; 
        }
        File[] files = dbDir.listFiles(File::isDirectory);
        if (files !=null) {
            for(File tableDir : files){
                tables.add(tableDir.getName());
            }
        }

        return tables;

    }

    //alaina ny col rhtra
    public static List<String> getColumns(String user, String database, String tableName) {
        List<String> columns = new ArrayList<>();

        File schemaFile = new File(
            "data/database/" + user + "/" + database + "/" + tableName + "/schema.tbl"
        );

        if (!schemaFile.exists()) return columns;

        try (BufferedReader reader = new BufferedReader(new FileReader(schemaFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    columns.add(parts[0].trim());
                }
            }
        } catch (IOException e) {
            return columns;
        }

        return columns;
    }

    //ny donne
    public static List<String[]> getAllRows(String user, String database, String tableName) {
        List<String[]> rows = new ArrayList<>();
        
        File dataFile = new File(
            "data/database/" + user + "/" + database + "/" + tableName + "/data.tbl"
        );
    
        if (!dataFile.exists()) return rows;
    
        try (BufferedReader reader = new BufferedReader(new FileReader(dataFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split("\\|");
                rows.add(values);
            }
        } catch (IOException e) {
            return rows;
        }
    
        return rows;
    }

 
}
