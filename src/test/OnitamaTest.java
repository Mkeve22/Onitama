import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import game.*;

/**
 * Ebben az osztályban teszteljük az Onitama játék osztályainak és függvényeinek működését,
 * Cél:
 *  - Legalább 3 osztály összesen 10 metódusának tesztelése.
 */
public class OnitamaTest {

    private GameState state;
    private HumanPlayer p1;
    private HumanPlayer p2;

    private Card c1;
    private Card c2;
    private Card c3;
    private Card c4;
    private Card c5;

    /**
     * A tesztek során felhasznált adatokat hozza létre
     */
    @BeforeEach
    void setUp() {
        p1 = new HumanPlayer(1);
        p2 = new HumanPlayer(2);

        // Normál GameState (decket létrehozza, de mi felülírjuk)
        state = new GameState(p1, p2, GameMode.PLAYER_VS_PLAYER, 0);

        // manuális fix kártyák
        c1 = new Card("Tiger", "/Base/Tiger.png", new int[][]{{0,1}});
        c2 = new Card("Dragon", "/Base/Dragon.png", new int[][]{{1,1}});
        c3 = new Card("Crane", "/Base/Crane.png", new int[][]{{0,-1}});
        c4 = new Card("Monkey", "/Base/Monkey.png", new int[][]{{-1,1}});
        c5 = new Card("Frog", "/Base/Frog.png", new int[][]{{2,0}});

        state.setP1Cards(c1, c2);
        state.setP2Cards(c3, c4);
        state.setCenterCard(c5);
    }

    /**
     * 1. Tábla inicializálása helyes
     */
    @Test
    void testInitialBoard() {
        Piece[][] b = state.getBoard();

        // Player 1 lent
        assertEquals(1, b[4][0].getOwner());
        assertEquals(1, b[4][2].getOwner());
        assertTrue(b[4][2].isMaster());

        // Player 2 fent
        assertEquals(2, b[0][0].getOwner());
        assertEquals(2, b[0][2].getOwner());
        assertTrue(b[0][2].isMaster());
    }

    /**
     * 2. Egyszerű lépés működik
     */
    @Test
    void testSimpleMove() {
        Move m = new Move(2, 4, 2, 3, c1);
        state.makeMove(m);

        Piece[][] b = state.getBoard();

        assertNull(b[4][2]);          // régi hely üres
        assertNotNull(b[3][2]);       // új helyen bábu
        assertEquals(1, b[3][2].getOwner());
    }

    /**
     * 3. Játékosváltás lépés után
     */
    @Test
    void testPlayerSwitch() {
        Player before = state.getCurrentPlayer();

        Move m = new Move(2, 4, 2, 3, c1);
        state.makeMove(m);

        assertNotEquals(before, state.getCurrentPlayer());
    }

    /**
     * 4. Leütés működik
     */
    @Test
    void testCapture() {
        // Tegyünk ellenséges bábut elé
        state.getBoard()[3][2] = new Piece(2, false);

        Move m = new Move(2, 4, 2, 3, c1);
        state.makeMove(m);

        Piece[][] b = state.getBoard();

        assertEquals(1, b[3][2].getOwner()); // p1 bábuja került oda
    }

    /**
     * 5. Master leütése azonnali győzelem
     */
    @Test
    void testCaptureMasterWins() {
        state.getBoard()[3][2] = new Piece(2, true); // Master

        Move m = new Move(2, 4, 2, 3, c1);
        state.makeMove(m);

        assertEquals(1, state.getWinner());
        assertTrue(state.isGameOver());
    }

    /**
     * 6. Templom győzelem működik
     */
    @Test
    void testTempleWin() {
        Piece master = state.getBoard()[4][2];
        state.getBoard()[4][2] = null;
        state.getBoard()[1][2] = master;

        Move m = new Move(2, 1, 2, 0, c1); // be a templomba
        state.makeMove(m);

        assertEquals(1, state.getWinner());
    }

    /**
     * 7. Kártyacsere működik
     */
    @Test
    void testCardSwap() {
        Move m = new Move(2, 4, 2, 3, c1);
        state.makeMove(m);

        assertEquals(c5, state.getP1Card1());
        assertEquals(c1, state.getCenterCard());
    }

    /**
     * 8. setBoard() működik
     */
    @Test
    void testSetBoard() {
        Piece[][] newBoard = new Piece[5][5];
        newBoard[2][2] = new Piece(1, true);

        state.setBoard(newBoard);

        assertEquals(newBoard, state.getBoard());
    }

    /**
     * 9. setCurrentPlayer() működik
     */
    @Test
    void testSetCurrentPlayer() {
        state.setCurrentPlayer(p2);
        assertEquals(p2, state.getCurrentPlayer());
    }

    /**
     * 10. setGameMode() működik
     */
    @Test
    void testSetGameMode() {
        state.setGameMode(GameMode.AI_VS_AI);
        assertEquals(GameMode.AI_VS_AI, state.getMode());
    }


    /**
     * 11. Card getterek működnek
     */
    @Test
    void testCardGetters() {
        int[][] moves = {{1,1}, {-1,0}};
        Card c = new Card("TestCard", "/img/test.png", moves);

        assertEquals("TestCard", c.getName());
        assertEquals("/img/test.png", c.getImagepath());
        assertArrayEquals(moves, c.getMoves());
    }


    /**
     * 12. HumanPlayer getId működik
     */
    @Test
    void testHumanPlayerId() {
        HumanPlayer hp = new HumanPlayer(5);
        assertEquals(5, hp.getId());
    }

    /**
     * 13. HumanPlayer nem AI
     */
    @Test
    void testHumanPlayerIsNotAI() {
        HumanPlayer hp = new HumanPlayer(2);
        assertFalse(hp.isAI());
    }

    /**
     * 14. HumanPlayer decideMove mindig null
     */
    @Test
    void testHumanPlayerDecideMoveReturnsNull() {
        HumanPlayer hp = new HumanPlayer(1);
        assertNull(hp.decideMove(state));
    }
}