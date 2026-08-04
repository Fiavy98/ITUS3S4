package src.Model;

public class Symbole {
    int id;
    String name;
    public Symbole(int id,String name){
        this.id=id;
        this.name=name;
    }

      public int getid() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setid(int i) {
        this.id=i;
    }


    public void setName(String name) {
        this.name=name;
    }
}
