package filemanagement;

import java.util.List;
import game.*;

public class CardLook {
    private static final List<Card> basecards = new BaseCardLibrary().getCards();
    private static final List<Card> senseiscard = new SenseiPathCardLibrary().getCards();

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
