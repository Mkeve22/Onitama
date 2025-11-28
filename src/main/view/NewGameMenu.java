package view;

import util.*;
import game.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @class NewGameMenu
 * @brief Az új játék létrehozásáért felelős grafikus felület
 *
 * Itt ki lehet választani a játékmódot
 * -PvP
 * -PvE
 * -EvE
 * valamint a kártyakönyvtárak közül:
 * -Base Card
 * -Sensei's Card
 * A Back to Menu gombbal viszajut a főmenübe (MainMenu) a felhasználó
 */
public class NewGameMenu  extends JFrame {

    private JButton backmain = new ImageButton("/NewGameMenu/backmenu_button.png", "/NewGameMenu/backmenu_button_press.png" ,372, 104);
    private JButton start = new ImageButton("/NewGameMenu/start_button.png", "/NewGameMenu/start_button_press.png", 372, 104);
    private JComboBox<ImageIcon> gamemod;
    private JComboBox<ImageIcon> cardLibrary;

    /**
     * @brief A NewGameMenu konstruktora
     *
     * Létrehozza az új játék létrehozásához szükséges menüt,
     * beállítja a gombokat és azok helyét,
     * a háttérképet,
     * és regisztrálja a hozzájuk tartozó funkciókat.
     */
    public  NewGameMenu(){
        setTitle("Onitama");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(new ImageIcon(getClass().getResource("/icon.png")).getImage());


        // Egyedi háttérpanel, ami méretezéskor újrarajzolja a képet.
        BackgroundPanel backgroundPanel = new BackgroundPanel("/NewGameMenu/newgame_background.png");
        backgroundPanel.setLayout(new GridBagLayout());
        setContentPane(backgroundPanel);

        // ActionListenerek hozzáadása a gombokhoz.
        backmain.addActionListener(new MainMenuActionListener(this));
        start.addActionListener(new StartGameActionListener(this));

        // Combo box elemei.
        ImageIcon pvp = new ImageIcon(getClass().getResource("/NewGameMenu/twoplayer_button_rounded.png"));
        ImageIcon pve = new ImageIcon(getClass().getResource("/NewGameMenu/oneplayer_button_rounded.png"));
        ImageIcon eve = new ImageIcon(getClass().getResource("/NewGameMenu/bot_button_rounded.png"));

        // Átlátszó ComboBox létrehozása.
        gamemod = new JComboBox<>(new ImageIcon[]{pvp, pve, eve});
        gamemod.setUI(new TransparentComboBox());
        gamemod.setFocusable(false);
        gamemod.setMaximumRowCount(3);

        // Játékmód felirat az átlátszó combobox mellé.
        JLabel modeLabel = new JLabel("GAME MODE:");
        modeLabel.setForeground(new Color(242, 222, 191));
        modeLabel.setFont(new Font("Serif", Font.BOLD, 38));

        // Játékmód panele.
        JPanel modePanel = new JPanel();
        modePanel.setLayout(new FlowLayout(FlowLayout.LEFT,20, 0));
        modePanel.setOpaque(false);

        modePanel.add(modeLabel);
        modePanel.add(gamemod);

        // A háttérképen a megfelelő helyre igazítjuk a játékmód panelt.
        GridBagConstraints cgb = new GridBagConstraints();
        cgb.gridx = 0;
        cgb.gridy = 0;
        cgb.anchor = GridBagConstraints.CENTER;
        cgb.insets = new Insets(250, 0, 0, 265);
        backgroundPanel.add(modePanel, cgb);

        // Combobox elemei.
        ImageIcon base = new ImageIcon(getClass().getResource("/NewGameMenu/basecard.png"));
        ImageIcon sensei = new ImageIcon(getClass().getResource("/NewGameMenu/senseiscard.png"));

        // Átlátszó ComboBox létrehozása.
        cardLibrary = new JComboBox<>(new ImageIcon[]{base, sensei});
        cardLibrary.setUI(new TransparentComboBox());
        cardLibrary.setFocusable(false);
        cardLibrary.setMaximumRowCount(2);

        // Kártyacsomagok panelje.
        JPanel libraryPanel = new JPanel();
        libraryPanel.setLayout(new FlowLayout(FlowLayout.LEFT,20, 0));
        libraryPanel.setOpaque(false);

        // Kártya könyvtár felirat az átlátszó comboboxhoz.
        JLabel cards = new JLabel("CARD LIBRARY:");
        cards.setForeground(new Color(242, 222, 191));
        cards.setFont(new Font("Serif", Font.BOLD, 30));

        libraryPanel.add(cards);
        libraryPanel.add(cardLibrary);

        // A háttérképen a megfelelő helyre igazítjuk a kártyacsomagok paneljét.
        GridBagConstraints agb = new GridBagConstraints();
        agb.gridx = 0;
        agb.gridy = 0;
        agb.anchor = GridBagConstraints.CENTER;
        agb.insets = new Insets(620, 0, 0, 420);
        backgroundPanel.add(libraryPanel, agb);


        //Gombok paneljének létrehozása.
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.add(Box.createVerticalStrut(70));
        buttonPanel.add(start);
        buttonPanel.add(Box.createVerticalStrut(30));
        buttonPanel.add(backmain);

        // Gombok panelét a megfelelő helyre helyezzük a háttérképen.
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 0, 480);
        backgroundPanel.add(buttonPanel, gbc);
    }

    /**
     * @class MainMenuActionListener
     * @brief A „Back TO Menu" gomb eseménykezelője
     *
     * Új főmenü (MainMenu) ablakot hozz létre,
     * majd bezárja az aktuális új játék menü ablakot.
     */
    class MainMenuActionListener implements ActionListener {

        private NewGameMenu frame;

        /**
         * @brief Az eseménykezelő konstruktora.
         *
         * @param frame A jelenlegi NewGameMenu ablak, amelyet be kell zárni.
         */
        public MainMenuActionListener(NewGameMenu frame) {
            this.frame = frame;
        }

        /**
         * @brief A „BACK TO MENU” gomb kattintásának kezelése.
         *
         * Létrehoz egy új főmenüt teljes képernyős módban, majd
         * bezárja a NewGameMenu ablakot.
         *
         * @param e Az esemény objektuma.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            MainMenu menu = new MainMenu();

            menu.setUndecorated(true);
            menu.setExtendedState(JFrame.MAXIMIZED_BOTH);

            menu.setVisible(true);
            frame.dispose();
        }
    }

    /**
     * @class StartGameActionListener
     * @brief A „START GAME" gomb eseménykezelője
     *
     * A menüben megadott játékmód és kártyagyujteménnyel létrehozza a játékot kezelő osztályt (GameState),
     * majd új játékteret hoz létre,
     * és feltünteti egy popup menüvel hogy elkezdődött a játék és ki kezdi
     */
    class StartGameActionListener implements ActionListener {
        private NewGameMenu frame;

        /**
         * @brief Az eseménykezelő konstruktora.
         *
         * @param frame A NewGameMenu ablak, amelyből a játék indul.
         */
        public StartGameActionListener(NewGameMenu frame) {
            this.frame = frame;
        }


        /**
         * @brief A „START GAME” gomb eseményének kezelése.
         *
         * A kiválasztott játékmódtól függően létrehozza a megfelelő játékosokat (Player vagy Ai),
         * inicializálja a GameState objektumot, betölti a játéktáblát, majd popup üzenetekkel jelzi
         * a harc kezdetét és az első kör indulását. AI kontra AI módban automatikusan elindítja
         * a gépek harcát a popupok után.
         *
         * @param e Az esemény objektuma.
         */
        @Override
        public void actionPerformed(ActionEvent e) {

            // Játékmód kinyerése.
            GameMode mode;
            switch (gamemod.getSelectedIndex()) {
                case 0: mode = GameMode.PLAYER_VS_PLAYER; break;
                case 1: mode = GameMode.PLAYER_VS_AI; break;
                case 2: mode = GameMode.AI_VS_AI; break;
                default: mode = GameMode.PLAYER_VS_PLAYER;
            }

            // Játékosok létrehozása.
            Player p1;
            Player p2;

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
                    p1 = new HumanPlayer(1);
                    p2 = new HumanPlayer(2);
            }

            // Kártyakönyvtár tipusának kinyerése.
            int library = cardLibrary.getSelectedIndex();

            // Játék létrehozása a megfelelő adatokkal.
            GameState gs = new GameState(p1, p2, mode,  library);

            // Új játéktér.
            BoardFrame bf = new BoardFrame(gs);
            bf.setVisible(true);

            // Játék kezdő popup
            ImagePopup.show(
                    bf,
                    "/Popup/fight.png",
                    2000,
                    1280, 720,
                    null
            );

            // Első kör popup
            ImagePopup.show(
                    bf,
                    "/Popup/red_turn.png",
                    2000,
                    1280, 720,
                    () -> {
                        //Popup után induljon az AI
                        if (mode == GameMode.AI_VS_AI) {
                            bf.startAIGameAfterPopups();
                        }
                    }
            );

            // NewGameMenu bezárása
            frame.dispose();
        }
    }
}
