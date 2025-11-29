package view;

import game.*;
import util.*;

import javax.swing.*;
import java.awt.*;


/**
 * BoardFrame
 * A Játéktér kinézetéért felel valamint a kártyák kilyelöléséért,
 * mezők kijelöléséért,
 * és a körök egymás utáni vezényléséért amik szorosan függenek a játéktér aktuális állapotától
 * amit a BoardFrame kezel és jelenít meg
 * Két gomb található:
 * - SAVE: elmenti a játékot és ott folytatódik ahol elmentették
 * - SAVE AND EXIT: stintén elmenti a játékot, de kilép a főképernyőre
 */
public class BoardFrame extends JFrame {

    private CardPanel selectedCardPanel = null;
    private TilePanel selectedTile = null;

    private GameState gameState;
    private TilePanel[][] tileGrid = new TilePanel[5][5];

    private CardPanel p2c1Panel;
    private CardPanel p2c2Panel;
    private CardPanel p1c1Panel;
    private CardPanel p1c2Panel;
    private CardPanel centerPanel;

    private Timer aiThinkTimer = null;
    private Timer aiNextTimer = null;


    /**
     * Konstruktor
     *
     * Létrehozza a pálya kinézetét elhelyezi a paneleket és a gombokat a megfelelő helyre,
     * valamint a gombokhoz és panelekhez hozzáadja a funkcióikat
     * @param gameState A játék aktuális állását tartalmazó osztály
     */
    public BoardFrame(GameState gameState) {
        this.gameState = gameState;
        setTitle("Onitama");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(new ImageIcon(getClass().getResource("/icon.png")).getImage());
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        //HÁTTÉRPANEL
        BackgroundPanel backgroundPanel = new BackgroundPanel("/BoardFrame/background.png");
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);

        //FELSŐ 2 KÁRTYAHELY
        JPanel topCards = new JPanel();
        topCards.setOpaque(false);
        topCards.setPreferredSize(new Dimension(0, 250));
        topCards.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 20));
        topCards.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));


        backgroundPanel.add(topCards, BorderLayout.NORTH);
        p2c1Panel = new CardPanel(gameState.getP2Card1(), true);
        p2c1Panel.setRotated(true);
        p2c1Panel.addPropertyChangeListener("selected", evt -> handleCardSelection(p2c1Panel));
        topCards.add(p2c1Panel);

        p2c2Panel = new CardPanel(gameState.getP2Card2(), true);
        p2c2Panel.setRotated(true);
        p2c2Panel.addPropertyChangeListener("selected", evt -> handleCardSelection(p2c2Panel));
        topCards.add(p2c2Panel);



        //ALSÓ 2 KÁRTYAHELY
        JPanel bottomCards = new JPanel();
        bottomCards.setOpaque(false);
        bottomCards.setPreferredSize(new Dimension(0, 250));
        bottomCards.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 20));
        bottomCards.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        backgroundPanel.add(bottomCards, BorderLayout.SOUTH);
        p1c1Panel = new CardPanel(gameState.getP1Card1(), true);
        p1c1Panel.addPropertyChangeListener("selected", evt -> handleCardSelection(p1c1Panel));
        bottomCards.add(p1c1Panel);

        p1c2Panel = new CardPanel(gameState.getP1Card2(), true);
        p1c2Panel.addPropertyChangeListener("selected", evt -> handleCardSelection(p1c2Panel));
        bottomCards.add(p1c2Panel);



        //BAL OLDALI 5. KÁRTYA
        JPanel sideCard = new JPanel();
        sideCard.setOpaque(false);
        sideCard.setPreferredSize(new Dimension(600, 0));
        sideCard.setLayout(new GridBagLayout());
        backgroundPanel.add(sideCard, BorderLayout.WEST);

        centerPanel = new CardPanel(gameState.getCenterCard(), false);
        sideCard.add(centerPanel);


        //JOBB OLDALI GOMBOK
        JPanel rightButtons = new JPanel();
        rightButtons.setOpaque(false);
        rightButtons.setPreferredSize(new Dimension(600, 0));
        rightButtons.setLayout(new GridBagLayout());
        backgroundPanel.add(rightButtons, BorderLayout.EAST);

        JPanel buttonsColumn = new JPanel();
        buttonsColumn.setOpaque(false);
        buttonsColumn.setLayout(new GridLayout(2, 1, 0, 20));
        rightButtons.add(buttonsColumn);

        JButton saveButton = new ImageButton("/BoardFrame/save_button.png", "/BoardFrame/save_button_press.png", 272, 100);
        JButton exitButton = new ImageButton("/BoardFrame/saveexit_button.png", "/BoardFrame/saveexit_button_press.png", 272, 100);

        // ActionListenerek hozzáadása
        saveButton.addActionListener(e -> showSaveDialog());
        exitButton.addActionListener(e -> showExitSaveDialog());


        buttonsColumn.add(saveButton);
        buttonsColumn.add(exitButton);


        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        backgroundPanel.add(center, BorderLayout.CENTER);

        //KÖZÉPSŐ TÁBLA
        JPanel centerBoard = new JPanel(new GridLayout(5, 5, 5, 5));
        centerBoard.setOpaque(false);
        centerBoard.setPreferredSize(new Dimension(550, 550));
        center.add(centerBoard);

        // Tábla mezőinek létrehozása
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                TilePanel tile = new TilePanel(col, row, gameState, this);
                tileGrid[row][col] = tile;
                centerBoard.add(tile);
            }
        }

        // Megfelelő kártyák listenereinek engedélyezése - Egyik játékos ne férjen hozzá a másik lapjaihoz
        disableAllCardListeners();
        enableCardListenersForCurrentPlayer();
    }

    /**
     * Ez zöldre szinezi azokat a mezőket amik egy adott kártyával és bábuval lehetségesen léphetők
     */
    private void updateHighlights() {
        // Minden mezőről töröljük a zöld kiemelést
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                tileGrid[r][c].setHighlighted(false);
            }
        }

        // Ha nincs kijelölt mező vagy kártya → nincs kiemelés
        if (selectedTile == null || selectedCardPanel == null) {
            return;
        }

        // Kiválasztott mező és kártya kkinyerése
        Card card = selectedCardPanel.getCard();
        Piece piece = gameState.getBoard()[selectedTile.getTileY()][selectedTile.getTileX()];

        // Nincs bábu a kijelölt mezőn
        if (piece == null) {
            return;
        }

        // Bábu tulajdonosa
        int owner = piece.getOwner();

        // Mező koordinátája
        int fromX = selectedTile.getTileX();
        int fromY = selectedTile.getTileY();

        // Végigmegyunk a kártya összes lépésén
        for (int[] m : card.getMoves()) {
            int dx = m[0];
            int dy = m[1];

            // Irány függő az irány az aktuális játékostól függ
            if (owner == 1) {
                dy = -dy;
            }
            else {
                dx = -dx;
            }

            // Lépés végrehajtásával melyik mezőre jutnánk
            int tx = fromX + dx;
            int ty = fromY + dy;

            // táblán belül marad?
            if (tx < 0 || tx > 4 || ty < 0 || ty > 4){
                continue;
            }

            // Saját bábut nem léphet le
            Piece target = gameState.getBoard()[ty][tx];
            if (target != null && target.getOwner() == owner) {
                continue;
            }

            // Ha minden ellenörzés helyes akkor a mezőt kiszinezettre módosítjuk
            tileGrid[ty][tx].setHighlighted(true);
        }
    }

    /**
     * Ez a fügvénya kártyák kijelölését kezeli (Sárga keret) és meghivja az updateHighlights függvényt a végén
     * @param clicked Az a kártya amire éppen ráklikkelt a felhasználó
     */
    private void handleCardSelection(CardPanel clicked) {
        // Ha kattintottak egy új kártyára → előző kijelöltet töröljük
        if (selectedCardPanel != null && selectedCardPanel != clicked) {
            selectedCardPanel.setSelected(false);  // kikapcsoljuk előzőt
            selectedCardPanel.repaint();
        }

        // Ha ugyanarra kattint → kikapcsoljuk
        if (clicked == selectedCardPanel) {
            selectedCardPanel = null;
        } else {
            selectedCardPanel = clicked;
        }

        // Változás után frissítjük a kijelöléseket
        updateHighlights();

    }

    /**
     * Kezeli a játékostábla egyik mezőjére történő kattintást.
     * A metódus működése:
     * Ha egy bábu van a mezőn és az a jelenlegi játékoshoz tartozik,
     * akkor kijelöli azt.
     * Ha már ki volt jelölve egy mező és a kattintott mező egy érvényes,
     * zölden kiemelt célmező, akkor létrehoz és végrehajt egy lépést.
     * Érvénytelen kattintás esetén (ellenfél bábuja vagy nem highlighted mező)
     * a lépés megszakad.
     * Lépés után frissíti a táblát, a kártyákat, popupot jelenít meg,
     * majd ha szükséges, elindítja a Gépi lépését.
     *
     * @param x A kattintott mező X koordinátája (oszlop).
     * @param y A kattintott mező Y koordinátája (sor).
     */
    public void handleTileClick(int x, int y) {


        TilePanel clicked = tileGrid[y][x];
        Piece piece = gameState.getBoard()[y][x];


        // Saját kör szabály
        if (selectedTile == null) {
            if (piece != null && piece.getOwner() != gameState.getCurrentPlayer().getId()) {
                return; // nem a te bábud, nem jelölheted ki
            }
        } else{
            // Ha a kattintott mező NEM highlighted (NEM lépés célpontja)
            // és ellenfél bábuja van rajta → TILTÁS
            if (!clicked.isHighlighted() &&
                    piece != null &&
                    piece.getOwner() != gameState.getCurrentPlayer().getId()) {
                return;
            }
        }

        // Ha a mező ki van emelve zölddel - lépni kell!
        if (clicked.isHighlighted() && selectedTile != null && selectedCardPanel != null) {

            // Lépés létrehozása
            Move move = new Move(
                    selectedTile.getTileX(),
                    selectedTile.getTileY(),
                    x, y,
                    selectedCardPanel.getCard()
            );

            // GameState lépés végrehajtása
            gameState.makeMove(move);

            // Játéktér frissítése
            refreshBoard();
            refreshCards();
            enableCardListenersForCurrentPlayer();


            // Győzelem ellenőrzés
            if (gameState.isGameOver()) {
                int w = gameState.getWinner();
                String img = (w == 1)
                        ? "/Popup/red_win.png"
                        : "/Popup/blue_win.png";

                ImagePopup.show(this, img, 2500,1280, 720, () -> {
                    dispose();
                    new MainMenu().setVisible(true);
                });
                return;
            }

            // KÖvetkező játékos kiírása
            ImagePopup.show(
                    this,
                    (gameState.getCurrentPlayer().getId() == 1)
                            ? "/Popup/red_turn.png"
                            : "/Popup/blue_turn.png",
                    1500,
                    1280, 720,
                    null
            );

            // Jelölések törlése
            selectedTile.setSelected(false);
            selectedTile = null;
            selectedCardPanel.setSelected(false);
            selectedCardPanel = null;
            updateHighlights();

            // Lehet a gép jön meghívjuk lehetséges lépésre
            maybeTriggerAI();
            return;
        }

        // Ha más mező volt kijelölve → azt töröljük
        if (selectedTile != null && selectedTile != clicked) {
            selectedTile.setSelected(false);
        }

        // Kattintás ugyanarra → kikapcsol
        if (selectedTile == clicked) {
            selectedTile.setSelected(false);
            selectedTile = null;
        } else {
            clicked.setSelected(true);
            selectedTile = clicked;
        }

        // Módosítások miat kijelölések frissítése
        updateHighlights();


    }

    /**
     * A 5x5-ös tábla frissítése
     */
    private void refreshBoard() {
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                tileGrid[y][x].updatePiece();
            }
        }
    }


    /**
     * Kártyák pozíciójának frissítése
     */
    private void refreshCards() {
        p1c1Panel.setCard(gameState.getP1Card1());
        p1c2Panel.setCard(gameState.getP1Card2());
        p2c1Panel.setCard(gameState.getP2Card1());
        p2c2Panel.setCard(gameState.getP2Card2());
        centerPanel.setCard(gameState.getCenterCard());

        repaint();
    }


    /**
     * Körtöl függően kártyák kattinthatóvá tétele
     */
    private void enableCardListenersForCurrentPlayer() {
        if (gameState.getCurrentPlayer().getId() == 1) {
            p1c1Panel.enableClick();
            p1c2Panel.enableClick();

            p2c1Panel.disableClick();
            p2c2Panel.disableClick();
        } else {
            p2c1Panel.enableClick();
            p2c2Panel.enableClick();

            p1c1Panel.disableClick();
            p1c2Panel.disableClick();
        }
    }


    /**
     * Összes kártya tiltása (nem kattinthatóak)
     */
    private void disableAllCardListeners() {
        p1c1Panel.disableClick();
        p1c2Panel.disableClick();
        p2c1Panel.disableClick();
        p2c2Panel.disableClick();
    }

    /**
     * Megnézi hogy a Gép köre jön e ha igen kiválasztja a gép mit fog lépni és végbeviszi a lépést
     */
    private void maybeTriggerAI() {
        // Ha nem a gép jön akkor a játékos köre van.
        if (!gameState.getCurrentPlayer().isAI()) {
            enableCardListenersForCurrentPlayer();
            return;
        } else {
            disableAllCardListeners();  // Gép körében egyik kártya sem kattintható
        }

        if (gameState.isGameOver()) return;

        // Timer: mintha gomdolkodna a gép
        aiThinkTimer = new Timer(2500, e -> {
            // Kiválaszja a lépést
            Move aiMove = gameState.getCurrentPlayer().decideMove(gameState);

            // Ha lett kiválasztott lépés végrehajtjuk és frissítjuk a táblát és a kártyákat
            if (aiMove != null) {
                gameState.makeMove(aiMove);
                refreshBoard();
                refreshCards();
            }

            // Győzelem ellenörzés
            if (gameState.isGameOver()) {
                int w = gameState.getWinner();
                String img = (w == 1)
                        ? "/Popup/red_win.png"
                        : "/Popup/blue_win.png";

                ImagePopup.show(this, img, 2500, 1280, 720, () -> {
                    dispose();
                    new MainMenu().setVisible(true);
                });
                return;
            }

            // Soron következő játékos
            ImagePopup.show(
                    this,
                    (gameState.getCurrentPlayer().getId() == 1)
                            ? "/Popup/red_turn.png"
                            : "/Popup/blue_turn.png",
                    1500,
                    1280, 720,
                    null
            );

            // Következő játékos kártyáinak aktiválása
            enableCardListenersForCurrentPlayer();

            // Ha a megin a gép jön meghívja ujra a függvényt
            aiNextTimer = new Timer(500, ev -> maybeTriggerAI());
            aiNextTimer.setRepeats(false);
            aiNextTimer.start();

        });

        aiThinkTimer.setRepeats(false);
        aiThinkTimer.start();
    }

    /**
     * Gép a gép ellen elindítja az első gépi lépést automatikusan
     */
    public void startAIGameAfterPopups() {
        // PIROS az első - ha gép, induljon azonnal
        SwingUtilities.invokeLater(this::maybeTriggerAI);
    }

    /**
     * A Save gomb lenyomásához funkció
     *
     * Amikor a save gombot megnyomjuk megkérdezi a felhasználót melyik slotra akarja menteni a játékot,
     * majd onnan folytatódik a játék ahol elmentette
     */
    private void showSaveDialog() {
        // Gépek működésének leállítása
        stopAITimers();
        Object[] options = {"Save Slot 1", "Save Slot 2", "Cancel"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Choose a slot to save the game:",
                "Save Game",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == JOptionPane.YES_OPTION) {
            saveGameToSlot(1);
        } else if (choice == JOptionPane.NO_OPTION) {
            saveGameToSlot(2);
        }
        // Ha Gép körében történt a mentés gépnek kell jönnie
        maybeTriggerAI();
    }

    /**
     * Amikor kiválasztják a slotot a mentéshez ez a függvény hívódik meg ami elmenti a játék aktuális állását
     * @param slot Egy szám a mentési slot száma
     */
    private void saveGameToSlot(int slot) {
        try {
            filemanagement.SaveLoadManager.save(gameState, slot);

            JOptionPane.showMessageDialog(
                    this,
                    "Game saved successfully to Slot " + slot + "!",
                    "Saved",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Error saving game:\n" + ex.getMessage(),
                    "Save Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * A Save And Exit gomb lenyomásához funkció
     *
     * Amikor a save gombot megnyomjuk megkérdezi a felhasználót melyik slotra akarja menteni a játékot,
     * majd bezárja az aktuális ablakot és nyit egy új MainMenut
     */
    private void showExitSaveDialog() {
        // Gépek működésének leállítása
        stopAITimers();
        Object[] options = {"Save to Slot 1", "Save to Slot 2", "Cancel"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Save your game before exiting?",
                "Exit Game",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == JOptionPane.YES_OPTION) {
            saveGameToSlot(1);
            new MainMenu().setVisible(true);
            dispose();
        } else if (choice == JOptionPane.NO_OPTION) {
            saveGameToSlot(2);
            new MainMenu().setVisible(true);
            dispose();
        }
    }

    /**
     * A gép timereinek leállítása
     */
    private void stopAITimers() {
        if (aiThinkTimer != null) {
            aiThinkTimer.stop();
            aiThinkTimer = null;
        }
        if (aiNextTimer != null) {
            aiNextTimer.stop();
            aiNextTimer = null;
        }
    }
}


