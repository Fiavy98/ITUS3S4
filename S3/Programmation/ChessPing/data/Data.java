package data;
import javax.swing.ImageIcon;

import Piece.*;

public class Data {
    Piece[] lsPiece;
    
    public Data(){
        Type pion = new Type("pion");
        Type tour = new Type("tour");
        Type reine = new Type("reine");
        Type roi = new Type("roi");
        
        Puissance puiss_Pion = new Puissance(1);
        Puissance puiss_Tour = new Puissance(3);
        Puissance puiss_Reine = new Puissance(5);
        Puissance puiss_Roi = new Puissance(8);


        ImageIcon img_pionN = new ImageIcon("image/pionN.png");
        ImageIcon img_pionB = new ImageIcon("image/pionB.png");

        ImageIcon img_tourN = new ImageIcon("image/tourN.png");
        ImageIcon img_tourB = new ImageIcon("image/tourB.png");

        ImageIcon img_reineN = new ImageIcon("image/fouN.png");
        ImageIcon img_reineB = new ImageIcon("image/fouB.png");

        ImageIcon img_roiN = new ImageIcon("image/roiN.png");
        ImageIcon img_roiB = new ImageIcon("image/roiB.png");



        //=====================NOIR=======================
        //===Position===
        //---Pion---
        Position posit_pN1 = new Position(1, 0);
        Position posit_pN2 = new Position(1, 1);
        Position posit_pN3 = new Position(1, 2);
        Position posit_pN4 = new Position(1, 3);   
        //---Tour--- 
        Position posit_trN1 = new Position(0, 0);
        Position posit_trN2 = new Position(0, 3);
        //--Reine--
        Position posit_rnN1 = new Position(0, 1);
        //---Roi--
        Position posit_roiN1 = new Position(0, 2);

        
        //===Piece===
        //Pion
        Piece pN1 = new Piece(30, pion, Colore.NOIR, img_pionN, puiss_Pion, posit_pN1);
        Piece pN2 = new Piece(31, pion, Colore.NOIR, img_pionN, puiss_Pion, posit_pN2);
        Piece pN3 = new Piece(32, pion, Colore.NOIR, img_pionN, puiss_Pion, posit_pN3);
        Piece pN4 = new Piece(33, pion, Colore.NOIR, img_pionN, puiss_Pion, posit_pN4);
        //Tour
        Piece trN1 = new Piece(00, tour, Colore.NOIR, img_tourN, puiss_Tour, posit_trN1);
        Piece trN2 = new Piece(03, tour, Colore.NOIR, img_tourN, puiss_Tour, posit_trN2);
        //Reine
        Piece rnN1 = new Piece(01, reine, Colore.NOIR, img_reineN, puiss_Reine, posit_rnN1);
        //Roi
        Piece roiN1 = new Piece(02, roi, Colore.NOIR, img_roiN, puiss_Roi, posit_roiN1);


        //====================BLANC======================
        //Position
        Position posit_pB1 = new Position(0, 0);
        Position posit_pB2 = new Position(0, 1);
        Position posit_pB3 = new Position(0, 2);
        Position posit_pB4 = new Position(0, 3);
        //---Tour--- 
        Position posit_trB1 = new Position(1, 0);
        Position posit_trB2 = new Position(1, 3);
        //--Reine--
        Position posit_rnB1 = new Position(1, 1);
        //---Roi--
        Position posit_roiB1 = new Position(1, 2);


        //=====Piece===
        //Pion
        Piece pB1 = new Piece(00, pion, Colore.BLANC, img_pionB, puiss_Pion, posit_pB1);
        Piece pB2 = new Piece(01, pion, Colore.BLANC, img_pionB, puiss_Pion, posit_pB2);
        Piece pB3 = new Piece(02, pion, Colore.BLANC, img_pionB, puiss_Pion, posit_pB3);
        Piece pB4 = new Piece(03, pion, Colore.BLANC, img_pionB, puiss_Pion, posit_pB4);
        //Tour
        Piece trB1 = new Piece(10, tour, Colore.BLANC, img_tourB, puiss_Tour, posit_trB1);
        Piece trB2 = new Piece(13, tour, Colore.BLANC, img_tourB, puiss_Tour, posit_trB2);
        //Reine
        Piece rnB1 = new Piece(11, reine, Colore.BLANC, img_reineB, puiss_Reine, posit_rnB1);
        //Roi
        Piece roiB1 = new Piece(12, roi, Colore.BLANC, img_roiB, puiss_Roi, posit_roiB1);



        // Tableau contenant toutes les pièces
        lsPiece = new Piece[]{
            pN1, pN2, pN3, pN4,
            trN1, trN2,
            rnN1, roiN1,
            pB1, pB2, pB3, pB4,
            trB1, trB2,
            rnB1, roiB1
        };


        
    }

    public Piece[] getLsPiece(){
        return lsPiece;
    }

}
