package util;

import game.Card;

import javax.swing.*;
import java.awt.*;

public class CardPanel extends JPanel {

    private Card card;
    private Image cardImage;

     public CardPanel(Card card) {
         this.card = card;
         this.cardImage = new ImageIcon(getClass().getResource(card.getImagepath())).getImage();

         setPreferredSize(new Dimension(249, 145));
         setOpaque(false);
     }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(cardImage, 0, 0, getWidth(), getHeight(), null);
    }
}
