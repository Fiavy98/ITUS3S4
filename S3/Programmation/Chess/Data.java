package data;

import objet.*;
import repere.Position;

import javax.swing.ImageIcon;
public class Data{
    Piece[] lsPiece;
    Type[] lsType;
    public Data(){
        Type cavalier = new Type("cavalier");
        Type dam = new Type("dam");
        Type fou = new Type("fou");
        Type pion = new Type("pion");
        Type roi = new Type("roi");
        Type tour = new Type("tour");

        Color blanc = new Color("Blanc");
        Color noir = new Color("Noir");

        Position pion_B = new Position(6,0);
        Position pion_N = new Position(1,0);
  
        ImageIcon pionB = new ImageIcon("image/pionB.png");
        ImageIcon pionN = new ImageIcon("image/pionN.png");

        Piece p1 = new Piece(1,pion,blanc,pionB,pion_B);
        Piece p2 = new Piece(2,pion,noir,pionN,pion_N);

        lsPiece = new Piece[]{p1,p2};
        lsType = new Type[]{cavalier,dam,fou,pion,roi,tour};
    }

    public Piece[] getLsPiece(){
        return lsPiece;
    }

    public Type[] getLsType(){
        return lsType;
    }
}