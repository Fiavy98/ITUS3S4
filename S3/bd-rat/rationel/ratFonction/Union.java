public class Union {
    //Dans e mais pas dans f
    String[] e;
    String[] f;

    public Union(String[] e, String[] f) {
        this.e = e;
        this.f = f;
    }

    public void union(String[] e, String[] f) {
        Count count = new Count();

        int taille = count.count(e) + count.count(f);
        String[] e_f = new String[taille];

        int deux = 0;

        //Copier e dans e_f
        for (int i = 0; i < count.count(e); i++) {
            e_f[i] = e[i];
            deux++;
        }

        for (int j = 0; j < count.count(f); j++) {
            boolean existe = false;

            for (int k = 0; k < deux; k++) {
                if (e_f[k].equals(f[j])) { 
                    existe = true;
                    break;
                }
            }

            if (!existe) {
                e_f[deux] = f[j];
                deux++;
            }
        }

        System.out.println("Union de e et f :");
        for (int rep = 0; rep < deux; rep++) {
            System.out.println(e_f[rep]);
        }
    }
}
