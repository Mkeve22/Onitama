package game;


public class Piece {
    private final int owner;   // 1 vagy 2
    private final boolean master;

    public Piece(int owner, boolean master) {
        this.owner = owner;
        this.master = master;
    }

    public int getOwner() {
        return owner;
    }

    public boolean isMaster() {
        return master;
    }
}
