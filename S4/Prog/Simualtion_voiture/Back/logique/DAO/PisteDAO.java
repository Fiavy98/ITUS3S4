package Back.logique.DAO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Back.modele.Piste;

public class PisteDAO {
    public List<Piste> getAllPiste() {
        List<Piste> Piste = new ArrayList<>();
        String line = "";
        String splitBy = ",";
        try (BufferedReader br = new BufferedReader(new FileReader("data/Piste.csv"))) {
            while ((line = br.readLine()) != null) {
                String[] Pisteata = line.split(splitBy);
                Piste piste = new Piste(Float.parseFloat(Pisteata[0]), Pisteata[1]);
                Piste.add(piste);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Piste;
    }

    public Piste getPisteByName(String nom) {
        List<Piste> Piste = getAllPiste();
        for (Piste piste : Piste) {
            if (piste.getNom().equals(nom)) {
                return piste;
            }
        }
        return null;
    }
}
