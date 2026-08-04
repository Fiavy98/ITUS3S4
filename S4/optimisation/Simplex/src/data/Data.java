package src.data;
import java.util.ArrayList;
import java.util.List;
import src.Model.*;

public class Data {

    public List<Variable> lsVariable = new ArrayList<>();

    Variable v1 = new Variable(1);
    Variable v2 = new Variable(2);
    Variable v3 = new Variable(3);
    Variable v4 = new Variable(4);

    public Data() {
        lsVariable.add(v1);
        lsVariable.add(v2);
        lsVariable.add(v3);
        lsVariable.add(v4);
    }

    public void afficherVariables() {
        for (Variable v : lsVariable) {
            System.out.println(v);
        }
    }
}