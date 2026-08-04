package main;

import affichage.MaFenetre;
import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args){
        javax.swing.SwingUtilities.invokeLater(()-> {
            new MaFenetre();
        });
    }
}