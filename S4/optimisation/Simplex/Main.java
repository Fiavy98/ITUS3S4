import javax.swing.SwingUtilities;

import src.views.MainFrame; 

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}