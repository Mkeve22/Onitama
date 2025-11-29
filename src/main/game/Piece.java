package game;


/**
 * Egy bábut reprezentáló osztály az Onitama játékban.
 * Egy bábuhoz tartozik:
 * - egy tulajdonos játékos (1 vagy 2),
 * - valamint annak jelzése, hogy master-e.
 */
public class Piece {
    private final int owner;   // 1 vagy 2
    private final boolean master;

    /**
     * Létrehoz egy új bábut.
     *
     * @param owner  a bábu tulajdonosa (1 vagy 2)
     * @param master igaz, ha a bábu master
     */
    public Piece(int owner, boolean master) {
        this.owner = owner;
        this.master = master;
    }

    /**
     * @return a bábu tulajdonosának azonosítója
     */
    public int getOwner() {
        return owner;
    }

    /**
     * @return igaz, ha a bábu master figura
     */
    public boolean isMaster() {
        return master;
    }
}
