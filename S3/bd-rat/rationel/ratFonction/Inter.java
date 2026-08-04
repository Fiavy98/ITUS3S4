public class Inter {    
//Dans e et dans f
    public void inter(String[] e, String[] f) {
        Count count = new Count();
        int taille = count.count(e) + count.count(f);

        String[] e_f = new String[taille];
        int deux = 0;

        for (int i = 0; i < count.count(e); i++) { 
            boolean existe = false;

            for (int j = 0; j < count.count(f); j++) {
                if (e[i].equals(f[j])) { 
                    existe = true;
                    break; 
                }
            }

            if (existe) {
                e_f[deux] = e[i];
                deux++;
            }
        }

        System.out.println("Intersection de e et f :");
        for (int rep = 0; rep < deux; rep++) {
            System.out.println(e_f[rep]);
        }
    }
}
