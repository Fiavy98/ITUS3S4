public class Appartient {
    String x;
    String[] e;
    int taille;
    public Appartient(String x ,String[] e,int taille){
        this.x=x;
        this.e=e;
        this.taille=taille;
    }
    public void appartient(String x,String[] e,int taille){
        int val=0;
        for(int i=0; i<taille; i++){
            if(e[i]==x){
                val=1;
            }
        }

        if(val==1){
            System.out.println(x+" Apartient a e");
        }else{
            System.out.println(x+" N apartient pas a e");
        }
    }
}