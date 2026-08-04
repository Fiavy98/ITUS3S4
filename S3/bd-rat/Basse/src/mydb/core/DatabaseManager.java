package mydb.core;

import java.io.File;

public class DatabaseManager {
    
    // Créer une base pour un utilisateur
    public static void createDatabase(String username, String dbName) {
        File dbDir = new File("data/database/" + username + "/" + dbName);
        if (dbDir.exists()) {
            System.out.println("La base existe déjà.");
            return;
        }
        dbDir.mkdirs();
        System.out.println("Base créée : " + dbName);
    }

    // Vérifier si une base existe pour un utilisateur
    public static boolean databaseExists(String username, String dbName) {
        File dbDir = new File("data/database/" + username + "/" + dbName);
        return dbDir.exists() && dbDir.isDirectory();
    }

    public static void deleteDB(File file){
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                deleteDB(f);
            }   
        }

        file.delete();
    }

    public static boolean dropDataBase(String username,String dbName){
        File dbDir = new File("data/database/"+ username + "/" + dbName);

        if (!dbDir.exists() || !dbDir.isDirectory()) {
            return false;
        }

        deleteDB(dbDir);
        return true;

    }


    
}
