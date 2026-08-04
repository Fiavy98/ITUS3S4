package Piece;

public class Position {
    int row;
    int col;

    public Position(int row, int col){
        this.row=row;
        this.col=col;
    }

    public int getRow(){
        return row;
    }

    public int getCol(){
        return col;
    }

    @Override
   public boolean equals(Object o) {
        if (this == o) return true; // même objet
        if (!(o instanceof Position)) return false;
        Position pos = (Position) o;
        return this.row == pos.row && this.col == pos.col;
    }
}
