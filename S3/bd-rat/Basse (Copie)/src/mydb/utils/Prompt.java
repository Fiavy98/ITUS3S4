package mydb.utils;

public class Prompt {

    public static String get(String dbName) {
        return "NyDB-ko [(" + dbName + ")]> ";
    }
}