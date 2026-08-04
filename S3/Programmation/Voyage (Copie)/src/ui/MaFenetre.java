package ui;

import javax.swing.JFrame;

public class MaFenetre extends JFrame {
    public MaFenetre(){
        setTitle("VOYAGE");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        MonPanel panel = new MonPanel();
        add(panel);

    }
}
