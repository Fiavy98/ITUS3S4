public class Ensemble {
    
    public static void appartient(int x,int[] e,int taille){
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

    public static int count(int[] e){
        int count=0;
       /*for(int i=0; i<e.length; i++){
            count++;
        }*/
        for(int nb : e){
            count++;
        }

        return count;
    }

    public static void union(int[] e,int[] f){
         //   e + f : eviter le doublons 
        int taille = count(e) + count(f);  //length
        int[] e_f=new int[taille];
    
        int deux=0;
        for(int i=0; i<count(e); i++){
            e_f[i]=e[i];
            deux++;
        }
 
        for(int j=0; j<count(f); j++){
            int existe=1;
            for(int k=0; k<deux; k++){
                if(e_f[k]==f[j]){
                    existe=0;
                }
            }

            if(existe!=0){
                e_f[deux]=f[j];
                deux++;
            }
        }       
            System.out.println("Union de e et f :");
            for(int rep=0; rep<deux; rep++){
                System.out.println(e_f[rep]);
            }
    }
    public static void inter(int[] e,int[] f){
        int taille=count(e)+count(f);
        int[] e_f =new int[taille];
        int deux=0;
        for(int i=0; i<count(e);i++){ 
            int existe=0;
            for(int j=0; j<count(f);j++){
                if(e[i]==f[j]){ 
                    existe=1;
                }
            }

            if(existe==1){
                e_f[deux]=e[i];
                deux++;
            }
        }

         System.out.println("inter de e et f :");
            for(int rep=0; rep<deux; rep++){
                System.out.println(e_f[rep]);
            }
    }
//    Cartesien a x b = (a1,b1)(a1,b2)
    public static void cartesienne(int[] e,int[] f){
        int taille=count(e)*count(f);
        System.out.println("cartesien de e et f :");
       for(int i=0; i<count(e); i++){
            for(int j=0; j<count(f); j++){
                System.out.println(e[i]+","+f[j]);
            }
       }

    }
    
    public static void main(String[] args){
        int[] e={1,2,3,5,4};
        int[] f={4,3,5,6};
        int taille=count(e);

        System.out.println("Exo 1 :");
        int x=3;
        appartient(x,e,taille);

        System.out.println("\nExo 2 :");
        System.out.println("Nombre de valeur dans e :"+taille);

        System.out.println("\nExo 3 :");
        union(e,f);

        System.out.println("\nExo 4 :");
        inter(e,f);
         
        System.out.println("\nExo 5 :");
        cartesienne(e,f);
    }
}
