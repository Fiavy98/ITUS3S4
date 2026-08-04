package affichage;

import javax.swing.*;

import java.awt.*;
import java.util.Vector;

public class PanelPrincipal extends JPanel{

    public PanelPrincipal() {
        setLayout(new BorderLayout());
        Terrain terrain = new Terrain();

        add(terrain,BorderLayout.CENTER);
        
    }
}
