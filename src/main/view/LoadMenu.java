package view;

import util.BackgroundPanel;
import util.ImageButton;
import game.*;
import filemanagement.SaveLoadManager;
import util.ImagePopup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * LoadMenu
 * A mentések betöltésének menüjének grafikus felülete
 *
 * Ez az osztály hozza létre a mentések betölltésének menüjét, amelyen három gomb van:
 * - SAVE 1: Egyes save slot betöltése
 * - SAVE 2: kettes save slot betöltése
 * - BACK TO MENU: visszalép a főmenübe
 *
 * A felület beállítja a háttérképet, a gombok pozícióját és a hozzájuk tartozó
 * eseménykezelőket.
 */
public class LoadMenu extends JFrame {

    private JButton save1 = new ImageButton("/LoadMenu/save1.png", "/LoadMenu/save1_press.png", 381, 110);
    private JButton save2 = new ImageButton("/LoadMenu/save2.png", "/LoadMenu/save2_press.png", 381, 110);
    private JButton backmenu = new ImageButton("/LoadMenu/back.png", "/LoadMenu/back_press.png", 381, 146);

    /**
     * A LoadMenu konstruktora
     *
     * Létrehozza a betöltés menüjét, beállítja a háttérképet, a gombok pozícióját,
     * kinézetét, és regisztrálja a hozzájuk tartozó funkciókat.
     */
    public LoadMenu() {
        setTitle("Onitama");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(new  ImageIcon(getClass().getResource("/icon.png")).getImage());
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Egyedi háttérpanel, ami méretezéskor újrarajzolja a képet.
        BackgroundPanel backgroundPanel = new BackgroundPanel("/LoadMenu/back_ground.png");
        backgroundPanel.setLayout(new GridBagLayout());
        setContentPane(backgroundPanel);

        // ActionListenerek hozzáadása a gombokhoz.
        backmenu.addActionListener(new BackButtonListener(this));

        // Load slotok ActionListenerek hozzáadása
        save1.addActionListener(e -> loadSlot(1));
        save2.addActionListener(e -> loadSlot(2));

        // Gombok paneljének létrehozása.
        JPanel buttons =  new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
        buttons.setOpaque(false);

        buttons.add(Box.createVerticalStrut(360));
        buttons.add(save1);
        buttons.add(Box.createVerticalStrut(30));
        buttons.add(save2);
        buttons.add(Box.createVerticalStrut(30));
        buttons.add(backmenu);

        // Gombbok megfelelő helyre igazítása a háttérképre
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0,1155,0,0);
        backgroundPanel.add(buttons, gbc);

    }

    /**
     * BackButtonListener
     * A „BACK TO MENU" gomb eseménykezelője
     *
     * Új főmenü (MainMenu) ablakot hozz létre,
     * majd bezárja az aktuális új játék menü ablakot.
     */
    class BackButtonListener implements ActionListener {

        private LoadMenu loadMenu;


        /**
         * Az eseménykezelő konstruktora.
         *
         * @param loadMenu Az éppen megnyitott LoadMenu, amelyet be kell zárni.
         */
        public BackButtonListener(LoadMenu loadMenu) {
            this.loadMenu = loadMenu;
        }

        /**
         * A „BACK TO MENU” gomb kattintásának kezelése.
         *
         * Létrehozza és megjeleníti a főmenü ablakát,
         * majd bezárja a jelenlegi LoadMenu ablakot.
         *
         * @param e Az esemény objektuma.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            MainMenu mainMenu = new MainMenu();

            mainMenu.setUndecorated(true);
            mainMenu.setExtendedState(JFrame.MAXIMIZED_BOTH);

            mainMenu.setVisible(true);
            loadMenu.dispose();
        }
    }

    /**
     * A SAVE slotok betöltésének eseménye
     *
     * Megvizsgálja hogy létezik e a slot amit betöltenénk,
     * majd betölti a játék állását és létrehozza a játékteret, és bezárja az aktuális ablakot
     * két popup ablakot jelenít még meg az aktuális játékmódot és a következő játékost,
     * sikertelen betöltés esetén error üzenetet dob
     *
     * @param slot A betöltendő mentési slot száma
     */
    private void loadSlot(int slot) {
        try {

            // Slot létezik? - Ha nem hibaüzenet
            if (!SaveLoadManager.slotExists(slot)) {
                JOptionPane.showMessageDialog(
                        this,
                        "This save slot is EMPTY!",
                        "Load Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Betöltés.
            GameState loaded = SaveLoadManager.load(slot);

            // Menü bezárása.
            dispose();

            // Tábla megnyitása.
            BoardFrame board = new BoardFrame(loaded);
            board.setVisible(true);

            // Aktuális game mód kiírása.
            String img = getGameModeImage(loaded.getMode());
            ImagePopup.show(
                    board,
                    img,
                    2000,
                    346, 452,
                    null
            );

            // Következő kör kiírása.
            ImagePopup.show(
                    board,
                    (loaded.getCurrentPlayer().getId() == 1)
                            ? "/Popup/red_turn.png"
                            : "/Popup/blue_turn.png",
                    1500,
                    1280, 720,
                    () -> {
                        // Popup után induljon az AI
                        if (loaded.getCurrentPlayer().isAI()) {
                            board.startAIGameAfterPopups();
                        }
                    }
            );





        } catch (Exception ex) {
            // Kiírjuk a hibát.
            ex.printStackTrace();

            // HIbaüzenet a képernyőre.
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to load the game:\n" + ex.getMessage(),
                    "Load Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * A játékmódhoz tartozó képfájl elérési útvonalát adja vissza.
     *
     * Ez a metódus a GameMode enum alapján kiválasztja és visszaadja azt a képet,
     * amely a betöltés után megjelenő popupon használatos.
     *
     * @param mode A betölteni kívánt játék játékmódja.
     * @return A játékmódhoz tartozó kép elérési útvonala (String).
     */
    private String getGameModeImage(GameMode mode) {
        switch (mode) {
            case PLAYER_VS_PLAYER:
                return "/NewGameMenu/twoplayer_button_rounded.png";

            case PLAYER_VS_AI:
                return "/NewGameMenu/oneplayer_button_rounded.png";

            case AI_VS_AI:
                return "/NewGameMenu/bot_button_rounded.png";

            default:
                return "/NewGameMenu/twoplayer_button_rounded.png";
        }
    }
}
