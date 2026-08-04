package com.gestion.stock.metier.service;
import javax.swing.Spring;

import com.gestion.stock.metier.entity.Mouvement;
import com.gestion.stock.metier.entity.Produit;
import com.gestion.stock.metier.repository.*;

public class MouvementService {
    private MouvementRepository repo;
    private ProduitRepository produitRepository;
    
    public MouvementService(MouvementRepository repo){
        this.repo=repo;
    }

    public void entree(Mouvement mvt) throws Exception {
        repo.save(mvt);
        // TODO: mise à jour stock
    }

    public void sortie(Mouvement mvt) throws Exception {
        Produit produit = produitRepository.findById(mvt.getProduitId());

        if (!"SORTIE".equals(mvt.getType())) {
            throw new IllegalArgumentException("Ce n'est pas une sortie");
        }

        String methode = produit.getMethodeValuation();

        // if(methode.equals("FIFO")){

        //     appliquerFIFO(mvt);

        // }else if(methode.equals("LIFO")){

        //     appliquerLIFO(mvt);

        // } else {
        //     appliquerCUMP(mvnt);
        // }
        repo.save(mvt);
    }

}
