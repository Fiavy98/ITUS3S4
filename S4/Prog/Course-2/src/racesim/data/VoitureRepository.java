package racesim.data;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import racesim.model.Voiture;

public class VoitureRepository {
    private final Path filePath;

    public VoitureRepository(Path filePath) {
        this.filePath = filePath;
    }

    public static VoitureRepository defaultRepo() {
        return new VoitureRepository(Paths.get("data", "voitures.txt"));
    }

    public List<Voiture> load() {
        ensureFileExistsWithDefaults();
        List<Voiture> voitures = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            for (String line : lines) {
                Voiture voiture = parseLine(line);
                if (voiture != null) {
                    voitures.add(voiture);
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur de lecture: " + e.getMessage());
        }
        return voitures;
    }

    public void append(Voiture voiture) throws IOException {
        Files.createDirectories(filePath.getParent());
        String line = voiture.getNom() + ";" + voiture.getAccelerationKmHPerSec()
                + ";" + voiture.getVitesseMaxKmH();
        Files.write(
                filePath,
                Collections.singletonList(line),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        );
    }

    private void ensureFileExistsWithDefaults() {
        if (Files.exists(filePath)) {
            return;
        }
        try {
            Files.createDirectories(filePath.getParent());
            List<String> defaults = Arrays.asList(
                    "Ferrari;20;320",
                    "Bugatti;25;400",
                    "Toyota;10;180"
            );
            Files.write(filePath, defaults, StandardCharsets.UTF_8, StandardOpenOption.CREATE);
        } catch (IOException e) {
            System.err.println("Erreur creation fichier: " + e.getMessage());
        }
    }

    private Voiture parseLine(String line) {
        String trimmed = line.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        String[] parts = trimmed.split(";");
        if (parts.length < 3) {
            return null;
        }
        String nom = parts[0].trim();
        Double accel = parseDouble(parts[1]);
        Double vmax = parseDouble(parts[2]);
        if (nom.isEmpty() || accel == null || vmax == null) {
            return null;
        }
        return new Voiture(nom, accel, vmax);
    }

    private Double parseDouble(String text) {
        String normalized = text.trim().replace(',', '.');
        if (normalized.isEmpty()) {
            return null;
        }
        try {
            return Double.parseDouble(normalized);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
