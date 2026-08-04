package mydb.console;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mydb.core.DatabaseManager;
import mydb.core.TableManager;
import mydb.core.UserManager;
import mydb.core.request.Join;
import mydb.core.request.Select;
import mydb.core.request.Where;

public class FonctionCommand {
        static  String currentUser = "Tsisy";
        static String currentDatabase = "Tsisy";

        //==========================USER=====================================
        public static void hamoronaUser(String command) { 
        try {
            // Supprimer le ;
            command = command.replace(";", "");

            // Découper par espaces et stocker dans un tableau
            String[] parts = command.split("\\s+");

            // Vérifier la structure minimale
            if (parts.length != 3) {
                throw new IllegalArgumentException();
            }

            // parts[2] = username/password
            String[] credentials = parts[2].split("/");
            if (credentials.length != 2) {
                throw new IllegalArgumentException();
            }

            String username = credentials[0];
            String password = credentials[1];

            UserManager.createUser(username, password);

        } catch (Exception e) {
            System.out.println("diso fanoratra , andramo ny : HAMORONA MPAMPIASA username/password;");
        }
    }

    public static void connectUser(String command) {
        try {
            // Supprimer le ;
            command = command.replace(";", "").trim();

            // Découper par espace
            String[] parts = command.split("\\s+");

            // Vérifier syntaxe
            if (parts.length != 2) {
                throw new IllegalArgumentException();
            }

            // parts[1] = username/password
            String[] credentials = parts[1].split("/");

            if (credentials.length != 2) {
                throw new IllegalArgumentException();
            }

            String username = credentials[0];
            String password = credentials[1];

            // Vérifier que l'utilisateur existe et mot de passe correct
            if (UserManager.validateUser(username, password)) {
                 currentUser = username;
                System.out.println("CONNECTA en tant que : " + username);
            } else {
                System.out.println("Utilisateur na mot de passe DISO");
            }

        } catch (Exception e) {
            System.out.println("diso fanoratra. andramo : hampiasa username/password;");
        }
    }

//=========================BASE DE DONNES=============================================

    public static void hamoronaDB(String command) {
        try {
            if (currentUser.equals("none")) {
                System.out.println("Vous devez être connecté pour créer une base.");
                return;
            }

            command = command.replace(";", "").trim();
            String[] parts = command.split("\\s+");

            if (parts.length != 3) {
                throw new IllegalArgumentException();
            }

            String dbName = parts[2];
            DatabaseManager.createDatabase(currentUser, dbName);

        } catch (Exception e) {
            System.out.println("Syntaxe invalide. Utilisez : CREATE DATABASE nomDatabase;");
        }
    }

    public static void useDB(String command) {
        try {
            if (currentUser.equals("Tsisy")) {
                System.out.println("Vous devez être connecté pour utiliser une base.");
                return;
            }
            command = command.replace(";", "").trim();
            String[] parts = command.split("\\s+");
            if (parts.length != 2) {
                throw new IllegalArgumentException();
            }
            String dbName = parts[1];
            if (DatabaseManager.databaseExists(currentUser, dbName)) {
                currentDatabase = dbName;
            } else {
                System.out.println("Base tsy kobo : " + dbName);
            }

        } catch (Exception e) {
            System.out.println("Syntaxe invalide. Utilisez : USE nomDatabase;");
        }
    }

    public static void showDB() {
        if (currentUser.equals("Tsisy")) {
            System.out.println("Vous devez être connecté pour voir vos bases.");
            return;
        }

        File userDir = new File("data/database/" + currentUser);
        if (!userDir.exists() || !userDir.isDirectory()) {
            System.out.println("Aucune base trouvée pour l'utilisateur " + currentUser);
            return;
        }

        File[] databases = userDir.listFiles(File::isDirectory);
        if (databases == null || databases.length == 0) {
            System.out.println("Base tsy kobo");
            return;
        }

        int width = 20;

        // Ligne du haut
        System.out.println("+" + "-".repeat(width + 2) + "+");
        System.out.printf("| %-"+ width +"s |\n", "Database");
        System.out.println("+" + "-".repeat(width + 2) + "+");

        for (File db : databases) {
            System.out.printf("| %-"+ width +"s |\n", db.getName());
        }

        // Ligne du bas
        System.out.println("+" + "-".repeat(width + 2) + "+");
    }

    public static void dropDB(String command){
        if(currentUser.equals("Tsisy")){
            System.out.println("Connecte aloha");
            return;
        }

        command = command.replace(";", "");
        String[] parts = command.split("\\s+");

        if (parts.length != 3) {
            System.out.println("Tsipelina tsy ekena");
            return;
        }

        String dbName = parts[2];

        if (dbName.equals(currentDatabase)) {
            currentDatabase = "none";
        }

        boolean success = DatabaseManager.dropDataBase(currentUser, dbName);

        if (success) {
            System.out.println("Base voafafa");
        } else {
            System.out.println("Base mbola tsisy");
        }
    }

    public static void showUsers() {
        List<String> users = UserManager.getUsers();

        if (users.isEmpty()) {
            System.out.println("mbola tsisy user");
            return;
        }

        int width = 20;

        System.out.println("+" + "-".repeat(width + 2) + "+");
        System.out.printf("| %-"+ width +"s |\n", "User");
        System.out.println("+" + "-".repeat(width + 2) + "+");

        for (String user : users) {
            System.out.printf("| %-"+ width +"s |\n", user);
        }

        System.out.println("+" + "-".repeat(width + 2) + "+");
    }
//========================TABLE====================================
    public static void createTable(String command){
        if(currentUser.equals("Tsisy") || currentDatabase.equals("Tsisy")){
            System.out.println("Misafidiana aloha database");
            return;
        }

        try {
            command = command.replace(";", "").trim();
            String tableName = command.split("\\s+")[2];

            //Recupere la partie dans parenthèses
            String parenthese = command.substring(command.indexOf("(") + 1, command.indexOf(")"));
            String[] colonne = parenthese.split(",");

            //Map colonne
            Map<String, String> columns = new LinkedHashMap<>();
            for (String colDef : colonne) {
            String[] parts = colDef.trim().split("\\s+");
            columns.put(parts[0], parts[1].toUpperCase()); // nom -> type
            }

            // Validation  des types
            List<String> allowedTypes = List.of("INT", "TEXT");
            for (String type : columns.values()) {
                if (!allowedTypes.contains(type)) {
                    System.out.println("Type invalide : " + type);
                    return;
                }
            }


        // Création via TableManager
        boolean success = TableManager.createTbl(currentUser, currentDatabase, tableName, columns);
        if (success) {
            System.out.println("TABILAO VOAFORONA");
        } else {
            System.out.println("MISY FAHADISOANA");
        }
        } catch (Exception e) {
             System.out.println("TSIPELINA DISO");
        }

    }


    public static void insertDonneTable(String command) {
        if (currentUser.equals("Tsisy") || currentDatabase.equals("Tsisy")) {
            System.out.println("Vous devez être connecté et avoir sélectionné une base.");
            return;
        }

        try {
            // Nettoyage de la commande
            command = command.replace(";", "").trim();

            // AMPIDIRO AO AMIN'NY nomTable (...)
            String[] parts = command.split("\\s+");
            if (parts.length < 4) {
                System.out.println("Syntaxe invalide.");
                return;
            }

            // Nom de la table
            String tableName = parts[3];

            // Vérifier que la table existe
            File tableDir = new File(
                "data/database/" + currentUser + "/" + currentDatabase + "/" + tableName
            );
            if (!tableDir.exists()) {
                System.out.println("Tablilao tsy misy : " + tableName);
                return;
            }

            /* =======================
               Extraction des colonnes
               ======================= */
            int startCols = command.indexOf("(");
            int endCols = command.indexOf(")", startCols);
            if (startCols == -1 || endCols == -1) {
                System.out.println("Syntaxe invalide : colonnes manquantes.");
                return;
            }

            String anatyCols = command.substring(startCols + 1, endCols).trim();
            String[] columns = anatyCols.split(",");

            for (int i = 0; i < columns.length; i++) {
                columns[i] = columns[i].trim();
            }

            /* =======================
               Extraction des valeurs
               ======================= */
            int hoeIndex = command.toUpperCase().indexOf("HOE");
            if (hoeIndex == -1) {
                System.out.println("Syntaxe invalide : HOE manquant !");
                return;
            }

            int startValues = command.indexOf("(", hoeIndex);
            int endValues = command.indexOf(")", startValues);
            if (startValues == -1 || endValues == -1) {
                System.out.println("Syntaxe invalide : valeurs manquantes.");
                return;
            }

            String valuesPart = command.substring(startValues + 1, endValues).trim();
            String[] values = valuesPart.split(",");

            for (int i = 0; i < values.length; i++) {
                values[i] = values[i].trim();
            }

            /* =======================
               Vérification
               ======================= */
            if (columns.length != values.length) {
                System.out.println("Nombre de colonnes et valeurs différent !");
                return;
            }

            /* =======================
               Insertion via TableManager
               ======================= */
            boolean success = TableManager.insertRow(
                currentUser,
                currentDatabase,
                tableName,
                columns,
                values
            );

            if (success) {
                System.out.println("Donnée tafiditra !");
            } else {
                System.out.println("Erreur lors de l'insertion !");
            }

        } catch (Exception e) {
            System.out.println(
                "Syntaxe invalide. Exemple : " +
                "AMPIDIRO AO AMIN'NY table (col1, col2) HOE (val1, val2);"
            );
        }
    }

    public static void dropTable(String command) {
        if (currentUser.equals("Tsisy") || currentDatabase.equals("Tsisy")) {
            System.out.println("Vous devez être connecté et avoir sélectionné une base.");
            return;
        }

        try {
            command = command.replace(";", "");
            String[] parts = command.split("\\s+");

            if (parts.length != 4) {
                System.out.println("Tsipelina tsy ekena");
                return;
            }

            String tableName = parts[3];

            File tableDir = new File(
                    "data/database/" + currentUser + "/" + currentDatabase + "/" + tableName
                );
                if (!tableDir.exists()) {
                    System.out.println("Tablilao tsy misy : " + tableName);
                    return;
                }

            boolean ok = TableManager.dropTable(
                    currentUser,
                    currentDatabase,
                    tableName
            );

            if (ok) {
                System.out.println("Tablilao voafafa : " + tableName);
            } else {
                System.out.println("Tsy afaka mamafa tablilao.");
            }
        }catch (Exception e) {
            System.out.println("Hadisoana amin'ny DROP TABLE.");
        }

    }

    public static void deleteTable(String command) {
        if (currentUser.equals("Tsisy") || currentDatabase.equals("Tsisy")) {
            System.out.println("Tsy maintsy mifandray sy misafidy base aloha.");
            return;
        }

        try {
            command = command.replace(";", "").trim();
            String[] parts = command.split("\\s+");

            if (parts.length != 4) {
                System.out.println("diso tsipelina");
                return;
            }

            String tableName = parts[3];

            boolean ok = TableManager.deleteTable(
                    currentUser,
                    currentDatabase,
                    tableName
            );

            System.out.println(ok
                ? "Voafafa daholo ny donnees."
                : "Erreur lors de la suppression."
            );

        } catch (Exception e) {
            System.out.println("Hadisoana amin'ny DELETE.");
        }

    }


    public static void showTable(){
        if (currentUser.equals("Tsisy") || currentDatabase.equals("Tsisy")) {
            System.out.println("Tsy maintsy mifandray sy misafidy base aloha.");
            return;
        }

        List<String> tables = TableManager.listTables(currentUser, currentDatabase);

        if (tables.isEmpty()) {
            System.out.println("Tsy misy table");
        }

        int width = 20;
        System.out.println("+" + "-".repeat(width + 2) + "+");
        System.out.printf("| %-"+ width +"s |\n", "Table");
        System.out.println("+" + "-".repeat(width + 2) + "+");

        for (String tableName : tables) {
            System.out.printf("| %-"+ width +"s |\n", tableName);
        }

        System.out.println("+" + "-".repeat(width + 2) + "+");

    }

//=========================SELECT=================================
    public static void select(String command) {
    if (currentUser.equals("Tsisy") || currentDatabase.equals("Tsisy")) {
        System.out.println("Vous devez être connecté et avoir sélectionné une base.");
        return;
    }

    try {
        command = command.replace(";", "").trim();
        String upper = command.toUpperCase();

        // -------- WHERE (optionnel) --------
        Where where = null;
        if (upper.contains(" RAHA ")) {
            String wherePart = command.substring(upper.indexOf(" RAHA ") + " RAHA ".length()).trim();

            // split avec opérateur = > < avec ou sans espace
            String op = wherePart.contains("=") ? "=" : wherePart.contains(">") ? ">" : "<";
            String[] cond = wherePart.split("\\s*\\" + op + "\\s*");

            if (cond.length == 2) {
                String col = cond[0].trim();
                String val = cond[1].trim();
                where = new Where(col, op, val);
            } else {
                System.out.println("Diso tsipelina !");
                return;
            }

            // Retirer WHERE de la commande
            command = command.substring(0, upper.indexOf(" RAHA "));
        }

        // -------- FIDIO / AO AMIN'NY --------
        int fromIndex = upper.indexOf(" AO AMIN'NY ");
        if (fromIndex == -1) {
            System.out.println("Tsipelina tsy ekena");
            return;
        }

        String selectPart = command.substring("FIDIO".length(), fromIndex).trim();
        String tableName = command.substring(fromIndex + " AO AMIN'NY ".length()).trim();

        // Colonnes
        List<String> columns = new ArrayList<>();
        if (!selectPart.equals("*") && !selectPart.equalsIgnoreCase("*")) {
            for (String c : selectPart.split(",")) {
                columns.add(c.trim());
            }
        }

        // -------- Construire SELECT --------
        Select select = new Select(currentUser, currentDatabase, tableName, columns, where);

        // -------- Exécuter --------
        select.execute();

    } catch (Exception e) {
        System.out.println("Syntaxe invalide. Exemple :");
        System.out.println("FIDIO col1, col2 AO AMIN'NY table RAHA col = value;");
    }
    }

//=============================== JOIN ==========================
public static void join(String command) {

    if (currentUser.equals("Tsisy") || currentDatabase.equals("Tsisy")) {
        System.out.println("Vous devez être connecté et avoir sélectionné une base.");
        return;
    }

    try {
        command = command.replace(";", "").trim();

        // Regex pour parser FIDIO ... AVY AMIN'NY ... MIARAKA AMIN'NY ... RAHA ...
        Pattern pattern = Pattern.compile(
            "(?i)^FIDIO\\s+(.+)\\s+AVY AMIN'NY\\s+(\\S+)\\s+MIARAKA AMIN'NY\\s+(\\S+)\\s+RAHA\\s+(.+)$"
        );
        Matcher matcher = pattern.matcher(command);

if (!matcher.matches()) {
    System.out.println("hahaha"); // message d'erreur personnalisé
    return;
}


        String selectPart = matcher.group(1).trim(); // colonnes (ex: table1.name ou *)
        String table1 = matcher.group(2).trim();
        String table2 = matcher.group(3).trim();
        String condition = matcher.group(4).trim();   // ex: table1.id=table2.id

        // Colonnes
        List<String> columns = new ArrayList<>();
        if (!selectPart.equals("*")) {
            for (String c : selectPart.split(",")) {
                columns.add(c.trim());
            }
        } else {
            // Si *, on va récupérer toutes les colonnes de table1 et table2
            List<String> cols1 = TableManager.getColumns(currentUser, currentDatabase, table1);
            List<String> cols2 = TableManager.getColumns(currentUser, currentDatabase, table2);
            for (String c : cols1) columns.add(table1 + "." + c);
            for (String c : cols2) columns.add(table2 + "." + c);
        }

        // -------- condition JOIN --------
        String[] cond = condition.split("=");
        if (cond.length != 2) {
            System.out.println("Condition JOIN invalide");
            return;
        }

        String left  = cond[0].trim(); // table1.id
        String right = cond[1].trim(); // table2.id

        // -------- créer JOIN --------
        Join join = new Join(
                currentUser,
                currentDatabase,
                table1,
                table2,
                left.split("\\.")[1],   // colonne table1
                right.split("\\.")[1],  // colonne table2
                columns
        );

        join.execute();

    } catch (Exception e) {
        System.out.println("Erreur JOIN");
        e.printStackTrace();
    }
}

}
