package services;

import models.Mouvement;
import java.util.List;

import dao.MouvementDAO;

import java.util.ArrayList;
import java.util.Collections;

public class MouvementService {
    private MouvementDAO dao = new MouvementDAO();
    
    public void ajouterMouvement(Mouvement m)
            throws Exception {
                
        if(m.getQuantite() <= 0){
            throw new Exception(
                "Quantité invalide"
            );
        }

        if(m.getPrixUnitaire() < 0){
            throw new Exception(
                "Prix invalide"
            );
        }

        if(m.getType() == null){
            throw new Exception(
                "Type obligatoire"
            );
        }

        dao.insert(m);
    }
}
