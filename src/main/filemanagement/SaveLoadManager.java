package filemanagement;

import game.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A játékállás mentéséért és betöltéséért felelős osztály.
 */
public class SaveLoadManager {

    // Mentés mappája
    private static final String SAVE_FOLDER = "Save";

    // Első mentési slot
    private static final Path SLOT1 = Path.of(SAVE_FOLDER, "save1.json");

    // Második mentési slot
    private static final Path SLOT2 = Path.of(SAVE_FOLDER, "save2.json");




    /**
     * Elmenti a játékállapotot a megadott slotba.
     *
     * @param state a mentendő állapot
     * @param slot  1 vagy 2
     */
    public static void save(GameState state, int slot) throws IOException {

        // Mappa létrehozása
        Files.createDirectories(Path.of(SAVE_FOLDER));

        // Slot kiválasztása
        Path path = (slot == 1 ? SLOT1 : SLOT2);

        // JSON építés
        StringBuilder json = new StringBuilder();
        json.append("{\n");

        // Aktuális játékos
        json.append("\"currentPlayerId\":").append(state.getCurrentPlayer().getId()).append(",\n");

        // Board kezdete
        json.append("\"board\":[");
        Piece[][] b = state.getBoard();

        // Board sorok
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

        // Kártyák mentése
        json.append("\"p1c1\":\"").append(state.getP1Card1().getName()).append("\",\n");
        json.append("\"p1c2\":\"").append(state.getP1Card2().getName()).append("\",\n");
        json.append("\"p2c1\":\"").append(state.getP2Card1().getName()).append("\",\n");
        json.append("\"p2c2\":\"").append(state.getP2Card2().getName()).append("\",\n");
        json.append("\"centerCard\":\"").append(state.getCenterCard().getName()).append("\",\n");

        // Játékmód
        json.append("\"gamemod\":\"").append(state.getMode().name()).append("\",\n");

        // Kártya csomag mentése
        json.append("\"library\":").append(state.getLibrary()).append("\n");


        json.append("}");

        // ÍFájlba írás
        Files.writeString(path, json.toString());
    }



    /**
     * Betölti a játékot a megadott slotból.
     *
     * @param slot 1 vagy 2
     * @return a betöltött GameState
     */
    public static GameState load(int slot) throws IOException {

        // Slot kiválasztása
        Path path = (slot == 1 ? SLOT1 : SLOT2);

        // Létezésének ellenörzése
        if (!Files.exists(path)) {
            throw new IOException("Slot " + slot + " üres.");
        }

        // Fájl beolvasás
        String json = Files.readString(path).trim();


        // JSON érték-kinyerő lambda
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


        // ALAP MEZŐK
        int currentPlayerId = Integer.parseInt(get.apply("currentPlayerId"));
        String p1c1 = get.apply("p1c1");
        String p1c2 = get.apply("p1c2");
        String p2c1 = get.apply("p2c1");
        String p2c2 = get.apply("p2c2");
        String center = get.apply("centerCard");
        String mod = get.apply("gamemod");
        GameMode mode = GameMode.valueOf(mod);



        Piece[][] board = new Piece[5][5];

        // Board keresése
        int boardStart = json.indexOf("\"board\":") + 8;

        // Első '[' keresése
        int arrStart = json.indexOf("[", boardStart);

        // Board tömb vége
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

        // Board substring
        String boardStr = json.substring(arrStart, arrEnd + 1);

        // Sorok szétvágása
        String[] rows = boardStr.substring(1, boardStr.length() - 1).split("],");

        // Soronkénti parse
        for (int y = 0; y < 5; y++) {

            String row = rows[y].replace("[", "").replace("]", "");
            String[] cells = row.split(",");

            int index = 0;

            for (int x = 0; x < 5; x++) {

                String c = cells[index].trim();

                // JSON objektum összefűzése ha több részre tört
                if (c.startsWith("{")) {
                    while (!c.contains("}")) {
                        index++;
                        c += "," + cells[index].trim();
                    }
                }

                // Null mező
                if (!c.equals("null")) {

                    // Owner kinyerése
                    int owner = Integer.parseInt(
                            c.substring(c.indexOf("player") + 8, c.indexOf(",", c.indexOf("player")))
                    );

                    // Master flag
                    boolean master = Boolean.parseBoolean(
                            c.substring(c.indexOf("master") + 8, c.indexOf("}", c.indexOf("master")))
                    );
                    board[y][x] = new Piece(owner, master);
                }

                index++;
            }
        }



        //  JÁTÉKOSOK LÉTREHOZÁSA
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


        //  GAMESTATE FELÉPÍTÉSE
        int library = Integer.parseInt(get.apply("library"));
        GameState state = new GameState(p1, p2, mode, library);

        // Board beállítása
        state.setBoard(board);

        // Jelenlegi játékos
        state.setCurrentPlayer(currentPlayerId == 1 ? p1 : p2);

        // Kártyák beállítása
        state.setP1Cards(CardLook.getbyName(p1c1), CardLook.getbyName(p1c2));
        state.setP2Cards(CardLook.getbyName(p2c1), CardLook.getbyName(p2c2));
        state.setCenterCard(CardLook.getbyName(center));

        // Mód beállítása
        state.setGameMode(mode);

        return state;
    }




    /**
     * @return igaz, ha a slot fájl létezik
     */
    public static boolean slotExists(int slot) {
        return Files.exists(slot == 1 ? SLOT1 : SLOT2);
    }
}