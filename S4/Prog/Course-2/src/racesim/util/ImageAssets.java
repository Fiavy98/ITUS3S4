package racesim.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Supplier;
import javax.imageio.ImageIO;

public class ImageAssets {
    private static final Path CAR_IMAGE_PATH = Paths.get("data", "images", "car.png");
    private static final Path SPEEDO_IMAGE_PATH = Paths.get("data", "images", "speedometer.png");

    public static Image loadCarImage() {
        return loadOrPlaceholder(CAR_IMAGE_PATH, ImageAssets::createCarPlaceholder);
    }

    public static Image loadSpeedometerImage() {
        return loadOrPlaceholder(SPEEDO_IMAGE_PATH, ImageAssets::createSpeedometerPlaceholder);
    }

    private static Image loadOrPlaceholder(Path path, Supplier<BufferedImage> fallback) {
        if (Files.exists(path)) {
            try {
                return ImageIO.read(path.toFile());
            } catch (IOException ignored) {
            }
        }
        return fallback.get();
    }

    private static BufferedImage createCarPlaceholder() {
        int w = 80;
        int h = 40;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(220, 50, 50));
        g.fillRoundRect(6, 12, 68, 16, 10, 10);
        g.setColor(new Color(30, 30, 30));
        g.fillOval(16, 24, 12, 12);
        g.fillOval(52, 24, 12, 12);
        g.dispose();
        return img;
    }

    private static BufferedImage createSpeedometerPlaceholder() {
        int size = 200;
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(245, 245, 245));
        g.fillOval(0, 0, size, size);
        g.setColor(new Color(160, 160, 160));
        g.drawOval(2, 2, size - 4, size - 4);
        g.dispose();
        return img;
    }
}
