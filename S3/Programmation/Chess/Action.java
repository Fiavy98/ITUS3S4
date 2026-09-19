package acte;
import objet.*;
import data.Data;
import repere.*;
import affichage.ChessTable;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Action implements ActionListener {
    Type type;
    Piece piece;
    Position ancien;
    Position nouv;
    Deplacer deplacer;
    Data data;        
    ChessTable chessTable;

    public Action(Type type,Piece piece,Position ancien,Position nouv,Deplacer deplacer,Data data, ChessTable chessTable){
        this.type=type;
        this.piece=piece;
        this.ancien=ancien;
        this.nouv=nouv;
        this.deplacer=deplacer;
        this.chessTable = chessTable;
        this.data=data;
    }

    public void redessiner() {

}

    @Override
    public void actionPerformed(ActionEvent e){
        deplacer.DeplacePiece(type,piece,ancien,nouv,data);
        chessTable.redessiner();

    }
}
