package racesim;

import javax.swing.SwingUtilities;
import racesim.ui.FenetreAccueil;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FenetreAccueil fenetre = new FenetreAccueil();
            fenetre.setVisible(true);
        });
    }
}
