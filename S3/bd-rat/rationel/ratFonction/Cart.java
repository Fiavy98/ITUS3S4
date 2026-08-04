public class Cart {
    public void cartesienne(String[] e,String[] f){
        Count count = new Count();
        int taille=count.count(e)*count.count(f);
        System.out.println("cartesien de e et f :");
      
        for(int i=0; i<count.count(e); i++){
            for(int j=0; j<count.count(f); j++){
                System.out.println(e[i]+","+f[j]);
            }
       }

    }
}