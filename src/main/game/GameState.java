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

    private GameMode mode;

    private Integer winner = null;  // 1 vagy 2, ha már van győztes
    private int library;


    public GameState(Player p1, Player p2, GameMode mode, int library) {
        this.player1 = p1;
        this.player2 = p2;
        this.currentPlayer = player1;
        this.mode = mode;
        this.library = library;

        initBoard();
        initDeck(library);
    }

    public GameMode getMode() { return mode; }

    public Integer getWinner() {
        return winner;
    }

    public boolean isGameOver() {
        return winner != null;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public int getLibrary() {
        return library;
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

    private void initDeck(int library) {
        if (library == 0) {
            deck = new DeckManager(new BaseCardLibrary());
        } else {
            deck = new DeckManager(new SenseiPathCardLibrary());
        }
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
        int fx = move.fromX;
        int fy = move.fromY;
        int tx = move.toX;
        int ty = move.toY;

        Piece piece = board[fy][fx];
        if (piece == null) return false;

        // Ha van ellenfél bábu → leütjük
        Piece target = board[ty][tx];
        if (target != null && target.getOwner() != piece.getOwner()) {
            // ❗ Master leütve? → azonnali győzelem
            if (target.isMaster()) {
                winner = piece.getOwner();
                // opcionálisan még átrakhatod a bábut:
                board[ty][tx] = piece;
                board[fy][fx] = null;
                return true;  // nem váltunk játékost, vége a játéknak
            }

            board[ty][tx] = null; // sima bábu leütve
        }

        // Mozgatás
        board[ty][tx] = piece;
        board[fy][fx] = null;

        // Temple Victory (master a másik templomán)
        if (piece.isMaster()) {
            if (piece.getOwner() == 1 && tx == 2 && ty == 0) {
                winner = 1;
                return true;
            }
            if (piece.getOwner() == 2 && tx == 2 && ty == 4) {
                winner = 2;
                return true;
            }
        }

        // KÁRTYACSERE
        Card used = move.card;

        if (piece.getOwner() == 1) {
            if (p1Card1 == used) p1Card1 = centerCard;
            else p1Card2 = centerCard;
        } else {
            if (p2Card1 == used) p2Card1 = centerCard;
            else p2Card2 = centerCard;
        }

        centerCard = used;

        // Játékosváltás
        switchPlayer();
        return true;
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == player1 ? player2 : player1);
    }


    // =======================
// SETTEREK SAVE/LOAD-hoz
// =======================

    public void setBoard(Piece[][] newBoard) {
        this.board = newBoard;
    }

    public void setCurrentPlayer(Player p) {
        this.currentPlayer = p;
    }

    public void setP1Cards(Card c1, Card c2) {
        this.p1Card1 = c1;
        this.p1Card2 = c2;
    }

    public void setP2Cards(Card c1, Card c2) {
        this.p2Card1 = c1;
        this.p2Card2 = c2;
    }

    public void setCenterCard(Card c) {
        this.centerCard = c;
    }

    public void setGameMode(GameMode mode) {
        this.mode = mode;
    }


}
