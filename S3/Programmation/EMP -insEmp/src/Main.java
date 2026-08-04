import ui.MaFenetre;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MaFenetre fenetre = new MaFenetre();
            fenetre.setVisible(true);
        });
    }
}

/* javac -cp "lib/ojdbc8.jar" -d out $(find src -name "*.java")


java -cp ".:lib/ojdbc8.jar:out" Main


*/