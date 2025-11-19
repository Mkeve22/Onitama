package game;

import java.util.ArrayList;
import java.util.List;

public class BaseCardLibrary implements CardLibrary {

    @Override
    public List<Card> getCards() {
        List<Card> cards = new ArrayList<>();

        cards.add(new Card("Tiger", "/BaseCards/Tiger.png", new int[][]{
                {0, 2}, {0, -1}
        }));

        cards.add(new Card("Rooster", "/BaseCards/Rooster.png", new int[][]{
                {-1, 0}, {-1, -1}, {1, 0}, {1, 1}
        }));

        cards.add(new Card("Rabbit", "/BaseCards/Rabbit.png", new int[][]{
                {-1, -1}, {1, 1}, {2, 0}
        }));

        cards.add(new Card("Ox", "/BaseCards/Ox.png", new int[][]{
                {0, 1}, {1, 0}, {0, -1}
        }));

        cards.add(new Card("Monkey", "/BaseCards/Monkey.png", new int[][]{
                {-1, 1}, {1, 1}, {-1, -1}, {1, -1}
        }));

        cards.add(new Card("Horse", "/BaseCards/Horse.png", new int[][]{
                {0, 1}, {0, -1}, {-1, 0}
        }));

        cards.add(new Card("Goose", "/BaseCards/Goose.png", new int[][]{
                {-1, 0}, {1, 0},  {-1, 1}, {1, -1}
        }));

        cards.add(new Card("Frog", "/BaseCards/Frog.png", new int[][]{
                {-1, 1}, {-2, 0}, {1, -1}
        }));

        cards.add(new Card("Elephant", "/BaseCards/Elephant.png", new int[][]{
                {-1, 0}, {-1, 1}, {1, 0}, {1, 1}
        }));

        cards.add(new Card("Eel", "/BaseCards/Eel.png", new int[][]{
                {-1, 1}, {-1, -1}, {1, 0}
        }));

        cards.add(new Card("Dragon", "/BaseCards/Dragon.png", new int[][]{
                {-1, -1}, {-2, 1},  {1, -1}, {2, 1}
        }));

        cards.add(new Card("Crane", "/BaseCards/Crane.png", new int[][]{
                {-1, -1}, {1, -1}, {0, 1}
        }));

        cards.add(new Card("Crab", "/BaseCards/Crab.png", new int[][]{
                {-2, 0}, {0, 1},  {2, 0}
        }));

        cards.add(new Card("Cobra", "/BaseCards/Cobra.png", new int[][]{
                {-1, 0}, {1, -1}, {1, 1}
        }));

        cards.add(new Card("Boar", "/BaseCards/Boar.png", new int[][]{
                {-1, 0}, {0, 1}, {1, 0}
        }));

        cards.add(new Card("Ant", "/BaseCards/Ant.png", new int[][]{
                {-1, 1}, {1, 1}, {0, -1}
        }));
        return cards;
    }
}
