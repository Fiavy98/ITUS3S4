package ensemble;

import java.util.Vector;

public class Appartenance {

    public static <N> boolean appartient(Vector<N> ensemble, N element) {
        return ensemble.contains(element);
    }
    public static void main(String[] args) {
        Vector<Integer> ensemble = new Vector<>();
        ensemble.add(1);
        ensemble.add(2);
        ensemble.add(3);
        ensemble.add(4);
        ensemble.add(5);

        System.out.println(appartient(ensemble, 2));
        System.out.println(appartient(ensemble, 3));
        System.out.println(appartient(ensemble, 1));
    }
}