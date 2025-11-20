package util;

import view.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TileClickListener extends MouseAdapter {

    private final int x;
    private final int y;
    private final BoardFrame controller;

    public TileClickListener(int x, int y, BoardFrame controller) {
        this.x = x;
        this.y = y;
        this.controller = controller;
    }

   @Override
    public void mousePressed(MouseEvent e) {
        controller.handleTileClick(x, y);
    }
}
