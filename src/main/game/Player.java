package game;


/**
 * A játékosokat leíró interfész.
 * Egy játékos lehet emberi vagy AI típusú.
 * A lépésgenerálást (decideMove) csak az AI használja.
 */
public interface Player {

    /**
     * @return a játékos azonosítója (1 vagy 2)
     */
    int getId();


    /**
     * @return igaz, ha a játékos AI vezérelt
     */
    boolean isAI();


    /**
     * Az AI játékos ezzel a metódussal generál lépést.
     * Az emberi játékos esetén ez általában null-t ad vissza.
     *
     * @param state az aktuális játékállapot
     * @return az AI által kiválasztott lépés, vagy ember esetén null
     */
    Move decideMove(GameState state); //csak AI
}
