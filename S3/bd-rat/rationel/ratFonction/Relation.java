import java.util.*;

public class Relation {

    public List<String> creerRelation(String[] e, String[] f) {
        List<String> relation = new ArrayList<>();

        for (String a : e) {
            for (String b : f) {
                if (a.length() == b.length()) { 
                    relation.add("(" + a + ", " + b + ")");
                }
            }
        }

        return relation;
    }

}

