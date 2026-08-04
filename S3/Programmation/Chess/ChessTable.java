package affichage;

import data.Data;
import objet.*;
import repere.*;
import acte.Action;
import javax.swing.*;
import java.awt.*;
import java.awt.Color;
public class ChessTable extends JPanel{
    int taille = 8;
    JButton deplacer;
    Position ancien;
    Position nouv;
    public ChessTable(){
        setLayout(new BorderLayout());
        JPanel table = new JPanel(new GridLayout(taille,taille));
        Data data = new Data(); 
        Deplacer depl = new Deplacer();
        for(int row = 0; row < taille; row++){
            for(int col = 0; col < taille; col++){
                JPanel cases = new JPanel(new BorderLayout());
                for(Piece p : data.getLsPiece()){
                    if(p.getPosition().equals(ancien = new Position(row,col))){
                        cases.add(deplacer = new JButton(p.getImg()));
                        // Rendre le bouton transparent
                        deplacer.setOpaque(false);  
                        deplacer.setContentAreaFilled(false); 

                        deplacer.addActionListener(new Action(p.getType(),p,ancien,nouv,depl,data,this));
                        
                    }
                }

                if((row + col)%2==0){
                    cases.setBackground(Color.WHITE);
                }else{
                    cases.setBackground(Color.GRAY);
                }

              table.add(cases);
                
            }
        }

        add(table,BorderLayout.CENTER);


    }
}