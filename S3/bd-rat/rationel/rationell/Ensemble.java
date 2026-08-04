public class Ensemble {
    public static void main(String[] args){
        int[] e={1,2,3,4,5,9};
        int x=2;
        int trouve=0;
        for(int i=0; i<4; i++){
            if(e[i]==x){
                trouve=1;
            }
        }

        System.out.println("Exo 1 : appartient");
        if(trouve==1){
            System.out.println(x+" appartient a e");
        }else{
            System.out.println(x+" n appartient pas a e");
        }


        System.out.println("\nExo 2 : compter");
        int count=0;
       /* for(int i=0; i<e.length; i++){
           count++;
        }*/
       for(int nb : e){
            count++;
       }

        System.out.println("valeur dans l'ensemble : "+count);

    }



}