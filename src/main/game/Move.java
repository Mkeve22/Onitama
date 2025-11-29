package game;


/**
 * Egy lépést reprezentáló osztály.
 * Egy lépés tartalmazza:
 * - a kiinduló mezőt (fromX, fromY),
 * - a célmezőt (toX, toY),
 * - valamint azt a kártyát, amely meghatározta a mozgást.
 *
 * A Move objektum egyetlen lépést ír le az Onitama játékban.
 */
public class Move {
    public final int fromX, fromY;
    public final int toX, toY;
    public final Card card; // melyik kártyával lépett


    /**
     * Létrehoz egy új lépést.
     *
     * @param fromX a kezdőmező x koordinátája
     * @param fromY a kezdőmező y koordinátája
     * @param toX   a célmező x koordinátája
     * @param toY   a célmező y koordinátája
     * @param card  a mozgást meghatározó kártya
     */
    public Move(int fromX, int fromY, int toX, int toY, Card card) {
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
        this.card = card;
    }
}
