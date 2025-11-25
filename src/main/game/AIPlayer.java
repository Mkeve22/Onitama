package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class AIPlayer implements Player {

    private final int id;
    private final Random rnd = new Random();

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

        Piece[][] board = state.getBoard();

        // Kártyák kiválasztása a játékostól függően
        Card c1 = (id == 1 ? state.getP1Card1() : state.getP2Card1());
        Card c2 = (id == 1 ? state.getP1Card2() : state.getP2Card2());

        List<Move> allMoves = new ArrayList<>();

        // BÁBUK BEGYŰJTÉSE
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                Piece p = board[y][x];
                if (p == null || p.getOwner() != id) continue;

                allMoves.addAll(generateMovesForPiece(x, y, p, c1, state));
                allMoves.addAll(generateMovesForPiece(x, y, p, c2, state));
            }
        }

        if (allMoves.isEmpty()) return null;

        // ➤ OKOSÍTOTT AI: minden lépést pontozunk
        Move bestMove = null;
        int bestScore = -1000;

        for (Move m : allMoves) {
            int score = evaluateMove(m, state);

            if (score > bestScore) {
                bestScore = score;
                bestMove = m;
            }
        }

        return bestMove;
    }


    // ------ MOVE GENERATION --------------------------------------------------

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

    // ------ SCORING / EVALUATION --------------------------------------------

    private int evaluateMove(Move m, GameState state) {

        Piece[][] board = state.getBoard();
        Piece p = board[m.fromY][m.fromX];
        Piece target = board[m.toY][m.toX];

        int score = 0;

        // 1️⃣ Templom győzelem
        if (wouldTempleWin(m, state)) return 1000;

        // 2️⃣ Master ütés
        if (target != null && target.isMaster() && target.getOwner() != id) {
            return 200;
        }

        // 3️⃣ Sima ütés
        if (target != null && target.getOwner() != id) score += 50;

        // 4️⃣ Kerülje saját halálát
        if (!keepsPieceSafe(m, state)) score -= 999;

        // 5️⃣ Mastert óvni kell
        if (p.isMaster()) score -= 5;   // master mozgatása veszélyesebb

        // 6️⃣ Pozíció javítás (középpont felé)
        score += centerScore(m.toX, m.toY);

        // 7️⃣ Közeledés az ellenfél masterhez
        score += approachEnemyMaster(m, state);

        // 8️⃣ Random kis zaj → hogy ne legyen mindig teljesen kiszámítható
        score += rnd.nextInt(5);

        return score;
    }

    // Középpont preferálása
    private int centerScore(int x, int y) {
        int cx = 2, cy = 2;
        int dx = Math.abs(cx - x);
        int dy = Math.abs(cy - y);
        return 10 - (dx + dy); // minél közelebb a középhez, annál jobb
    }

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

    // ------ SAFETY CHECK -----------------------------------------------------

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

    private boolean wouldTempleWin(Move m, GameState state) {

        Piece[][] board = state.getBoard();
        Piece p = board[m.fromY][m.fromX];

        if (!p.isMaster()) return false;

        if (p.getOwner() == 1 && m.toX == 2 && m.toY == 0) return true;
        if (p.getOwner() == 2 && m.toX == 2 && m.toY == 4) return true;

        return false;
    }
}
