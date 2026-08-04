package repere;
import objet.*;
import data.Data;

// cette classe contient le deplacement de chaque piece
public class Deplacer {
    public void DeplacePiece(Type type,Piece p,Position ancien,Position nouv,Data data){
        for(Type tp : data.getLsType()){
            if(type.getNom().equals("pion")){
                nouv=new Position(ancien.getRow() - 1,ancien.getCol());
                // if(nouv.getRow()==ancien.getRow() - 1 && nouv.getCol()==ancien.getCol()){
                    p.setPosition(nouv);
                // }
            }
        }
    }
}


