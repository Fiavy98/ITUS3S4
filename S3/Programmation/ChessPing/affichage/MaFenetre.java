package affichage;

import javax.swing.*;
import java.awt.*;

public class MaFenetre extends JFrame {
    public MaFenetre() {
        setTitle("ECHEC MIFANGARO PING/PONG");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        PanelPrincipal panel = new PanelPrincipal();
        add(panel);
        setVisible(true);
    }

}
