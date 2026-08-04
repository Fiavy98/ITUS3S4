package controllers;

import models.*;
import services.EtatStockService;
import dao.ProduitDAO;

import java.util.ArrayList;
import java.util.List;

public class EtatStockController {

    private EtatStockService service = new EtatStockService();
    private ProduitDAO produitDAO = new ProduitDAO();

    public List<EtatStockDTO> getEtatStock() {

        try {
            List<Produit> produits = produitDAO.getAll();

            List<EtatStockDTO> result = new ArrayList<>();

            for (Produit p : produits) {

                switch (p.getMethodeValuation()) {

                    case CUMP -> result.add(service.calculCUMP(p));
                    case FIFO -> result.add(service.calculFIFO(p));
                    case LIFO -> result.add(service.calculLIFO(p));
                }
            }

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}