import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import Swing.MenuSwing;

public class MainSwing {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Simulation voiture - Swing");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new MenuSwing(frame));
            frame.setVisible(true);
        });
    }
}