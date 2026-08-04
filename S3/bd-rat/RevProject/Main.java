public class Main {
    public static void main(String[] args) {
        String [] A={"a","d","c"};
        String [] B={"b","a","b"};
        String [] C={"c","b","d"};

        String[] name = {"A","B","C"};

        String[][] tous={A,B,C};

        Projection proj = new Projection(name, tous);
        proj.elimine(A);

        System.out.println("\nSelection");

        String[] colonnes = {"A", "B", "C"};
        String[][] R = {
            {"a", "b", "1"},
            {"d", "a", "2"},
            {"c", "b", "3"},
            {"a", "b", "4"},
            {"e", "e", "5"}
        };
    
        Select sel = new Select(colonnes, R);
        sel.select_a("B","b");
    }
}