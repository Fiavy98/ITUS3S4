public class Select{
    String[] colonne;
    String[][] ligne;

    public Select(String[] colonne,String[][] ligne){
        this.colonne=colonne;
        this.ligne=ligne;
    }
    
    public void select_a(String col,String recherche){

        int indiceCol = -1;
        for(int i = 0; i < colonne.length; i++){ 
            if(colonne[i].equals(col)){
                indiceCol=i;
                break;
            }
        }

        if(indiceCol != -1) {
            System.out.println(col + " = " + recherche + " :");
            for(int i = 0; i < ligne.length; i++) {
                if(ligne[i][indiceCol].equals(recherche)) {
                    for(int j = 0; j < colonne.length; j++) {
                        System.out.print(ligne[i][j] + "\t");
                    }
                    System.out.println();
                }
            }
        } else {
            System.out.println("Colonne " + col + " introuvable !");
        }    
    }

    public void select_b(String col1,String crch1,String col2,String crch2,String col3,String crch3){
        int indiceCol1=-1;
        int indiceCol2=-1;
        int indiceCol3=-1;
       
        for (int i = 0; i < colonne.length; i++) {
            if (colonne[i].equals(col1)) indiceCol1 = i;
            if (colonne[i].equals(col2)) indiceCol2 = i;
            if (colonne[i].equals(col3)) indiceCol3 = i;
        }

     System.out.println("("+col1+" = "+crch1+" v "+col2+" = "+crch2+ ") ∧ "+col3+" ≤ "+crch3);
    
     if (indiceCol1 != -1 && indiceCol2 != -1 && indiceCol3 != -1) { 
        for (int i = 0; i < ligne.length; i++) {
            if ( (ligne[i][indiceCol1].equals(crch1) || ligne[i][indiceCol2].equals(crch2))
            && Integer.parseInt(ligne[i][indiceCol3]) <= Integer.parseInt(crch3)) {          
                for (int j = 0; j < colonne.length; j++) {
                    System.out.print(ligne[i][j] + "\t");
                }
                System.out.println();
            }
        }
        } else {
            System.out.println("⚠️ Une des colonnes n'existe pas !");
        }
    }


    public void select_c(String col1,String col2){

        int indiceCol1 = -1;
        int indiceCol2 = -1;
        for(int i = 0; i < colonne.length; i++){ 
            if(colonne[i].equals(col1)) indiceCol1=i;
            if(colonne[i].equals(col2)) indiceCol2=i;
        }

        if(indiceCol1 != -1 && indiceCol2 != -1) {
            System.out.println(col1 + " = " + col2 + " :");
            for(int i = 0; i < ligne.length; i++) {
                if(ligne[i][indiceCol1].equals(ligne[i][indiceCol2])) {
                    for(int j = 0; j < colonne.length; j++) {
                        System.out.print(ligne[i][j] + "\t");
                    }
                    System.out.println();
                }
            }
        }
    }
}