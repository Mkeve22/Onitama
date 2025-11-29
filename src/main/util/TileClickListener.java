package util;

import view.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * Egyszerű egérfigyelő osztály, amely egy táblamezőre történt kattintást
 * továbbít a BoardFrame vezérlő felé.
 *
 * Minden TilePanel-hez tartozik egy saját TileClickListener, amely
 * elmenti a mező koordinátáit, majd kattintáskor meghívja
 * a BoardFrame megfelelő kezelő metódusát.
 */
public class TileClickListener extends MouseAdapter {

    private final int x;
    private final int y;
    private final BoardFrame controller;

    /**
     * Létrehoz egy mezőhöz kötött kattintásfigyelőt.
     *
     * @param x          A mező X koordinátája.
     * @param y          A mező Y koordinátája.
     * @param controller A játékot irányító BoardFrame példány,
     *                   amely megkapja a kattintás eseményt.
     */
    public TileClickListener(int x, int y, BoardFrame controller) {
        this.x = x;
        this.y = y;
        this.controller = controller;
    }

    /**
     * Egér lenyomásakor értesíti a BoardFrame-et,
     * hogy a (x, y) mezőn kattintás történt.
     *
     * @param e the event to be processed
     */
   @Override
    public void mousePressed(MouseEvent e) {
        controller.handleTileClick(x, y);
    }
}
