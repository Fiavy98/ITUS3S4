package model;

public class Pluie {

    /**
     * Détermine le NOM du type de réparation selon la pluie
    */
   
    public static String determinerNomType(double pluie) {

        if (pluie < 0) {
            throw new IllegalArgumentException("Pluie négative");
        }

        // [0 ; 0.2]
        if (pluie >= 0 && pluie <= 0.2) {
            return "betonnee";
        }

        // ]0.2 ; 0.5]
        if (pluie > 0.2 && pluie <= 0.5) {
            return "pavee";
        }

        // ]0.5 ; +∞[
        return "goudronnee";
    }

}
