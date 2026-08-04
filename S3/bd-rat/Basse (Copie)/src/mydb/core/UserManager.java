package mydb.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserManager {
    private static final String USER_FILE = "data/users.db";
    
    public static void createUser(String username, String password) {
        try {
            File file = new File(USER_FILE);
            file.getParentFile().mkdirs();

            FileWriter writer = new FileWriter(file, true);
            writer.write(username + ":" + password + "\n");
            writer.close();

            System.out.println("Mpampiasa voaforona : " + username);
        } catch (IOException e) {
            System.out.println("Misy sampona teo ampamoronana");
        }
    }

    public static boolean validateUser(String username, String password) {
        File usersFile = new File("data/users.db");

        if (!usersFile.exists()) return false;

        try (Scanner sc = new Scanner(usersFile)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue; // ignorer les lignes vides

                String[] parts = line.split(":");
                if (parts.length != 2) continue; // ignorer lignes malformées

                String storedUser = parts[0];
                String storedPassword = parts[1];

                if (storedUser.equals(username) && storedPassword.equals(password)) {
                    return true; // utilisateur trouvé et mot de passe correct
                }
            }
        } catch (Exception e) {
            return false;
        }

        return false; 
    }

    public static List<String> getUsers() {
    List<String> users = new ArrayList<>();
    File file = new File("data/users.db");

    if (!file.exists()) {
        return users;
    }

    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
        String line;

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty() || !line.contains(":")) continue;

            String username = line.split(":", 2)[0];
            users.add(username);
        }
    } catch (IOException e) {
        System.out.println("Erreur famakiana ny users.db");
    }

    return users;
}



}
