public class Drible extends Joueur{
    double drible;
    public Drible(String nom,double drible){
        super(nom);
        this.drible=drible;
    }

    public void afficher(){
        System.out.println("Nom : "+nom+" Drible : "+drible);
    }
   
}