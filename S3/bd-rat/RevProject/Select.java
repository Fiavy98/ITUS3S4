public class Select {
    String[] colonne;
    String[][] ligne;

    public Select(String[] colonne,String[][] ligne){
        this.colonne=colonne;
        this.ligne=ligne;
    }

    public void select_a(String col,String chearch){
        int indiceCol =-1;
        for (int i = 0; i < colonne.length; i++){
            if(colonne[i].equals(col)){
                indiceCol=i;
                break;
            }
        }

        if(indiceCol != -1) {
            System.out.println(col + " = " + chearch + " :");
            for(int i = 0; i < ligne.length; i++) {
                if(ligne[i][indiceCol].equals(chearch)) {
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


}
