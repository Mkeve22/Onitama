package util;

import game.Card;

import javax.swing.*;
import java.awt.*;
import java.util.function.BooleanSupplier;

public class CardPanel extends JPanel {

    private Card card;
    private Image cardImage;
    private Boolean selected = false;

     public CardPanel(Card card) {
         this.card = card;
         this.cardImage = new ImageIcon(getClass().getResource(card.getImagepath())).getImage();

         setPreferredSize(new Dimension(249, 145));
         setOpaque(false);

         addMouseListener(new CardClickListener(this));
     }

     public Boolean isSelected() {
         return selected;
     }

    public void setSelected(boolean b) {
        selected = b;
        repaint();
    }

    public Card getCard() {
        return card;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(cardImage, 0, 0, getWidth(), getHeight(), null);

        // Kijelölés keret
        if (selected) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.YELLOW);
            g2.setStroke(new BasicStroke(4f));
            g2.drawRect(2, 2, getWidth() - 4, getHeight() - 4);
        }
    }
}
