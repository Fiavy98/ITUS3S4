import ui.EmpViewer;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EmpViewer viewer = new EmpViewer();
            viewer.setVisible(true);
        });
    }
}
