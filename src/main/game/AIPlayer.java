package game;


public class AIPlayer implements Player {

    private final int id;

    public AIPlayer(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean isAI() {
        return true;
    }

    @Override
    public Move decideMove(GameState state) {
        // Később ide tesszük az AI logikát
        return null;
    }
}
