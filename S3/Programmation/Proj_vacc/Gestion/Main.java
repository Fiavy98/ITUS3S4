import javax.swing.*;
import view.MyFrame;
public class Main {
    public static void main(String[] args) {
         SwingUtilities.invokeLater(() -> new MyFrame());
    }
}