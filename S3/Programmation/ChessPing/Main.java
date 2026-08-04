import affichage.MaFenetre;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MaFenetre fenetre = new MaFenetre();
            fenetre.setVisible(true);
        });
    }
}
