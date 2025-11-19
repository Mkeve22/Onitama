package game;

public class Move {
    public final int fromX, fromY;
    public final int toX, toY;
    public final Card card; // melyik kártyával lépett

    public Move(int fromX, int fromY, int toX, int toY, Card card) {
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
        this.card = card;
    }
}
