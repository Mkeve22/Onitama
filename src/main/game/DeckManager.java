package game;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeckManager {

    private List<Card> deck;
    private List<Card> player1 =  new ArrayList<>();
    private List<Card> player2 =   new ArrayList<>();
    private Card center;

    public DeckManager(CardLibrary library) {
        this.deck = new ArrayList<>(library.getCards());
        Collections.shuffle(deck);
    }

    public void dealcard(){
        player1.clear();
        player1.add(deck.get(0));
        player1.add(deck.get(1));

        player2.clear();
        player2.add(deck.get(2));
        player2.add(deck.get(3));

        center = deck.get(4);
    }


    public Card getCenter() {
        return center;
    }


    public List<Card> getPlayer1Cards() {
        return player1;
    }


    public List<Card> getPlayer2Cards() {
        return player2;
    }

    public void cardswap(Card usedcard, Boolean isPlayer1){
        Card oldcenter = center;
        center = usedcard;

        if (isPlayer1){
            player1.remove(usedcard);
            player1.add(oldcenter);
        }
        else{
            player2.remove(usedcard);
            player2.add(oldcenter);
        }
    }
}
