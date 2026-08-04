package Piece;
import javax.swing.ImageIcon;

public class Piece {
    int id;
    Type nom;
    Colore couleur;
    ImageIcon img;
    Puissance puissance;
    Position position;

    public Piece(int id, Type nom, Colore couleur,ImageIcon img,Puissance puissance, Position position) {
        this.id = id;
        this.nom = nom;
        this.couleur = couleur;
        this.img=img;
        this.puissance = puissance;
        this.position = position;
    }


    public int getId() {
        return id;
    }

    public Type getNom() {
        return nom;
    }

    public Colore getCouleur() {
        return couleur;
    }

    public ImageIcon getImg() {
        return img;
    }

    public Puissance getPuissance() {
        return puissance;
    }

    public Position getPosition() {
        return position;
    }
}
    

