package objet;
import javax.swing.ImageIcon;

import repere.Position;
public class Piece  {
    int id;
    Type type;
    Color color;
    ImageIcon image;
    Position position;

    public Piece(int id,Type type,Color color,ImageIcon image,Position position){
        this.id=id;
        this.type=type;
        this.color=color;
        this.image=image;
        this.position=position;
    }

    public int getId(){
        return id;
    }

    public Type getType(){
        return type;
    }

        public Color getColor(){
        return color;
    }

    public Position getPosition(){
        return position;
    }
   
    public void setPosition(Position p){
        this.position=p;
    }

    public ImageIcon getImg(){
        return image;
    }
}