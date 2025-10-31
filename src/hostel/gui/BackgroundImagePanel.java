package hostel.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

public class BackgroundImagePanel extends JPanel {

    private Image backgroundImage;

    public BackgroundImagePanel(String imagePath) {
        try (InputStream is = getClass().getResourceAsStream(imagePath)) {
            if (is == null) {
                throw new RuntimeException("Background image not found at path: " + imagePath);
            }
            backgroundImage = ImageIO.read(is);
        } catch (Exception e) {
            System.err.println("Error loading background image: " + e.getMessage());
            backgroundImage = null;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
