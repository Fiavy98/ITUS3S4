package affichage;

import javax.swing.*;
import java.awt.*;
import java.awt.Color;

import data.*;
import Piece.*;

import boule.*;


public class Terrain extends JPanel{
    int ligne = 2;
    int colonne = 4;

    Data data;
    JButton btnNoir;
    JButton btnBlanc;
    public Terrain(){
        data = new Data();
               
        setLayout(new BorderLayout());
        JPanel table1 = new JPanel(new GridLayout(ligne,colonne));
        for(int row = 0; row < ligne; row++){
            for(int col = 0; col < colonne; col++){
                JPanel cases = new JPanel(new BorderLayout());
                
                for(Piece p : data.getLsPiece()){
                    if (p.getCouleur() == Colore.NOIR &&
                        p.getPosition().equals(new Position(row, col))) {

                        cases.add(createTransparentButton(p.getImg()), BorderLayout.CENTER);

                    }
                }
            
                if ((row + col) % 2 == 0) {
                    cases.setBackground(new Color(135, 206, 250)); // grenat 
                } else {
                    cases.setBackground(new Color(245, 245, 220)); // crème
                }
              table1.add(cases);
                
            }
        }

        //--------------------------------------------------------------------
        JPanel table2 = new JPanel(new GridLayout(ligne,colonne));
        for(int row = 0; row < ligne; row++){
            for(int col = 0; col < colonne; col++){
                JPanel cases = new JPanel(new BorderLayout());
                
                for(Piece p : data.getLsPiece()){
                    if (p.getCouleur() == Colore.BLANC &&
                        p.getPosition().equals(new Position(row, col))) {

                        cases.add(createTransparentButton(p.getImg()), BorderLayout.CENTER);

                     }
                }

                if ((row + col) % 2 == 0) {
                    cases.setBackground(new Color(135, 206, 250)); // grenat 
                } else {
                    cases.setBackground(new Color(245, 245, 220)); // crème
                }
              table2.add(cases);
                
            }
        }

        add(table1,BorderLayout.NORTH);
        CaseBoule caseBoule = new CaseBoule(); // taille de la zone de jeu
        add(caseBoule, BorderLayout.CENTER);

        add(table2,BorderLayout.SOUTH);
        

    }

    private JButton createTransparentButton(ImageIcon icon) {
        JButton btn = new JButton(icon);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        return btn;
    }

}
