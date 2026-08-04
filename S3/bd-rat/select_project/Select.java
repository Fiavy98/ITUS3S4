public class Select{
    
    public void select(String[][] table, String[] colonnes, String colonne, String valeur) {
        int pos = -1;
        for (int i = 0; i < colonnes.length; i++) {
            if (colonnes[i].equals(colonne)) {
                pos = i;
                break;
            }
        }
        
        if (pos == -1) {
            System.out.println("Colonne non trouvee!");
            return;
        }

        for (String nom : colonnes) {
            System.out.print(nom + " ");
        }
        
        System.out.println();
        for (int i = 0; i < table.length; i++) {
            if (table[i][pos].equals(valeur)) {
                for (int j = 0; j < table[i].length; j++) {
                    System.out.print(table[i][j] + " ");
                }
                System.out.println();
            }
        }
    }
    
    public void select_b(String[][] table, String[] colonnes, String valASelect, String valBSelect, int valCMax) {
        int posA = -1, posB = -1, posC = -1;
    
        // Trouver les indices des colonnes
        for (int i = 0; i < colonnes.length; i++) {
            if (colonnes[i].equals("A")) posA = i;
            if (colonnes[i].equals("B")) posB = i;
            if (colonnes[i].equals("C")) posC = i;
        }
    
        if (posA == -1 || posB == -1 || posC == -1) {
            System.out.println("Colonnes manquantes !");
            return;
        }

        for (String nom : colonnes) {
            System.out.print(nom + " ");
        }
        System.out.println();
    
        for (int i = 0; i < table.length; i++) {
            String valA = table[i][posA];
            String valB = table[i][posB];
            int valC = Integer.parseInt(table[i][posC]);
    
            // (A = valASelect OR B = valBSelect) AND C <= valCMax
            if ((valA.equals(valASelect) || valB.equals(valBSelect)) && valC <= valCMax) {
                for (int j = 0; j < table[i].length; j++) {
                    System.out.print(table[i][j] + " ");
                }
                System.out.println();
            }
        }
    }
    
    public void selec_c(String[][] table, String[] colonnes) {
        int posA = -1, posB = -1;
    
        // Trouver les indices des colonnes A et B
        for (int i = 0; i < colonnes.length; i++) {
            if (colonnes[i].equals("A")) posA = i;
            if (colonnes[i].equals("B")) posB = i;
        }
    
        if (posA == -1 || posB == -1) {
            System.out.println("Colonnes manquantes !");
            return;
        }
    
        // Afficher les entêtes
        for (String nom : colonnes) {
            System.out.print(nom + " ");
        }
        System.out.println();
    
        // Parcourir et sélectionner les lignes où A = B
        for (int i = 0; i < table.length; i++) {
            String valA = table[i][posA];
            String valB = table[i][posB];
    
            if (valA.equals(valB)) {
                for (int j = 0; j < table[i].length; j++) {
                    System.out.print(table[i][j] + " ");
                }
                System.out.println();
            }
        }
    }
    
    

}