package game;

import java.util.Arrays;

public class GameState {

    private final Player player1;
    private final Player player2;

    private Player currentPlayer;

    private Piece[][] board = new Piece[5][5];

    // Kártyák
    private Card p1Card1;
    private Card p1Card2;
    private Card p2Card1;
    private Card p2Card2;
    private Card centerCard;

    private DeckManager deck;


    public GameState(Player p1, Player p2) {
        this.player1 = p1;
        this.player2 = p2;
        this.currentPlayer = player1;

        initBoard();
        initDeck();
    }

    private void initBoard() {

        for (Piece[] row : board) {
            Arrays.fill(row, null);
        }

        // Player 1 (bottom)
        board[4][0] = new Piece(1, false);
        board[4][1] = new Piece(1, false);
        board[4][2] = new Piece(1, true);  // Master
        board[4][3] = new Piece(1, false);
        board[4][4] = new Piece(1, false);

        // Player 2 (top)
        board[0][0] = new Piece(2, false);
        board[0][1] = new Piece(2, false);
        board[0][2] = new Piece(2, true);  // Master
        board[0][3] = new Piece(2, false);
        board[0][4] = new Piece(2, false);
    }

    private void initDeck() {
        deck = new DeckManager(new BaseCardLibrary());
        deck.dealcard();

        p1Card1 = deck.getPlayer1Cards().get(0);
        p1Card2 = deck.getPlayer1Cards().get(1);

        p2Card1 = deck.getPlayer2Cards().get(0);
        p2Card2 = deck.getPlayer2Cards().get(1);

        centerCard = deck.getCenter();
    }

    public Piece[][] getBoard() {
        return board;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Card getP1Card1() {
        return p1Card1;
    }

    public Card getP1Card2() {
        return p1Card2;
    }

    public Card getP2Card1() {
        return p2Card1;
    }

    public Card getP2Card2() {
        return p2Card2;
    }

    public Card getCenterCard() {
        return centerCard;
    }

    public boolean makeMove(Move move) {
        // TODO: Validálás
        // TODO: Lépés
        // TODO: Ütés
        // TODO: Kártyacsere
        // TODO: Győzelem ellenőrzés

        return true;
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == player1 ? player2 : player1);
    }


}
