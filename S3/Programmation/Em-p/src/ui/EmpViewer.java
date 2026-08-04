package ui;

import javax.swing.*;

public class EmpViewer extends JFrame {

    public EmpViewer() {
        super("Liste des Employés");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 400);
        setLocationRelativeTo(null);

        // Ajouter le panel
        add(new EmpPanel());
    }
}
