package affichage;
import javax.swing.*;

public class MaFenetre extends JFrame{
    public MaFenetre(){
        setTitle("Election");
        setSize(700,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        MonPanel panel = new MonPanel();
        add(panel);

        setVisible(true);
    }
}