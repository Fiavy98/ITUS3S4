package mydb.console;

import java.util.Scanner;

import mydb.utils.Prompt;

public class Console {
    public static void start() {

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print(Prompt.get(FonctionCommand.currentDatabase));

            //Lire tous les ligne
            String command = scanner.nextLine().trim();
            
            if (command.equalsIgnoreCase("hiala;")
                    || command.equalsIgnoreCase("handao;")) {
                System.out.println("Veloma");
                break;
            }
//================================== USER ==============================================
            if (command.toUpperCase().startsWith("HAMORONA MPAMPIASA")) {
                FonctionCommand.hamoronaUser(command);
            }

            else if (command.toUpperCase().startsWith("ASEHOY NY MPAMPIASA")) {
                FonctionCommand.showUsers();
            }
            
            else if (command.toUpperCase().startsWith("HIDITRA")) {
                FonctionCommand.connectUser(command);
            }
//=================================== DATABASE ============================================
            else if (command.toUpperCase().startsWith("HAMORONA DATABASES")) {
                FonctionCommand.hamoronaDB(command);
            }

            else if (command.toUpperCase().startsWith("HAMPIASA")) {
                FonctionCommand.useDB(command);
            }

            else if (command.toUpperCase().startsWith("ASEHOY NY DATABASES")) {
                FonctionCommand.showDB();
            }

            else if (command.toUpperCase().startsWith("FAFANA DATABASES")) {
                FonctionCommand.dropDB(command);
            }
//================================ TABLE ===================================================
            else if (command.toUpperCase().startsWith("HAMORONA TABILAO")) {
                FonctionCommand.createTable(command);
            }

            else if (command.toUpperCase().startsWith("AMPIDIRO AO AMIN'NY")) { // base('') HOE()
                FonctionCommand.insertDonneTable(command);
            }

            else if (command.toUpperCase().startsWith("FAFANA NY TABILAO")) { //DROP
                FonctionCommand.dropTable(command);
            }

            else if (command.toUpperCase().startsWith("FAFANA NY DONNEE")) { //DELETE
                FonctionCommand.deleteTable(command);
            }

            else if (command.toUpperCase().startsWith("ASEHOY NY TABILAO")) { //DELETE
                FonctionCommand.showTable();
            }

            else if (command.toUpperCase().startsWith("FIDIO")) {
                FonctionCommand.select(command);
}


            else {
                System.out.println("Commandy tsy kobo");
            }
        }   
        scanner.close();
    }

}
