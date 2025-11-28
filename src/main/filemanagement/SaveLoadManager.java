package filemanagement;

import game.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SaveLoadManager {

    private static final String SAVE_FOLDER = "Save";
    private static final Path SLOT1 = Path.of(SAVE_FOLDER, "save1.json");
    private static final Path SLOT2 = Path.of(SAVE_FOLDER, "save2.json");



    // =======================
    //         SAVE
    // =======================
    public static void save(GameState state, int slot) throws IOException {

        Files.createDirectories(Path.of(SAVE_FOLDER));

        Path path = (slot == 1 ? SLOT1 : SLOT2);

        StringBuilder json = new StringBuilder();
        json.append("{\n");

        json.append("\"currentPlayerId\":").append(state.getCurrentPlayer().getId()).append(",\n");

        // --- BOARD ---
        json.append("\"board\":[");
        Piece[][] b = state.getBoard();

        for (int y = 0; y < 5; y++) {
            json.append("[");
            for (int x = 0; x < 5; x++) {
                Piece p = b[y][x];
                if (p == null) {
                    json.append("null");
                } else {
                    json.append("{\"player\":").append(p.getOwner())
                            .append(",\"master\":").append(p.isMaster()).append("}");
                }
                if (x < 4) json.append(",");
            }
            json.append("]");
            if (y < 4) json.append(",");
        }

        json.append("],\n");

        // --- Kártyák ---
        json.append("\"p1c1\":\"").append(state.getP1Card1().getName()).append("\",\n");
        json.append("\"p1c2\":\"").append(state.getP1Card2().getName()).append("\",\n");
        json.append("\"p2c1\":\"").append(state.getP2Card1().getName()).append("\",\n");
        json.append("\"p2c2\":\"").append(state.getP2Card2().getName()).append("\",\n");
        json.append("\"centerCard\":\"").append(state.getCenterCard().getName()).append("\",\n");

        // --- Mode ---
        json.append("\"gamemod\":\"").append(state.getMode().name()).append("\",\n");

        // --- Library (0 = Base, 1 = Sensei) ---
        json.append("\"library\":").append(state.getLibrary()).append("\n");


        json.append("}");

        Files.writeString(path, json.toString());
    }



    // =======================
    //         LOAD
    // =======================
    public static GameState load(int slot) throws IOException {

        Path path = (slot == 1 ? SLOT1 : SLOT2);

        if (!Files.exists(path)) {
            throw new IOException("Slot " + slot + " üres.");
        }

        String json = Files.readString(path).trim();


        // ================================
        //     KULCS ÉRTÉK KINYERŐ LAMBDA
        // ================================
        java.util.function.Function<String, String> get = (key) -> {

            String search = "\"" + key + "\":";
            int start = json.indexOf(search) + search.length();

            if (json.charAt(start) == '\"') {
                int end = json.indexOf("\"", start + 1);
                return json.substring(start + 1, end);
            } else {
                int end = json.indexOf(",", start);
                if (end == -1) end = json.indexOf("}", start);
                return json.substring(start, end).trim();
            }
        };


        // ======================
        //   ALAP MEZŐK
        // ======================
        int currentPlayerId = Integer.parseInt(get.apply("currentPlayerId"));
        String p1c1 = get.apply("p1c1");
        String p1c2 = get.apply("p1c2");
        String p2c1 = get.apply("p2c1");
        String p2c2 = get.apply("p2c2");
        String center = get.apply("centerCard");
        String mod = get.apply("gamemod");

        GameMode mode = GameMode.valueOf(mod);



        // ======================
        //     BOARD PARSING
        // ======================

        Piece[][] board = new Piece[5][5];

        int boardStart = json.indexOf("\"board\":") + 8;

        int arrStart = json.indexOf("[", boardStart);

        int depth = 0;
        int arrEnd = -1;

        for (int i = arrStart; i < json.length(); i++) {

            char ch = json.charAt(i);

            if (ch == '[') depth++;
            if (ch == ']') depth--;

            if (depth == 0) {
                arrEnd = i;
                break;
            }
        }

        if (arrEnd == -1)
            throw new RuntimeException("BOARD JSON parse error: no closing ']' found.");

        String boardStr = json.substring(arrStart, arrEnd + 1);

        String[] rows = boardStr.substring(1, boardStr.length() - 1).split("],");

        for (int y = 0; y < 5; y++) {

            String row = rows[y].replace("[", "").replace("]", "");
            String[] cells = row.split(",");

            int index = 0;

            for (int x = 0; x < 5; x++) {

                String c = cells[index].trim();

                if (c.startsWith("{")) {
                    while (!c.contains("}")) {
                        index++;
                        c += "," + cells[index].trim();
                    }
                }

                if (!c.equals("null")) {
                    int owner = Integer.parseInt(
                            c.substring(c.indexOf("player") + 8, c.indexOf(",", c.indexOf("player")))
                    );
                    boolean master = Boolean.parseBoolean(
                            c.substring(c.indexOf("master") + 8, c.indexOf("}", c.indexOf("master")))
                    );
                    board[y][x] = new Piece(owner, master);
                }

                index++;
            }
        }



        // ============================
        //  JÁTÉKOSOK LÉTREHOZÁSA
        // ============================

        Player p1, p2;

        switch (mode) {

            case PLAYER_VS_PLAYER:
                p1 = new HumanPlayer(1);
                p2 = new HumanPlayer(2);
                break;

            case PLAYER_VS_AI:
                p1 = new HumanPlayer(1);
                p2 = new AIPlayer(2);
                break;

            case AI_VS_AI:
                p1 = new AIPlayer(1);
                p2 = new AIPlayer(2);
                break;

            default:
                throw new RuntimeException("Ismeretlen gamemod: " + mode);
        }


        // ============================
        //  GAMESTATE FELÉPÍTÉSE
        // ============================

        int library = Integer.parseInt(get.apply("library"));
        GameState state = new GameState(p1, p2, mode, library);

        state.setBoard(board);
        state.setCurrentPlayer(currentPlayerId == 1 ? p1 : p2);

        state.setP1Cards(CardLook.getbyName(p1c1), CardLook.getbyName(p1c2));
        state.setP2Cards(CardLook.getbyName(p2c1), CardLook.getbyName(p2c2));
        state.setCenterCard(CardLook.getbyName(center));
        state.setGameMode(mode);

        return state;
    }



    // =======================
    //   SLOT létezés ellenőrzés
    // =======================
    public static boolean slotExists(int slot) {
        return Files.exists(slot == 1 ? SLOT1 : SLOT2);
    }
}