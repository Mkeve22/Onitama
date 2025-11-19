package game;

public class HumanPlayer implements Player {

    private int id;

    public HumanPlayer(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean isAI() {
        return false;
    }

    @Override
    public Move decideMove(GameState state) {
        return null; //Ember nem generál lépéseket
    }
}
