package src.Model;

public class Variable {
    int index;
    String name;

    public Variable(int index){
        this.index=index;
        this.name= "x"+(index +1);
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public void setIndex(int i) {
        this.index=i;
    }


    public void setName(String name) {
        this.name=name;
    }


}
