public class Projection {
    String[] a;
    String[] b;
    String[] c;
    String[] noms;
    String[][] tous;
    

    public Projection(String[] a,String[] b,String[] c,String[] noms,String[][] tous){
        this.a=a;
        this.b=b;
        this.c=c;
        this.noms=noms;
        this.tous=tous;
    }

    public void elimine(String[] x){
        for (int i = 0; i < tous.length; i++) {
            if (tous[i] != x) {
                System.out.print(noms[i] + " : ");
                for (int j = 0; j < tous[i].length; j++) {
                    System.out.print(tous[i][j] + " ");
                }
                System.out.println();
            }
        }
    }
}

