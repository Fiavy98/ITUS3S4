public class Ensemble {
    String[] valeur;

    public void getVal(String[] e ){
        System.out.print("{");
        for(int i=0; i<e.length; i++){
            System.out.print(e[i]+" ");
        }
        System.out.print("}");
    }
}