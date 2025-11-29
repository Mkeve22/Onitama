package game;

import java.util.Arrays;

/**
 * A játék teljes állapotát kezelő osztály.
 *
 * Feladata:
 * - a két játékos tárolása,
 * - a jelenlegi játékos nyomon követése,
 * - a 5×5-ös tábla kezelése,
 * - a kártyák kiosztása és cseréje,
 * - a lépések végrehajtása és a győzelem ellenőrzése,
 * - a kezdő kártyalibrary kiválasztása.
 */
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


    /**
     * Létrehozza a játékállapotot két játékossal, beállítja a játékmódot,
     * létrehozza az alap táblaállást és előkészíti a kártyapaklit.
     *
     * @param p1      az első játékos
     * @param p2      a második játékos
     * @param mode    a játék módja
     * @param library annak jelölése, melyik kártyagyűjteményt használjuk
     */
    public GameState(Player p1, Player p2, GameMode mode, int library) {
        this.player1 = p1;
        this.player2 = p2;
        this.currentPlayer = player1;
        this.mode = mode;
        this.library = library;

        initBoard();
        initDeck(library);
    }

    /**
     * @return a játék módja
     */
    public GameMode getMode() { return mode; }

    /**
     * @return a győztes játékos azonosítója, vagy null, ha még nincs győztes
     */
    public Integer getWinner() {
        return winner;
    }

    /**
     * @return igaz, ha a játék már véget ért
     */
    public boolean isGameOver() {
        return winner != null;
    }

    /**
     * @return az első játékos
     */
    public Player getPlayer1() {
        return player1;
    }

    /**
     * @return a második játékos
     */
    public Player getPlayer2() {
        return player2;
    }

    /**
     * @return a kiválasztott kártyalibrary azonosítója
     */
    public int getLibrary() {
        return library;
    }


    /**
     * Létrehozza a játék kezdőtábláját.
     *
     * A két játékos bábuit az Onitama szabályai szerint helyezi el:
     * - az 1-es játékos bábui az alsó sorban,
     * - a 2-es játékos bábui a felső sorban,
     * mindkét játékos középső figurája a master.
     */
    private void initBoard() {

        for (Piece[] row : board) {
            Arrays.fill(row, null);
        }

        // Player 1 (Alllul)
        board[4][0] = new Piece(1, false);
        board[4][1] = new Piece(1, false);
        board[4][2] = new Piece(1, true);  // Master
        board[4][3] = new Piece(1, false);
        board[4][4] = new Piece(1, false);

        // Player 2 (Fellül)
        board[0][0] = new Piece(2, false);
        board[0][1] = new Piece(2, false);
        board[0][2] = new Piece(2, true);  // Master
        board[0][3] = new Piece(2, false);
        board[0][4] = new Piece(2, false);
    }

    /**
     * Kiválasztja a megfelelő kártyagyűjteményt, létrehozza és megkeveri a paklit,
     * majd kiosztja a kezdőkártyákat mindkét játékosnak és a középre.
     *
     * @param library 0 esetén az alap kártyakészletet használja,
     *                más érték esetén a Sensei's Path kiegészítőt.
     */
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

    /**
     * @return a játéktábla 5×5-ös mátrixa
     */
    public Piece[][] getBoard() {
        return board;
    }

    /**
     * @return a soron következő játékos
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * @return az első játékos első kártyája
     */
    public Card getP1Card1() {
        return p1Card1;
    }

    /**
     * @return az első játékos második kártyája
     */
    public Card getP1Card2() {
        return p1Card2;
    }

    /**
     * @return a második játékos első kártyája
     */
    public Card getP2Card1() {
        return p2Card1;
    }

    /**
     * @return a második játékos második kártyája
     */
    public Card getP2Card2() {
        return p2Card2;
    }

    /**
     * @return a középső kártya
     */
    public Card getCenterCard() {
        return centerCard;
    }

    /**
     * Végrehajt egy lépést a táblán:
     * - megmozgatja a bábut,
     * - ha ellenfél bábu van a célmezőn, leüti,
     * - ellenőrzi a győzelmi feltételeket (master leütése vagy templom győzelem),
     * - elvégzi a kártyacserét,
     * - játékost vált, ha nincs győztes.
     *
     * @param move a végrehajtandó lépés (kezdő mező, célmező és használt kártya)
     * @return igaz, ha a lépés sikeresen lefutott
     */
    public boolean makeMove(Move move) {
        int fx = move.fromX;
        int fy = move.fromY;
        int tx = move.toX;
        int ty = move.toY;

        Piece piece = board[fy][fx];
        if (piece == null) return false;

        // Ha van ellenfél bábu leütjük
        Piece target = board[ty][tx];
        if (target != null && target.getOwner() != piece.getOwner()) {
            // Master leütve - azonnali győzelem
            if (target.isMaster()) {
                winner = piece.getOwner();
                board[ty][tx] = piece;
                board[fy][fx] = null;
                return true;  // nem váltunk játékost, vége a játékn
            }

            board[ty][tx] = null; // sima bábu leütve
        }

        // Mozgatás
        board[ty][tx] = piece;
        board[fy][fx] = null;

        // Master a másik templomán
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

    /**
     * Átkapcsol a következő játékosra.
     * Ha a jelenlegi játékos az 1-es, akkor a 2-es következik, és fordítva.
     */
    private void switchPlayer() {
        currentPlayer = (currentPlayer == player1 ? player2 : player1);
    }



    /**
     * A tábla állapotának beállítása. Általában AI vagy mentés visszatöltés
     * során használatos.
     *
     * @param newBoard az új tábla (5×5 darab Piece objektum)
     */
    public void setBoard(Piece[][] newBoard) {
        this.board = newBoard;
    }

    /**
     * Beállítja, hogy melyik játékos van soron.
     *
     * @param p a következő játékos
     */
    public void setCurrentPlayer(Player p) {
        this.currentPlayer = p;
    }

    /**
     * Beállítja az első játékos két kártyáját.
     *
     * @param c1 az első kártya
     * @param c2 a második kártya
     */
    public void setP1Cards(Card c1, Card c2) {
        this.p1Card1 = c1;
        this.p1Card2 = c2;
    }

    /**
     * Beállítja a második játékos két kártyáját.
     *
     * @param c1 az első kártya
     * @param c2 a második kártya
     */
    public void setP2Cards(Card c1, Card c2) {
        this.p2Card1 = c1;
        this.p2Card2 = c2;
    }

    /**
     * Beállítja a középső kártyát.
     *
     * @param c a középső kártya
     */
    public void setCenterCard(Card c) {
        this.centerCard = c;
    }

    /**
     * Beállítja az aktuális játékmódot.
     *
     * @param mode az új játékmód
     */
    public void setGameMode(GameMode mode) {
        this.mode = mode;
    }
}
