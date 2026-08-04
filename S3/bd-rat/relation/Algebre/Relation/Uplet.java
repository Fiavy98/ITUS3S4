import java.util.List;

public class Uplet {
    private List<Object> valeurs;


    public Uplet(List<Object> valeurs) {
        this.valeurs = valeurs;
    }

    public Object getValeurs(int indice) {
        return valeurs.get(indice);
    }
    @Override
    public string to String() {
        return valeurs.toString();
    }
}