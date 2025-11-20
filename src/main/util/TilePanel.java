package util;

import game.*;
import javax.swing.*;
import java.awt.*;
import view.*;

public class TilePanel extends JPanel {

    private final int x;
    private final int y;
    private final GameState state;
    private boolean selected = false;
    // 🔥 MOD – új flag a zöld kiemeléshez
    private boolean highlighted = false;            // <-- MOD

    // 🔥 MOD – setter/getter
    public void setHighlighted(boolean h) {         // <-- MOD
        this.highlighted = h;
        repaint();
    }

    public boolean isHighlighted() {                // <-- MOD
        return highlighted;
    }

    private static final Image redMaster   = new ImageIcon(TilePanel.class.getResource("/Piece/red_master.png")).getImage();
    private static final Image redStudent  = new ImageIcon(TilePanel.class.getResource("/Piece/red_student.png")).getImage();
    private static final Image blueMaster  = new ImageIcon(TilePanel.class.getResource("/Piece/blue_master.png")).getImage();
    private static final Image blueStudent = new ImageIcon(TilePanel.class.getResource("/Piece/blue_student.png")).getImage();

    public TilePanel(int x, int y, GameState state, BoardFrame controller) {
        this.x = x;
        this.y = y;
        this.state = state;

        setOpaque(false);

        addMouseListener(new TileClickListener(x, y, controller));
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        repaint();
    }

    public boolean isSelected() {
        return selected;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 1) KIRÁLY MEZŐ SZÍNEZÉS
        if (x == 2 && y == 0) {
            g.setColor(new Color(40, 90, 200, 180)); // kék
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        if (x == 2 && y == 4) {
            g.setColor(new Color(200, 40, 40, 180)); // piros
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        // 2) BÁBU KIRAJZOLÁSA
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

        //ha lehetséges lépés → zöld háttér
        if (highlighted) {
            g.setColor(new Color(0, 255, 0, 120));
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        // 3) KERET FEHÉR
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

    public int getXCoord() { return x; }
    public int getTileX() { return x; }
    public int getTileY() { return y; }
}