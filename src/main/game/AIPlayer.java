package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Gépi játékos mely előre megírt szabályok alapján válaasztja ki a lépését
 */
public class AIPlayer implements Player {

    private final int id;
    private final Random rnd = new Random();

    /**
     * Konstruktor
     * @param id
     */
    public AIPlayer(int id) {
        this.id = id;
    }

    /**
     * @return - az id-t adja vissza
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     *
     * @return Ai esetén true amugy false
     */
    @Override
    public boolean isAI() {
        return true;
    }

    /**
     * Ez a függvény kiválasztja hogy a gép mit lépjen,
     * megszabott feltételek alapján
     *
     * @param state - aktuális játéktér
     * @return visszaadja a gép által kiválasztott lépést
     */
    @Override
    public Move decideMove(GameState state) {

        Piece[][] board = state.getBoard();

        // Kártyák kiválasztása a játékostól függően
        Card c1 = (id == 1 ? state.getP1Card1() : state.getP2Card1());
        Card c2 = (id == 1 ? state.getP1Card2() : state.getP2Card2());

        List<Move> allMoves = new ArrayList<>();

        // LÉPÉSEL BEGYŰJTÉSE
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                Piece p = board[y][x];
                if (p == null || p.getOwner() != id) continue;

                allMoves.addAll(generateMovesForPiece(x, y, p, c1, state));
                allMoves.addAll(generateMovesForPiece(x, y, p, c2, state));
            }
        }

        if (allMoves.isEmpty()) return null;

        // Kiválasztja a legjobb lépést: elöször minden lépést pontoz
        Move bestMove = null;
        int bestScore = Integer.MIN_VALUE;

        for (Move m : allMoves) {
            int score = evaluateMove(m, state);

            if (score > bestScore) {
                bestScore = score;
                bestMove = m;
            }
        }

        return bestMove;
    }


    /**
     * Legenerálja egy adott bábu összes lehetséges lépését
     * a kapott kártya alapján.
     *
     * A metódus figyelembe veszi:
     * - a kártya mozgásait,
     * - a játékos irányát,
     * - a tábla határait,
     * - hogy saját bábut nem léphet le,
     * - hogy az ellenfél bábu üthető.
     *
     * @param x        a bábu jelenlegi x koordinátája
     * @param y        a bábu jelenlegi y koordinátája
     * @param piece    maga a bábu
     * @param card     a mozgást meghatározó kártya
     * @param state    a játék aktuális állapota
     * @return a bábuval tehető lépések listája
     */
    private List<Move> generateMovesForPiece(int x, int y, Piece piece, Card card, GameState state) {

        List<Move> result = new ArrayList<>();

        for (int[] mv : card.getMoves()) {

            int dx = mv[0];
            int dy = mv[1];

            // A opció: UI szerinti rossz orientáció
            if (piece.getOwner() == 1) dy = -dy;
            else dx = -dx;

            int tx = x + dx;
            int ty = y + dy;

            if (tx < 0 || tx > 4 || ty < 0 || ty > 4) continue;

            Piece target = state.getBoard()[ty][tx];

            if (target != null && target.getOwner() == piece.getOwner()) continue;

            result.add(new Move(x, y, tx, ty, card));
        }

        return result;
    }

    /**
     * Pontozza az adott lépést és visszaadja annak értékét.
     *
     * A pontozás figyelembe veszi többek között:
     * - templom győzelem lehetőségét,
     * - ellenfél master leütését,
     * - a saját master fenyegetettségét,
     * - bábu biztonságban marad-e,
     * - pozíció előnyöket,
     * - célpont ütését,
     * - az ellenfél master felé közeledést,
     * - véletlen kis pont hozzáadását a kevésbé kiszámítható AI érdekében.
     *
     * @param m      a vizsgált lépés
     * @param state  a játék aktuális állapota
     * @return a lépés pontszáma, minél nagyobb, annál jobb
     */
    private int evaluateMove(Move m, GameState state) {

        Piece[][] board = state.getBoard();
        Piece p = board[m.fromY][m.fromX];
        Piece target = board[m.toY][m.toX];

        int score = 0;

        // Templom győzelem
        if (wouldTempleWin(m, state)) return 2000;

        // Master ütés
        if (target != null && target.isMaster() && target.getOwner() != id) {
            return 2000;
        }

        // Master védelme
        if (isMasterThreatened(state)) {

            // LEHET SIMA BÁBUVAL LEÜTNI A TÁMADÓT
            for (int[] pos : getThreateningPositions(state)) {

                // Ez a lépés a támadó mezőre megy?
                if (m.toX == pos[0] && m.toY == pos[1]) {

                    // Ha MASTER lép még nem térünk vissza, lehet veszélyes
                    if (!p.isMaster()) {
                        return 700; // SIMA bábuval ütni a legjobb!
                    }
                }
            }


            // MASTER ÜTI A TÁMADÓT — csak akkor, ha lépés UTÁN biztonságos
            for (int[] pos : getThreateningPositions(state)) {
                if (m.toX == pos[0] && m.toY == pos[1]) {

                    if (p.isMaster()) {
                        GameState next = cloneAndSimulateMove(state, m);

                        if (!isMasterThreatened(next)) {
                            return 600;  // jó lépés, masterrel biztonságosan üti
                        } else {
                            return -300; // öngyilkos, kerülni kell
                        }
                    }
                }
            }


            // MASTER MENEKÜL — UTÁNA biztonságos legyen
            if (p.isMaster()) {
                GameState next = cloneAndSimulateMove(state, m);

                if (!isMasterThreatened(next)) {
                    return 500; // menekülés működik
                }
            }

            // Egyéb lépés, ami nem segít → rossz
            return -500;
        }


        // Sima ütés
        if (target != null && target.getOwner() != id) score += 50;

        // Kerülje saját halálát
        if (!keepsPieceSafe(m, state)) score -= 999;

        // Mastert óvni kell
        if (p.isMaster()) score -= 2;   // master mozgatása veszélyesebb

        // Pozíció javítás (középpont felé)
        score += centerScore(m.toX, m.toY);

        // Közeledés az ellenfél masterhez
        score += approachEnemyMaster(m, state);

        // Random kis pont hogy ne legyen mindig teljesen kiszámítható
        score += rnd.nextInt(5);

        return score;
    }

    /**
     * A tábla középpontjához való közelség alapján ad pontot.
     * Minél közelebb kerül a bábu a tábla középpontjához (2,2),
     * annál magasabb pontszámot kap.
     *
     * @param x a célmező x koordinátája
     * @param y a célmező y koordinátája
     * @return a középponthoz való közelség pontszáma
     */
    private int centerScore(int x, int y) {
        int cx = 2, cy = 2;
        int dx = Math.abs(cx - x);
        int dy = Math.abs(cy - y);
        return 10 - (dx + dy); // minél közelebb a középhez, annál jobb
    }

    /**
     * A lépést pontozza annak alapján, hogy a bábu közeledik-e
     * az ellenfél master figurájához.
     *
     * @param m      a vizsgált lépés
     * @param state  a játék aktuális állapota
     * @return pontszám, amely nagyobb, ha a lépés közelebb visz az ellenfél masterhez
     */
    private int approachEnemyMaster(Move m, GameState state) {
        int enemy = (id == 1 ? 2 : 1);

        // ellenfél master keresése
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                Piece ep = state.getBoard()[y][x];
                if (ep != null && ep.isMaster() && ep.getOwner() == enemy) {
                    int dx = Math.abs(x - m.toX);
                    int dy = Math.abs(y - m.toY);
                    return 15 - (dx + dy);
                }
            }
        }
        return 0;
    }


    /**
     * Megvizsgálja, hogy a lépés után a bábu biztonságban marad-e.
     *
     * A metódus megnézi:
     * - az ellenfél összes kártyáját,
     * - az ellenfél összes potenciális lépését,
     * - hogy bármelyik lépés az adott mezőre tud-e érkezni.
     *
     * @param m      a lépés
     * @param state  a játék aktuális állapota
     * @return igaz, ha a lépés után a bábu nem üthető le azonnal
     */
    private boolean keepsPieceSafe(Move m, GameState state) {

        Piece[][] board = state.getBoard();
        Piece piece = board[m.fromY][m.fromX];

        int enemy = (piece.getOwner() == 1 ? 2 : 1);
        Card ec1 = (enemy == 1 ? state.getP1Card1() : state.getP2Card1());
        Card ec2 = (enemy == 1 ? state.getP1Card2() : state.getP2Card2());

        Card[] enemyCards = {ec1, ec2};

        for (Card c : enemyCards) {
            for (int[] mv : c.getMoves()) {

                int dx = mv[0];
                int dy = mv[1];

                if (enemy == 1) dy = -dy;
                else dx = -dx;

                int ex = m.toX - dx;
                int ey = m.toY - dy;

                if (ex < 0 || ex > 4 || ey < 0 || ey > 4) continue;

                Piece ep = board[ey][ex];

                if (ep != null && ep.getOwner() == enemy) return false;
            }
        }

        return true;
    }

    /**
     * Eldönti, hogy a lépés templom győzelmet eredményez-e.
     * A templom győzelem akkor következik be, amikor a master
     * eljut az ellenfél templommezőjére.
     *
     * @param m      a lépés
     * @param state  a játék aktuális állapota
     * @return igaz, ha a lépés templom győzelmet hoz
     */
    private boolean wouldTempleWin(Move m, GameState state) {

        Piece[][] board = state.getBoard();
        Piece p = board[m.fromY][m.fromX];

        if (!p.isMaster()) return false;

        if (p.getOwner() == 1 && m.toX == 2 && m.toY == 0) return true;
        if (p.getOwner() == 2 && m.toX == 2 && m.toY == 4) return true;

        return false;
    }


    /**
     * Megvizsgálja, hogy az Gép saját master figurája
     * éppen fenyegetve van-e az ellenfél kártyái alapján.
     *
     * Megkeresi a master pozícióját, majd megnézi,
     * hogy bármelyik ellenfél bábu képes-e eljutni oda
     * a következő lépésben.
     *
     * @param state a játék aktuális állapota
     * @return igaz, ha a master fenyegetve van
     */
    private boolean isMasterThreatened(GameState state) {

        Piece[][] board = state.getBoard();

        // Master pozíciója megkeresése
        int mx = -1, my = -1;
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                Piece p = board[y][x];
                if (p != null && p.isMaster() && p.getOwner() == id) {
                    mx = x;
                    my = y;
                }
            }
        }

        if (mx == -1) return false;

        int enemy = (id == 1 ? 2 : 1);
        Card ec1 = (enemy == 1 ? state.getP1Card1() : state.getP2Card1());
        Card ec2 = (enemy == 1 ? state.getP1Card2() : state.getP2Card2());

        for (Card c : new Card[]{ec1, ec2}) {
            for (int[] mv : c.getMoves()) {

                int dx = mv[0];
                int dy = mv[1];

                // ellenfél irányát normalizáljuk
                if (enemy == 1) dy = -dy;
                else dx = -dx;

                int ex = mx - dx;
                int ey = my - dy;

                if (ex < 0 || ex > 4 || ey < 0 || ey > 4) continue;

                Piece attacker = board[ey][ex];

                if (attacker != null && attacker.getOwner() == enemy) {
                    return true; // TALÁLT FENYEGETÉS
                }
            }
        }

        return false;
    }

    /**
     * Visszaadja azon mezők listáját, ahonnan az ellenfél
     * a következő lépésben le tudja ütni az Gép masterét.
     *
     * @param state a játék aktuális állapota
     * @return lista minden olyan mező koordinátájával, ahol a fenyegető ellenfél bábu áll
     */
    private List<int[]> getThreateningPositions(GameState state) {

        List<int[]> threats = new ArrayList<>();
        Piece[][] board = state.getBoard();

        // Master pozíció
        int mx = -1, my = -1;
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                Piece p = board[y][x];
                if (p != null && p.isMaster() && p.getOwner() == id) {
                    mx = x;
                    my = y;
                }
            }
        }

        int enemy = (id == 1 ? 2 : 1);
        Card ec1 = (enemy == 1 ? state.getP1Card1() : state.getP2Card1());
        Card ec2 = (enemy == 1 ? state.getP1Card2() : state.getP2Card2());

        for (Card c : new Card[]{ec1, ec2}) {
            for (int[] mv : c.getMoves()) {

                int dx = mv[0];
                int dy = mv[1];

                if (enemy == 1) dy = -dy;
                else dx = -dx;

                int ex = mx - dx;
                int ey = my - dy;

                if (ex < 0 || ex > 4 || ey < 0 || ey > 4) continue;

                Piece attacker = board[ey][ex];
                if (attacker != null && attacker.getOwner() == enemy) {
                    threats.add(new int[]{ex, ey});
                }
            }
        }
        return threats;
    }


    /**
     * Létrehoz egy másolatot a játékállapotról, majd azon
     * végrehajtja a megadott lépést.
     *
     * Ezt a Gép arra használja, hogy megvizsgálja:
     * - egy lépés után fenyegetésbe kerül-e a master,
     * - egy lépés biztonságos-e vagy öngyilkos jellegű,
     * - milyen a tábla jövőbeni állapota a lépés után.
     *
     * A másolat tartalmazza:
     * - a táblát,
     * - a bábukat,
     * - a kártyákat,
     * - az aktuális játékos beállításait.
     *
     * @param state az eredeti játékállapot
     * @param move  a szimulálandó lépés
     * @return a lépés végrehajtása utáni új játékállapot
     */
    private GameState cloneAndSimulateMove(GameState state, Move move) {

        // Új GameState objektum létrehozása, ugyanazzal a mode-dal
        GameState copy = new GameState(
                state.getPlayer1(),
                state.getPlayer2(),
                state.getMode(),
                state.getLibrary()
        );

        // Tábla másolása
        Piece[][] newBoard = new Piece[5][5];
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                Piece p = state.getBoard()[y][x];
                if (p != null) newBoard[y][x] = new Piece(p.getOwner(), p.isMaster());
            }
        }
        copy.setBoard(newBoard);

        // Kártyák másolása
        copy.setP1Cards(state.getP1Card1(), state.getP1Card2());
        copy.setP2Cards(state.getP2Card1(), state.getP2Card2());
        copy.setCenterCard(state.getCenterCard());

        // Current player másolása
        copy.setCurrentPlayer(state.getCurrentPlayer());

        // Lépés szimulálása
        copy.makeMove(new Move(
                move.fromX, move.fromY,
                move.toX, move.toY,
                move.card
        ));

        return copy;
    }
}