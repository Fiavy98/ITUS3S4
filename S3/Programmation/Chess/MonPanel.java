package affichage; 
import javax.swing.*;
import java.awt.*;

public class MonPanel extends JPanel {
    public MonPanel(){
        setLayout(new BorderLayout());
        ChessTable table = new ChessTable();
        add(table,BorderLayout.CENTER);

    }
}