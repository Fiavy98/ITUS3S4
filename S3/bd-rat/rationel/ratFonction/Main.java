import java.util.List;

public class Main {    
    public static void main(String[] args){
        String[] e={"1","2","3"};
        String[] f={"4","3"};

        Ensemble ee = new Ensemble();
        Ensemble ef = new Ensemble();
 
        System.out.println("e :");
        ee.getVal(e);
 
        System.out.println("\nf :");
        ef.getVal(f);

        Count count = new Count();

        int taille=count.count(e);
        
        String x="3";
        Appartient app = new Appartient(x,e,taille);
        Union union = new Union(e,f);
        Inter inter = new Inter();
        Cart cart = new Cart();
        Relation relat = new Relation();

        System.out.println("\nExo 1 :");
        app.appartient(x,e,taille);

        System.out.println("\nExo 2 :");
        System.out.println("Nombre de valeur dans e :"+taille);

        System.out.println("\nExo 3 :");
        union.union(e,f);

        System.out.println("\nExo 4 :");
        inter.inter(e,f);
         
        System.out.println("\nExo 5 :");
        cart.cartesienne(e,f);
  
        List<String> R = relat.creerRelation(e, f);
        System.out.println("\nRelation :");
        for (String couple : R) {
            System.out.println(couple);
        }
        
    }
}
