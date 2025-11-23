package util;

import game.Card;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;


public class CardPanel extends JPanel {

    private Card card;
    private Image cardImage;
    private Boolean selected = false;
    private boolean rotated = false;   // 🔹 ÚJ: forgatási flag

     public CardPanel(Card card, boolean isselected) {
         this.card = card;
         this.cardImage = new ImageIcon(getClass().getResource(card.getImagepath())).getImage();

         setPreferredSize(new Dimension(249, 145));
         setOpaque(false);
        if(isselected) {
            addMouseListener(new CardClickListener(this));
        }
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

    public void setCard(Card c) {
        this.card = c;
        this.cardImage = new ImageIcon(getClass().getResource(c.getImagepath())).getImage();
        repaint();
    }

    // 🔹 ÚJ: forgatás beállítása
    public void setRotated(boolean rotated) {
        this.rotated = rotated;
        repaint();
    }

    public void enableClick() {
        for (MouseListener ml : getMouseListeners()) {
            removeMouseListener(ml);
        }
        addMouseListener(new CardClickListener(this));
    }

    public void disableClick() {
        for (MouseListener ml : getMouseListeners()) {
            removeMouseListener(ml);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 🔹 Kép rajzolása (másolt Graphics2D-vel, hogy a keret ne forogjon)
        Graphics2D g2 = (Graphics2D) g.create();

        if (rotated) {
            // a panel közepe körül forgatunk 180°-ot
            g2.rotate(Math.PI, getWidth() / 2.0, getHeight() / 2.0);
        }

        g2.drawImage(cardImage, 0, 0, getWidth(), getHeight(), null);
        g2.dispose();

        // Kijelölés keret (NEM forgatjuk)
        if (selected) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.YELLOW);
            g2d.setStroke(new BasicStroke(4f));
            g2d.drawRect(2, 2, getWidth() - 4, getHeight() - 4);
        }
    }
}
