
public class Attribut {
    private String nom;
    private Domaine type;

    public Attribut(String nom, Domaine type) {
        this.nom = nom;
        this.type = type;
    }

    public String getNom() {
        return nom;
    }

    public Domaine getType() {
        return type;
    }

    @Override 
    public String toString() {
        return nom + ": " + 
        type;
    }
}