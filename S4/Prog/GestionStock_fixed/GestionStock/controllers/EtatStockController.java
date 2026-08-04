package controllers;

import models.*;
import services.EtatStockService;
import dao.ProduitDAO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
public class EtatStockController {

    private EtatStockService service = new EtatStockService();

    public List<EtatStockDTO> getEtatStock(LocalDate date) {
        return service.getEtatStock(date);
    }

    public List<EtatStockDTO> getEtatStockByProduit(int produitId, LocalDate date) {
    return service.getEtatStockByProduit(produitId, date);
}
}