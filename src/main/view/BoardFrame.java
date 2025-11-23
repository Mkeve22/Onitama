package view;

import game.*;
import util.*;

import javax.swing.*;
import java.awt.*;

public class BoardFrame extends JFrame {

    private CardPanel selectedCardPanel = null;
    private GameState gameState;
    private TilePanel selectedTile = null;
    private TilePanel[][] tileGrid = new TilePanel[5][5];

    private CardPanel p2c1Panel;
    private CardPanel p2c2Panel;

    private CardPanel p1c1Panel;
    private CardPanel p1c2Panel;

    private CardPanel centerPanel;

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
        p2c1Panel.setRotated(true); // 🔹 FELSŐ KÁRTYA FORGATÁSA
        p2c1Panel.addPropertyChangeListener("selected", evt -> handleCardSelection(p2c1Panel));
        topCards.add(p2c1Panel);

        p2c2Panel = new CardPanel(gameState.getP2Card2(), true);
        p2c2Panel.setRotated(true); // 🔹 FELSŐ KÁRTYA FORGATÁSA
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

        buttonsColumn.add(saveButton);
        buttonsColumn.add(exitButton);


        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        backgroundPanel.add(center, BorderLayout.CENTER);

        //KÖZÉPSŐ TÁBLA
        JPanel centerBoard = new JPanel(new GridLayout(5, 5, 5, 5));
        centerBoard.setOpaque(false);
        centerBoard.setPreferredSize(new Dimension(550, 550));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        center.add(centerBoard, gbc);

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                TilePanel tile = new TilePanel(col, row, gameState, this);
                tileGrid[row][col] = tile;
                centerBoard.add(tile);
            }
        }

        disableAllCardListeners();
        enableCardListenersForCurrentPlayer();

    }

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

        Card card = selectedCardPanel.getCard();
        Piece piece = gameState.getBoard()[selectedTile.getTileY()][selectedTile.getTileX()];

        if (piece == null) {
            return;                 // nincs bábu a kijelölt mezőn
        }
        int owner = piece.getOwner();

        int fromX = selectedTile.getTileX();
        int fromY = selectedTile.getTileY();

        for (int[] m : card.getMoves()) {
            int dx = m[0];
            int dy = m[1];

            // fontos: irány függő
            if (owner == 1) {
                dy = -dy;
            }
            else {
                dx = -dx;
            }

            int tx = fromX + dx;
            int ty = fromY + dy;

            // táblán belül marad?
            if (tx < 0 || tx > 4 || ty < 0 || ty > 4){
                continue;
            }

            // saját bábut nem léphet le
            Piece target = gameState.getBoard()[ty][tx];
            if (target != null && target.getOwner() == owner) {
                continue;
            }

            tileGrid[ty][tx].setHighlighted(true);
        }
    }

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
        updateHighlights();

    }

    public void handleTileClick(int x, int y) {

        // A TilePanel példányt onnan kapjuk meg, ahol eltároltuk őket
        TilePanel clicked = tileGrid[y][x];
        Piece piece = gameState.getBoard()[y][x];


        // ❗ Saját kör szabály
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

        // Ha a mező ki van emelve zölddel → lépni kell!
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

            // UI frissítés
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
        updateHighlights();
    }

    public void refreshBoard() {
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                tileGrid[y][x].updatePiece();
            }
        }
        repaint();
    }

    public void refreshCards() {
        p1c1Panel.setCard(gameState.getP1Card1());
        p1c2Panel.setCard(gameState.getP1Card2());
        p2c1Panel.setCard(gameState.getP2Card1());
        p2c2Panel.setCard(gameState.getP2Card2());
        centerPanel.setCard(gameState.getCenterCard());

        repaint();
    }

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


    private void disableAllCardListeners() {
        p1c1Panel.disableClick();
        p1c2Panel.disableClick();
        p2c1Panel.disableClick();
        p2c2Panel.disableClick();
    }
}
