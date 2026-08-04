import java.util.ArrayList;
import java.util.List;




public class Relation {
    String nom;
    List<Attribut> attributs;
    List<Uplet> lignes;

    public Relation(String nom,List<Attribut> attributs) {
        this.nom = nom;
        this.attributs = attributs;
        this.lignes = new ArrayList<>();
    }

    public void ajouter_Uplet(Uplet ligne) {
        uplet.add(ligne);
    }

    public List<Uplet> getUplets() {
        return lignes;
    }



    public void Afficher() {
        System.out.println("Relation : " + nom);

        for(Attribut colonne : attributs) {
            System.out.println(colonne.nom + "\t");
        }
        System.out.println();

        for(Uplet ligne : lignes) {
            System.out.println("ligne");
        }
    }

    //selection

    public Selection(String attributNom,Object valeur) {
        List<Uplet> result = new ArrayList<>();
        int indice = -1;

        for(int i =0; i<attributs.size(); i++) {
            if(attributs.get(i).getNom.equals(valeur)) {
                result.add(ligne);
            }
        }
    }
    Relation newRelation = new Relation(nom + "_selection", attributs);
    newRelation.ligne = result;
    return newRelation;


    //projection

    public Projection(List<String> attributsProjection) {
        List<Attribut> newAttribut = newArrayList<>();
        List<Uplet> result =newArrayList<>();


        //indices des attributs a projeter

        List<Integer> indices = new ArrayList<>();
        for(String nomAttribut : attributsProjection) {
            for(int i =0; i < attributs.size(); i++) {
                if (attributs.get(i).getNom().equals(nomAttribut)) {
                    newAttribut.add(attributs.get(i));
                    indices.add(i);
                    break;
                }
            }
        }
    }
}