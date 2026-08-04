package models;

import java.time.LocalDate;

import dao.MouvementDAO;

public class EtatStockDTO {

    private int produitId;
    private String nomProduit;

    private double stockQuantite;
    private double valeurStock;
    private double prixMoyen;
    private String methode; 
    private java.time.LocalDate lastMovementDate;
    
    private MouvementDAO dao = new MouvementDAO();

    public EtatStockDTO() {
    }

    public EtatStockDTO(int produitId, String nomProduit,
                        double stockQuantite,
                        double valeurStock,
                        double prixMoyen,
                        java.time.LocalDate lastMovementDate
                        ) {

        this.produitId = produitId;
        this.nomProduit = nomProduit;
        this.stockQuantite = stockQuantite;
        this.valeurStock = valeurStock;
        this.prixMoyen = prixMoyen;
        this.lastMovementDate = lastMovementDate;
    }

    public int getProduitId() {
        return produitId;
    }

    public void setProduitId(int produitId) {
        this.produitId = produitId;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public double getStockQuantite() {
        return stockQuantite;
    }

    public void setStockQuantite(double stockQuantite) {
        this.stockQuantite = stockQuantite;
    }

    public double getValeurStock() {
        return valeurStock;
    }

    public void setValeurStock(double valeurStock) {
        this.valeurStock = valeurStock;
    }

    public double getPrixMoyen() {
        return prixMoyen;
    }

    public void setPrixMoyen(double prixMoyen) {
        this.prixMoyen = prixMoyen;
    }
    public String getMethode() {
    return methode;
}

public void setMethode(String methode) {
    this.methode = methode;
}

public LocalDate getLastMovementDate() {
    return lastMovementDate;
}

public void setLastMovementDate(LocalDate lastMovementDate) {
    this.lastMovementDate = lastMovementDate;
}


    // ================= UTILE POUR AFFICHAGE =================

    @Override
    public String toString() {
        return "Produit: " + nomProduit +
                " | Stock: " + stockQuantite +
                " | Prix moyen: " + prixMoyen +
                " | Valeur stock: " + valeurStock;
    }
}