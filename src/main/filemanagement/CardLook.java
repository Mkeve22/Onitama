package filemanagement;

import java.util.List;
import game.*;

/**
 * Kártyák megkereséséért felelős segédosztály.
 * Egy kártyanevet adsz meg, és visszaadja a megfelelő Card objektumot.
 */
public class CardLook {

    // Alap kártyák listája
    private static final List<Card> basecards = new BaseCardLibrary().getCards();

    // Sensei útja kártyák listája
    private static final List<Card> senseiscard = new SenseiPathCardLibrary().getCards();

    /**
     * Megkeresi és visszaadja a megadott nevű kártyát.
     * Először az alap kártyákban, majd a SenseiPath kártyákban keres.
     *
     * @param cardName a keresett kártya neve
     * @return a megfelelő Card objektum
     * @throws IllegalArgumentException ha nincs ilyen nevű kártya
     */
    public static Card getbyName(String cardName) {
        for (Card card : basecards) {
            if (card.getName().equals(cardName)) {
                return card;
            }
        }
        for (Card card : senseiscard) {
            if (card.getName().equals(cardName)) {
                return card;
            }
        }
        throw new IllegalArgumentException("Nem létezik ilyen kártya: " + cardName);
    }
}
