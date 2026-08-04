package db;

public class Calcul {

    public static double calculPrix(String idProduit, String idUnite, double qte) {
        double pv = From_unite.getPUunite(idProduit, idUnite); 
        return qte * pv;
    }

    public static double parseQuantite(String txt) {
        if (txt == null) return 0;
        txt = txt.trim().replace(',', '.'); 
    
        if (txt.contains("/")) {
            try {
                String[] parts = txt.split("/");
                if (parts.length == 2) {
                    double num = Double.parseDouble(parts[0].trim());
                    double den = Double.parseDouble(parts[1].trim());
                    if (den == 0) return 0;
                    return num / den;
                }
            } catch (Exception e) {
                return 0;
            }
        }
    
        try {
            return Double.parseDouble(txt);
        } catch (Exception e) {
            return 0;
        }
    }
    
    
    
}
