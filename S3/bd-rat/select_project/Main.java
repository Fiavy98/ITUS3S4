public class Main {
    public static void main(String[] args) {
        String[] e={"A","B","A"};
        String[] f={"D","E","F"};
        String[] g={"2","4","5"};

        String[] name = {"e","f","g"};
        String[][] tous = {e, f, g};

        System.out.println("Projection :");
        Projection proj = new Projection(e, f, g,name,tous);
        proj.elimine(g);


        
        System.out.println("\nSelection :");

        String[] colonnes = {"A", "B", "C"};
        String[][] R = {
            {"a", "b", "1"},
            {"d", "a", "2"},
            {"c", "b", "3"},
            {"a", "b", "4"},
            {"e", "e", "5"}
        };
    
        Select s = new Select();
        s.select(R, colonnes, "A", "a"); 
        
        System.out.println("\nSelection b :");

        s.select_b(R, colonnes, "a", "a", 3);
        System.out.println("\nSelection c :");
        s.selec_c(R, colonnes);
    }
    
}

