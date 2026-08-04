package dao;


import javax.swing.ImageIcon;
import java.awt.Image;
import java.io.File;

public class ImageUtils {

    public static ImageIcon loadImage(String imgName, int w, int h) {

        // ✅ Protection contre NULL
        if (imgName == null || imgName.trim().isEmpty()) {
            imgName = "default.png";
        }

        File file = new File("img", imgName);

        ImageIcon icon = new ImageIcon(file.getAbsolutePath());
        Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);

        return new ImageIcon(img);
    }
}


