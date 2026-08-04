package services;

import dao.MouvementDAO;
import dao.ProduitDAO;
import models.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EtatStockService {

    private MouvementDAO mouvementDAO = new MouvementDAO();
    private ProduitDAO produitDAO = new ProduitDAO();

    public List<EtatStockDTO> getEtatStock(LocalDate dateFiltre) {
        if (dateFiltre == null) {
            dateFiltre = LocalDate.now();
        }

        List<Produit> produits;

        try {
            produits = produitDAO.getAll();
        } catch (Exception e) {
            throw new RuntimeException("Erreur chargement produits", e);
        }

        List<EtatStockDTO> result = new ArrayList<>();

        for (Produit p : produits) {

            try {

                // Stock
                double stock = mouvementDAO.getStock(
                        p.getId(),
                        dateFiltre
                );

              
                // Valeur stock
                double valeur = mouvementDAO.getValeurStock(
                        p.getId(),
                        dateFiltre,
                        p.getMethodeValuation().name()
                );

                // Prix Moyen
                double prixMoyen = 0;
                if (stock > 0) {
                    prixMoyen = valeur / stock;
                }

                EtatStockDTO dto = new EtatStockDTO();
                dto.setProduitId(p.getId());
                dto.setNomProduit(p.getNom());
                dto.setStockQuantite(stock);
                dto.setValeurStock(valeur);
                dto.setPrixMoyen(prixMoyen);
                dto.setMethode(p.getMethodeValuation().name());
                dto.setLastMovementDate(dateFiltre);

                result.add(dto);

            } catch (Exception e) {
                System.err.println(
                    "Erreur produit ID " + p.getId() + " : " + e.getMessage()
                );
            }
        }

        return result;
    }
public List<EtatStockDTO> getEtatStockByProduit(int produitId, LocalDate dateFiltre) {

    try {

        if (dateFiltre == null) {
            dateFiltre = LocalDate.now();
        }

        Produit p = produitDAO.getById(produitId);

        double stock = mouvementDAO.getStock(produitId, dateFiltre);

        double valeur = mouvementDAO.getValeurStock(
                produitId,
                dateFiltre,
                p.getMethodeValuation().name()
        );

        double prixMoyen = (stock > 0) ? (valeur / stock) : 0;

        EtatStockDTO dto = new EtatStockDTO();
        dto.setProduitId(p.getId());
        dto.setNomProduit(p.getNom());
        dto.setStockQuantite(stock);
        dto.setValeurStock(valeur);
        dto.setPrixMoyen(prixMoyen);
        dto.setMethode(p.getMethodeValuation().name());
        dto.setLastMovementDate(dateFiltre);

        return List.of(dto);

    } catch (Exception e) {

        throw new RuntimeException("Erreur EtatStockByProduit", e);
    }
}
}