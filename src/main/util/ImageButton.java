package util;

import javax.swing.*;
import java.awt.*;

/**
 * Saját képet használó JButton osztály ami a képet és a kép kinézetét tárolja megnyomáskor
 */
public class ImageButton extends JButton {
    private final Image image;
    private final ImageIcon pressedImage;


    /**
     * Létrehozza a képes gombot
     * @param imagePath - Kép elérési útja
     * @param pressedImagePath - Kép elérési útja
     * @param width - szélesség
     * @param height - magasság
     */
    public ImageButton(String imagePath, String pressedImagePath, int width, int height) {
        image = new ImageIcon(getClass().getResource(imagePath)).getImage();
        pressedImage = new ImageIcon(getClass().getResource(pressedImagePath));

        // CSak a kép jelencsen meg más ne
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);

        // Méret beállítása
        Dimension dimension = new Dimension(width, height);
        setPreferredSize(dimension);
        setMinimumSize(dimension);
        setMaximumSize(dimension);

    }

    /**
     * Megrajzolja a képet a gomb teljes területére
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image toDraw = getModel().isPressed() ? pressedImage.getImage() : image;
        g.drawImage(toDraw, 0, 0, getWidth(), getHeight(), this);
    }
}
