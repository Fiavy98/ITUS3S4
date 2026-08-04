package controllers;

import models.*;
import services.MouvementService;
import views.MvntStockView;

import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MouvementController {
    private MouvementService service = new MouvementService();

      public boolean Ajouter(Mouvement m){
        try {
            service.ajouterMouvement(m);
            return true;

        } catch (Exception e) {

            System.out.println(e.getMessage());
            return false;
        }
    }
}
