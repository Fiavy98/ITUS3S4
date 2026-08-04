package affichage;
import javax.swing.*;

public class MaFenetre extends JFrame {
    public MaFenetre(){
        setTitle("Jeux d'echec");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        MonPanel panel = new MonPanel();
        add(panel);

        setVisible(true);
    }
}