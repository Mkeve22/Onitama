package util;

import javax.swing.*;
import java.awt.*;

//Saját JButton osztály képpel rendelkező gombokhoz
public class ImageButton extends JButton {
    private final Image image;
    private final ImageIcon pressedImage;


    public ImageButton(String imagePath, String pressedImagePath) {
        image = new ImageIcon(getClass().getResource(imagePath)).getImage();
        pressedImage = new ImageIcon(getClass().getResource(pressedImagePath));
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        Dimension dimension = new Dimension(328, 80);
        setPreferredSize(dimension);
        setMinimumSize(dimension);
        setMaximumSize(dimension);

    }

    //Megrajzolja a gom képét attól függően éppen meg van e nyomva a gomb
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image toDraw = getModel().isPressed() ? pressedImage.getImage() : image;
        g.drawImage(toDraw, 0, 0, getWidth(), getHeight(), this);
    }
}
