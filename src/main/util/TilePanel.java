package util;

import game.*;
import javax.swing.*;
import java.awt.*;
import view.*;


/**
 * A játéktábla egyik mezőjét megjelenítő panel.
 *
 * A TilePanel feladatai:
 * - tárolja a mező x és y koordinátáját,
 * - kirajzolja a rajta lévő bábut,
 * - megjeleníti a kijelölést (sárga keret),
 * - megjeleníti a lehetséges lépést (zöld háttér),
 * - speciális mezőszínezést alkalmaz a trón mezőkre,
 * - a kattintásokat továbbítja a vezérlő felé.
 *
 * A panel átlátszó háttérrel rendelkezik.
 */
public class TilePanel extends JPanel {

    private final int x;
    private final int y;
    private final GameState state;
    private boolean selected = false;

    private boolean highlighted = false;


    private static final Image redMaster   = new ImageIcon(TilePanel.class.getResource("/Piece/red_master.png")).getImage();
    private static final Image redStudent  = new ImageIcon(TilePanel.class.getResource("/Piece/red_student.png")).getImage();
    private static final Image blueMaster  = new ImageIcon(TilePanel.class.getResource("/Piece/blue_master.png")).getImage();
    private static final Image blueStudent = new ImageIcon(TilePanel.class.getResource("/Piece/blue_student.png")).getImage();

    /**
     * Konstruktor
     * Létrehozza a TilePanelt
     *
     * @param x          a mező oszlopa
     * @param y          a mező sora
     * @param state      a játék állapota
     * @param controller az a vezérlő, amelyhez a kattintási eseményeket továbbítjuk
     */
    public TilePanel(int x, int y, GameState state, BoardFrame controller) {
        this.x = x;
        this.y = y;
        this.state = state;

        setOpaque(false);

        addMouseListener(new TileClickListener(x, y, controller));
    }


    /**
     * A mező lehetséges lépési célpontként való megjelölése.
     *
     * @param h - igaz, ha a mező zölden ki legyen emelve
     */
    public void setHighlighted(boolean h) {
        this.highlighted = h;
        repaint();
    }

    /**
     * A mezőn lévő bábu frissítése és újrarajzolása.
     */
    public void updatePiece() {
        repaint();
    }

    /**
     * Visszaadja, hogy a mező zölden ki van-e emelve.
     *
     * @return igaz, ha a mező highlight állapotban van
     */
    public boolean isHighlighted() {
        return highlighted;
    }

    /**
     * Beállítja, hogy a mező ki van-e jelölve.
     *
     * @param selected igaz, ha a mező sárga keretet kap
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
        repaint();
    }


    /**
     * A mező teljes grafikus megjelenítéséért felel:
     * - király mezők színezése (felső kék, alsó piros),
     * - bábu kirajzolása,
     * - zöld kiemelés lehetséges lépéshez,
     * - fehér vagy sárga keret rajzolása.
     *
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // KIRÁLY MEZŐ SZÍNEZÉS
        if (x == 2 && y == 0) {
            g.setColor(new Color(40, 90, 200, 180)); // kék
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        if (x == 2 && y == 4) {
            g.setColor(new Color(200, 40, 40, 180)); // piros
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        // BÁBU KIRAJZOLÁSA
        Piece p = state.getBoard()[y][x];

        if (p != null) {
            Image img = p.isMaster()
                    ? (p.getOwner() == 1 ? redMaster : blueMaster)
                    : (p.getOwner() == 1 ? redStudent : blueStudent);

            int margin = 6;
            g.drawImage(img,
                    margin,
                    margin,
                    getWidth() - margin * 2,
                    getHeight() - margin * 2,
                    null);
        }

        // Ha lehetséges lépés zöld háttér
        if (highlighted) {
            g.setColor(new Color(0, 255, 0, 120));
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        // KERET FEHÉR
        Graphics2D g2 = (Graphics2D) g;

        if (selected) {
            g2.setColor(Color.YELLOW);
            g2.setStroke(new BasicStroke(4f));
        } else {
            g2.setColor(new Color(255, 255, 255, 220));
            g2.setStroke(new BasicStroke(3f));
        }

        g2.drawRect(1, 1, getWidth() - 3, getHeight() - 3);
    }

    /**
     * @return - a mező x koordinátája
     */
    public int getTileX() { return x; }

    /**
     * @return - a mező y koordinátája
     */
    public int getTileY() { return y; }
}