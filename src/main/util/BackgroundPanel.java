package util;

import javax.swing.*;
import java.awt.*;

/**
 * Saját JPanel a háttérképnek ami eg ykép és igazodik a mérete az ablak méretéhez
 */
public class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    /**
     * Konstruktor
     *
     * Beállítja a képet adattagnak
     * @param imagePath A kép elérési útvonala
     */
    public BackgroundPanel(String imagePath) {
        ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
        backgroundImage = icon.getImage();
    }

    /**
     * Kirajzolja a képet az ablak teljes méretére
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
