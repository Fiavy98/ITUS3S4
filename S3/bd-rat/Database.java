import java.io.*;
import java.util.*;

public class Database {

    static final String FILE_NAME = "data.txt";
    public static void add(String data) throws IOException {
        FileWriter fw = new FileWriter(FILE_NAME, true);
        fw.write(data + "\n");
        fw.close();
    }

    public static void read() throws IOException {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("Base vide.");
            return;
        }

        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            System.out.println(sc.nextLine());
        }
        sc.close();
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Commande: add | read");
            return;
        }

        switch (args[0]) {
            case "add":
                if (args.length < 2) {
                    System.out.println("Ex: java Database add Rakoto");
                } else {
                    add(args[1]);
                    System.out.println("Ajouté !");
                }
                break;

            case "read":
                read();
                break;

            default:
                System.out.println("Commande inconnue");
        }
    }
}
