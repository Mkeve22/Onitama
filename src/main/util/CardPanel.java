package util;

import game.Card;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

/**
 * Saját JPanel osztály a kártyáknak beállítja a kártyák képét,
 * lehetséges lépéseiket, kiválasztási állapotát,
 * valamint attól függően hogy melyik játékosnál van éppen a kártya az elforgathatóságát is tárolja
 */
public class CardPanel extends JPanel {

    private Card card;
    private Image cardImage;
    private Boolean selected = false;
    private boolean rotated = false;

    /**
     * Konstruktor
     * Beállítja a panel adattagjait és méretét (minden kártya ugyan akkora),
     * valamint hozzáadja a MousListenert ha kell
     * @param card
     * @param isselected
     */
     public CardPanel(Card card, boolean isselected) {
         this.card = card;
         this.cardImage = new ImageIcon(getClass().getResource(card.getImagepath())).getImage();

         setPreferredSize(new Dimension(249, 145));
         setOpaque(false);
         if(isselected) {
             addMouseListener(new CardClickListener(this));
         }
     }

    /**
     * Getter ami visszaadja hogy ki van e választva a kártya
     * @return Boolean
     */
     public Boolean isSelected() {
         return selected;
     }

    /**
     * Beállítja a kiválasztottság értékét,
     * majd ujra rajzoltatja a kártyát
     * @param b -Boolean
     */
    public void setSelected(boolean b) {
         selected = b;
         repaint();
     }

    /**
     * Visszaadja a Kártyát
     * @return Card
     */
    public Card getCard() {
        return card;
     }

    /**
     * Beállítja a kártya képépét és a kártyát egy adott kártya alapján,
     * majd ujra rajzoltatja
     * @param c
     */
    public void setCard(Card c) {
        this.card = c;
        this.cardImage = new ImageIcon(getClass().getResource(c.getImagepath())).getImage();
        repaint();
    }

    /**
     * Beállítja hogy el kelll-e forgatni a kártyát,
     * mmajd ujra rajzoltatja
     * @param rotated
     */
    public void setRotated(boolean rotated) {
        this.rotated = rotated;
        repaint();
    }

    /**
     * Kattinthatóvá teszi a kártyát
     */
    public void enableClick() {
        for (MouseListener ml : getMouseListeners()) {
            removeMouseListener(ml);
        }
        addMouseListener(new CardClickListener(this));
    }

    /**
     * Elveszi a kattinthatóságát a kártyának
     */
    public void disableClick() {
        for (MouseListener ml : getMouseListeners()) {
            removeMouseListener(ml);
        }
    }


    /**
     * A kártya megrajzolásáért felel
     * Ha meg kell forgatni a képet megforgatva rajzolja ki és ha ki van választva Sárga keretet csinál neki.
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Kép rajzolása
        Graphics2D g2 = (Graphics2D) g.create();

        if (rotated) {
            // A panel közepe körül forgatunk 180°-ot
            g2.rotate(Math.PI, getWidth() / 2.0, getHeight() / 2.0);
        }

        g2.drawImage(cardImage, 0, 0, getWidth(), getHeight(), null);
        g2.dispose();

        // Kijelölés keret
        if (selected) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.YELLOW);
            g2d.setStroke(new BasicStroke(4f));
            g2d.drawRect(2, 2, getWidth() - 4, getHeight() - 4);
        }
    }
}
