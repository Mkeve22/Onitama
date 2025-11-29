package game;


/**
 * Egy emberi játékost reprezentáló osztály.
 * Nem generál automatikus lépéseket, a játékos kattintással irányítja a bábukat.
 */
public class HumanPlayer implements Player {

    private int id;

    /**
     * Létrehoz egy új emberi játékost.
     *
     * @param id a játékos azonosítója
     */
    public HumanPlayer(int id) {
        this.id = id;
    }


    /**
     * @return a játékos azonosítója
     */
    @Override
    public int getId() {
        return id;
    }


    /**
     * @return mindig false, mert ez a játékos nem AI
     */
    @Override
    public boolean isAI() {
        return false;
    }

    /**
     * Az emberi játékos nem generál lépést automatikusan,
     * ezért ez mindig null-t ad vissza.
     *
     * @param state az aktuális játékállapot
     * @return mindig null
     */
    @Override
    public Move decideMove(GameState state) {
        return null; //Ember nem generál lépéseket
    }
}
