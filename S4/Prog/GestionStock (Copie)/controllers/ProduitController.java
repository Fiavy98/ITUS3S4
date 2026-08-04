package controllers;

import models.Produit;
import services.ProduitService;
import views.AjoutStockView;

import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class ProduitController {
    
    private ProduitService service = new ProduitService();

    public void ajouterProduit(Produit p){
        try {
            service.AjouterProduits(p);
            
        } catch (Exception e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    public List<Produit> getAll(){

        try {

            return service.getAll();

        } catch (Exception e) {

            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
