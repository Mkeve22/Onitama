package game;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A játékkártyák kiosztását és kezelését végző osztály.
 *
 * A DeckManager feladata:
 * - a teljes kártyapakli átvétele és megkeverése,
 * - a kártyák kiosztása a két játékos számára,
 * - a középső kártya beállítása.
 */
public class DeckManager {

    private List<Card> deck;
    private List<Card> player1 =  new ArrayList<>();
    private List<Card> player2 =   new ArrayList<>();
    private Card center;


    /**
     * Létrehoz egy új DeckManager példányt, amely a kapott könyvtár kártyáiból
     * készít egy megkevert paklit.
     *
     * @param library a kártyákat szolgáltató CardLibrary
     */
    public DeckManager(CardLibrary library) {
        this.deck = new ArrayList<>(library.getCards());
        Collections.shuffle(deck);
    }


    /**
     * Kiosztja a pakli legfelső öt kártyáját:
     * - az első két lap az első játékoshoz kerül,
     * - a következő két lap a második játékoshoz,
     * - az ötödik lap pedig a középső kártya lesz.
     *
     * A kiosztás előtt üríti a játékosok kezét.
     */
    public void dealcard(){
        player1.clear();
        player1.add(deck.get(0));
        player1.add(deck.get(1));

        player2.clear();
        player2.add(deck.get(2));
        player2.add(deck.get(3));

        center = deck.get(4);
    }


    /**
     * @return középső kártya
     */
    public Card getCenter() {
        return center;
    }


    /**
     * @return 1-es player kártyái
     */
    public List<Card> getPlayer1Cards() {
        return player1;
    }


    /**
     * @return 2-es player kártyái
     */
    public List<Card> getPlayer2Cards() {
        return player2;
    }
}
