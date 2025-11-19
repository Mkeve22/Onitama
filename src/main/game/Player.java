package game;

public interface Player {
    int getId();
    boolean isAI();
    Move decideMove(GameState state); //csak AI
}
