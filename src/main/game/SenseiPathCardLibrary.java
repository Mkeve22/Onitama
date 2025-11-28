package game;

import java.util.ArrayList;
import java.util.List;

public class SenseiPathCardLibrary implements CardLibrary {

    @Override
    public List<Card> getCards() {
        List<Card> cards = new ArrayList<>();

        cards.add(new Card("Antelope", "/SenseiPath/antelope.png", new int[][]{
                {-2, 0}, {-1, -1}, {1, 1}
        }));

        cards.add(new Card("Bear", "/SenseiPath/bear.png", new int[][]{
                {0, 1}, {1, -1}, {-1, 1}
        }));

        cards.add(new Card("Dog", "/SenseiPath/dog.png", new int[][]{
                {-1, 0}, {-1, 1}, {-1, -1}
        }));

        cards.add(new Card("Fox", "/SenseiPath/fox.png", new int[][]{
                {1, -1}, {1, 1}, {1, 0}
        }));

        cards.add(new Card("Giraffe", "/SenseiPath/giraffe.png", new int[][]{
                {0, -1}, {-2, 1}, {2, 1}
        }));

        cards.add(new Card("Goat", "/SenseiPath/goat.png", new int[][]{
                {-1, 0}, {0, -1}, {1, 1}
        }));

        cards.add(new Card("Iguana", "/SenseiPath/iguana.png", new int[][]{
                {-2, 1}, {0, 1}, {1, -1}
        }));

        cards.add(new Card("Kirin", "/SenseiPath/kirin.png", new int[][]{
                {1, 2}, {-1, 2}, {0, -2}
        }));

        cards.add(new Card("Mouse", "/SenseiPath/mouse.png", new int[][]{
                {-1, -1}, {0, 1}, {1, 0}
        }));

        cards.add(new Card("Otter", "/SenseiPath/otter.png", new int[][]{
                {-1, 1}, {2, 0}, {1, -1}
        }));

        cards.add(new Card("Panda", "/SenseiPath/panda.png", new int[][]{
                {-1, -1}, {0, 1}, {1, 1}
        }));

        cards.add(new Card("Phoenix", "/SenseiPath/phoenix.png", new int[][]{
                {-2, 0}, {2, 0}, {-1, 1}, {1, 1}
        }));

        cards.add(new Card("Raccoon", "/SenseiPath/raccoon.png", new int[][]{
                {-1, -1}, {2, 1}, {0, 1}
        }));

        cards.add(new Card("Rat", "/SenseiPath/rat.png", new int[][]{
                {-1, 0}, {1, -1}, {0, 1}
        }));

        cards.add(new Card("Seasnake", "/SenseiPath/seasnake.png", new int[][]{
                {-1, -1}, {0, 1}, {2, 0}
        }));

        cards.add(new Card("Sheep", "/SenseiPath/sheep.png", new int[][]{
                {-1, 1}, {0, -1}, {1, 0}
        }));

        cards.add(new Card("Turtle", "/SenseiPath/turtle.png", new int[][]{
                {-2, 0}, {-1, -1}, {2, 0}, {1, -1}
        }));

        cards.add(new Card("Viper", "/SenseiPath/viper.png", new int[][]{
                {-2, 0}, {0, 1}, {-1, -1}
        }));

        return cards;
    }
}