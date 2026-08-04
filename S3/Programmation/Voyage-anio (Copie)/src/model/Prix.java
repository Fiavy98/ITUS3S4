package model;

public class Prix {

    /**
     * Calcule le prix d'une réparation en fonction de :
     * - profondeur du trou (cm)
     * - type de route (pavée, béton, goudron, etc.)
     * - surface du trou (m²)
     */
 public static double calculerPrix(Trou trou, TypeRoute typeRoute) {

    double profondeur = trou.getProfondeur();
    double surface = trou.getSurface();
    double prixM2;

    String type = typeRoute.getNom().toLowerCase().trim();

    switch (type) {

        case "pavee":
            if (profondeur >= 0 && profondeur <= 0.2) {        // [0 ; 0.2]
                prixM2 = 5000;
            } else if (profondeur > 0.2 && profondeur <= 0.5) { // ]0.2 ; 0.5]
                prixM2 = 6000;
            } else if (profondeur > 0.5 && profondeur <= 0.9) { // ]0.5 ; 0.9]
                prixM2 = 10000;
            } else {
                throw new IllegalArgumentException(
                    "Profondeur non supportée : " + profondeur + " m"
                );
            }
            break;

        case "goudronnee":
            if (profondeur >= 0 && profondeur <= 0.4) {
                prixM2 = 15000;
            } else if (profondeur > 0.4 && profondeur <= 0.8) {
                prixM2 = 20000;
            } else {
                throw new IllegalArgumentException(
                    "Profondeur non supportée : " + profondeur + " m"
                );
            }
            break;

        case "betonnee":
            if (profondeur >= 0 && profondeur <= 0.2) {
                prixM2 = 10000;
            } else if (profondeur > 0.2 && profondeur <= 0.4) {
                prixM2 = 15000;
            } else if (profondeur > 0.4 && profondeur <= 0.6) {
                prixM2 = 20000;
            } else if (profondeur > 0.6 && profondeur <= 0.8) {
                prixM2 = 25000;
            } else {
                throw new IllegalArgumentException(
                    "Profondeur non supportée : " + profondeur + " m"
                );
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

