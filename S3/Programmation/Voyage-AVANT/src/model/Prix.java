package model;

public class Prix {

    /**
     * Calcule le prix d'une réparation en fonction de :
     * - profondeur du trou (cm)
     * - type de route (pavée, béton, goudron, etc.)
     * - surface du trou (m²)
     */

    public static double calculerPrix(Trou trou, TypeRoute typeRoute) {

        double profondeur = trou.getProfondeur(); // en cm
        double surface = trou.getSurface();       // en m2
        double prixM2 = 0;

        String type = typeRoute.getNom().toLowerCase().trim();

        switch (type) {

            case "pavee":
                if (profondeur <= 20) {
                    prixM2 = 40;
                } else {
                    prixM2 = 70;
                }
                break;

            case "goudronnee":
                if (profondeur <= 20) {
                    prixM2 = 30;
                } else {
                    prixM2 = 60;
                }
                break;

            case "betonnee":
                if (profondeur <= 20) {
                    prixM2 = 50;
                } else {
                    prixM2 = 90;
                }
                break;

            default:
                throw new IllegalArgumentException(
                    "Type de route inconnu : " + typeRoute.getNom()
                );
        }

        return prixM2 * surface;
    }
}

