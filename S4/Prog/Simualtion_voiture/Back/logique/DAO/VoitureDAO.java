package Back.logique.DAO;

import java.util.List;

import Back.modele.Voiture;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class VoitureDAO {
    /*
     * prendre les donnees dans un fichier csv le chemin c'est /data/voitures.csv
     */

    public List<Voiture> getAllVoitures() {
        List<Voiture> voitures = new ArrayList<>();
        String line = "";
        String splitBy = ",";
        try (BufferedReader br = new BufferedReader(new FileReader("data/voitures.csv"))) {
            while ((line = br.readLine()) != null) {
                String[] voitureData = line.split(splitBy);
                Voiture voiture = new Voiture(voitureData[0], Float.parseFloat(voitureData[1]),
                        Float.parseFloat(voitureData[2]));
                voitures.add(voiture);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return voitures;
    }

    public Voiture getVoitureByName(String nom) {
        List<Voiture> voitures = getAllVoitures();
        for (Voiture voiture : voitures) {
            if (voiture.getNom().equals(nom)) {
                return voiture;
            }
        }
        return null;
    }
}
