package data;

import objet.*;

public class Data {
    Faritany[] listFaritany;
    Faritra[] listFaritra;
    Distrika[] listDistrika;
    Bv[] listBv;
    Candidat[] listCandidat;

public Data() {
    // --- Faritany ---
    Faritany faritany1 = new Faritany("Faritany 1");
    Faritany faritany2 = new Faritany("Faritany 2");

    // --- Faritra ---
    Faritra faritra1 = new Faritra("Faritra 1", faritany1);
    Faritra faritra2 = new Faritra("Faritra 2", faritany1);
    Faritra faritra3 = new Faritra("Faritra 3", faritany2);
    Faritra faritra4 = new Faritra("Faritra 4", faritany2);

    faritany1.AjouteFaritra(faritra1);
    faritany1.AjouteFaritra(faritra2);
    faritany2.AjouteFaritra(faritra3);
    faritany2.AjouteFaritra(faritra4);

    // --- Distrika ---
    Distrika distrika1 = new Distrika("Distrika 1", faritra1);
    Distrika distrika2 = new Distrika("Distrika 2", faritra1);

    Distrika distrika3 = new Distrika("Distrika 3", faritra2);
    Distrika distrika4 = new Distrika("Distrika 4", faritra2);

    Distrika distrika5 = new Distrika("Distrika 5", faritra3);
    Distrika distrika6 = new Distrika("Distrika 6", faritra3);

    Distrika distrika7 = new Distrika("Distrika 7", faritra4);
    Distrika distrika8 = new Distrika("Distrika 8", faritra4);

    faritra1.AjoutDistrika(distrika1);
    faritra1.AjoutDistrika(distrika2);

    faritra2.AjoutDistrika(distrika3);
    faritra2.AjoutDistrika(distrika4);

    faritra3.AjoutDistrika(distrika5);
    faritra3.AjoutDistrika(distrika6);

    faritra4.AjoutDistrika(distrika7);
    faritra4.AjoutDistrika(distrika8);

    // --- BV ---
    Bv bv1  = new Bv("BV 1", distrika1);
    Bv bv2  = new Bv("BV 2", distrika1);

    Bv bv3  = new Bv("BV 3", distrika2);
    Bv bv4  = new Bv("BV 4", distrika2);

    Bv bv5  = new Bv("BV 5", distrika3);
    Bv bv6  = new Bv("BV 6", distrika3);

    Bv bv7  = new Bv("BV 7", distrika4);
    Bv bv8  = new Bv("BV 8", distrika4);

    Bv bv9  = new Bv("BV 9", distrika5);
    Bv bv10 = new Bv("BV 10", distrika5);

    Bv bv11 = new Bv("BV 11", distrika6);
    Bv bv12 = new Bv("BV 12", distrika6);

    Bv bv13 = new Bv("BV 13", distrika7);
    Bv bv14 = new Bv("BV 14", distrika7);

    Bv bv15 = new Bv("BV 15", distrika8);
    Bv bv16 = new Bv("BV 16", distrika8);

    distrika1.AjoutBv(bv1); distrika1.AjoutBv(bv2);
    distrika2.AjoutBv(bv3); distrika2.AjoutBv(bv4);
    distrika3.AjoutBv(bv5); distrika3.AjoutBv(bv6);
    distrika4.AjoutBv(bv7); distrika4.AjoutBv(bv8);
    distrika5.AjoutBv(bv9); distrika5.AjoutBv(bv10);
    distrika6.AjoutBv(bv11); distrika6.AjoutBv(bv12);
    distrika7.AjoutBv(bv13); distrika7.AjoutBv(bv14);
    distrika8.AjoutBv(bv15); distrika8.AjoutBv(bv16);

    // --- Candidat (1 par BV) ---
    Candidat c1  = new Candidat("Candidat 1",  1, bv1, 0);
    Candidat c2  = new Candidat("Candidat 2",  2, bv2, 0);
    Candidat c3  = new Candidat("Candidat 3",  3, bv3, 0);
    Candidat c4  = new Candidat("Candidat 4",  4, bv4, 0);
    Candidat c5  = new Candidat("Candidat 5",  5, bv5, 0);
    Candidat c6  = new Candidat("Candidat 6",  6, bv6, 0);
    Candidat c7  = new Candidat("Candidat 7",  7, bv7, 0);
    Candidat c8  = new Candidat("Candidat 8",  8, bv8, 0);
    Candidat c9  = new Candidat("Candidat 9",  9, bv9, 0);
    Candidat c10 = new Candidat("Candidat 10",10, bv10,0);
    Candidat c11 = new Candidat("Candidat 11",11, bv11,0);
    Candidat c12 = new Candidat("Candidat 12",12, bv12,0);
    Candidat c13 = new Candidat("Candidat 13",13, bv13,0);
    Candidat c14 = new Candidat("Candidat 14",14, bv14,0);
    Candidat c15 = new Candidat("Candidat 15",15, bv15,0);
    Candidat c16 = new Candidat("Candidat 16",16, bv16,0);

    bv1.AjoutCandidat(c1);   bv2.AjoutCandidat(c2);
    bv3.AjoutCandidat(c3);   bv4.AjoutCandidat(c4);
    bv5.AjoutCandidat(c5);   bv6.AjoutCandidat(c6);
    bv7.AjoutCandidat(c7);   bv8.AjoutCandidat(c8);
    bv9.AjoutCandidat(c9);   bv10.AjoutCandidat(c10);
    bv11.AjoutCandidat(c11); bv12.AjoutCandidat(c12);
    bv13.AjoutCandidat(c13); bv14.AjoutCandidat(c14);
    bv15.AjoutCandidat(c15); bv16.AjoutCandidat(c16);

    // --- Initialisation des listes ---
    listFaritany = new Faritany[]{faritany1, faritany2};
    listFaritra  = new Faritra[]{faritra1, faritra2, faritra3, faritra4};
    listDistrika = new Distrika[]{distrika1, distrika2, distrika3, distrika4, distrika5, distrika6, distrika7, distrika8};
    listBv       = new Bv[]{bv1, bv2, bv3, bv4, bv5, bv6, bv7, bv8, bv9, bv10, bv11, bv12, bv13, bv14, bv15, bv16};
    listCandidat = new Candidat[]{c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c13,c14,c15,c16};
}

    public Faritany[] LsFaritany(){
      return listFaritany;
    }

    public Faritra[] LsFaritra(){
      return listFaritra;
    }

        public Distrika[] LsDistrika(){
      return listDistrika;
    }

    public Bv[] LsBv(){
      return listBv;
    }

    public Candidat[] LsCandidat(){
      return listCandidat;
    }
}
