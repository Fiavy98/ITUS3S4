// ===============================
// SERVICES/ArticleService.java
// ===============================

package services;

import dao.ProduitDAO;
import db.ConnectionDB;
import models.Produit;

import java.sql.Connection;
import java.util.List;

public class ProduitService {
    private ProduitDAO dao = new ProduitDAO();

    public void AjouterProduits(Produit p) throws Exception{
        if (p.getNom() == null || p.getNom().isEmpty()) {
            throw new Exception("Nom obligatoire");
        }
        if (p.getPrixVenteDefaut() < 0) {
            throw new Exception("Prix invalide");
        }

        dao.insert(p);
    }
    
    public List<Produit> getAll() throws Exception {

        return dao.getAll();
    }

}