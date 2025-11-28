package view;

import util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @class MainMenu
 * @brief A játék kezdőképernyőjéért felelős grafikus felület.
 *
 * Ez az osztály hozza létre a teljes képernyős főmenüt, amely három gombot tartalmaz:
 * - New Game: új játék indítása
 * - Load: mentett játékok betöltése
 * - Quit: az alkalmazás bezárása
 *
 * A felület beállítja a háttérképet, a gombok pozícióját és a hozzájuk tartozó
 * eseménykezelőket.
 */
public class MainMenu extends JFrame {

    private JButton newGameButton =  new ImageButton("/MainMenu/newgame.png", "/MainMenu/newgame_press.png", 294, 74);
    private JButton loadButton =  new ImageButton("/MainMenu/load.png", "/MainMenu/load_press.png", 294, 74);
    private JButton quit = new ImageButton("/MainMenu/quit.png", "/MainMenu/quit_press.png", 294, 74);


    /**
     * @brief A MainMenu konstruktora.
     *
     * Létrehozza a kezdőképernyőt, beállítja a háttérképet, a gombok pozícióját,
     * kinézetét, és regisztrálja a hozzájuk tartozó funkciókat.
     */
    public MainMenu() {
        setTitle("Onitama");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(new ImageIcon(getClass().getResource("/icon.png")).getImage());

        // Eltávolítja az ablak keretét.
        setUndecorated(true);
        // Teljes képernyőre vál.
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Egyedi háttérpanel, ami méretezéskor újrarajzolja a képet.
        BackgroundPanel backgroundPanel = new BackgroundPanel("/MainMenu/back_ground.png");
        backgroundPanel.setLayout(new GridBagLayout());
        setContentPane(backgroundPanel);

        // Actionok hozzáadása a gombokhoz.
        newGameButton.addActionListener(new NewGameActionListener(this));
        quit.addActionListener(new QuitActionListener(this));
        loadButton.addActionListener(new LoadButtonListener(this));


        // Gombokat tartalmazó panel.
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);

        // Lejjeb toljuk a gombokat.
        buttonPanel.add(Box.createVerticalStrut(250));
        buttonPanel.add(newGameButton);
        buttonPanel.add(Box.createVerticalStrut(30));
        buttonPanel.add(loadButton);
        buttonPanel.add(Box.createVerticalStrut(30));
        buttonPanel.add(quit);

        // Középre helyezés a GridBagLayout segítségével.
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        backgroundPanel.add(buttonPanel, gbc);
    }
    /**
     * @class NewGameActionListener
     * @brief Az „NEW GAME” gomb eseménykezelője.
     *
     * Új NewGameMenu ablakot hoz létre teljes képernyős módban,
     * majd bezárja az aktuális főmenü ablakot.
     */
    class NewGameActionListener implements ActionListener {

        private final JFrame currentFrame;

        /**
         * @brief Az eseménykezelő konstruktora.
         * @param currentFrame Az aktuális főmenü ablaka, amelyet be kell zárni.
         */
        public NewGameActionListener(JFrame currentFrame) {
            this.currentFrame = currentFrame;
        }

        /**
         * @brief A „NEW GAME” gomb kattintásának kezelése.
         *
         * Új játékindító menüt hoz létre teljes képernyős módban,
         * láthatóvá teszi, majd bezárja a jelenlegi főmenüt.
         *
         * @param ae Az esemény objektuma.
         */
        @Override
        public void actionPerformed(ActionEvent ae) {
            NewGameMenu newGame = new NewGameMenu();

            newGame.setUndecorated(true);
            newGame.setExtendedState(JFrame.MAXIMIZED_BOTH);

            newGame.setVisible(true);
            currentFrame.dispose();
        }
    }

    /**
     * @class QuitActionListener
     * @brief Az alkalmazás bezárásáért felelő eseménykezelő
     *
     * Bezárja az ablakot és kilép a programból
     */
    class QuitActionListener implements ActionListener {
        private final JFrame currentFrame;

        /**
         * @brief Konstruktor.
         * @param currentFrame Az aktuális ablak, amely bezárandó.
         */
        public QuitActionListener(JFrame currentFrame) {
            this.currentFrame = currentFrame;
        }

        /**
         * @brief A „QUIT” gomb eseményének kezelése.
         *
         * Bezárja az ablakot és teljesen kilép a programból.
         *
         * @param ae Az esemény objektuma.
         */
        @Override
        public void actionPerformed(ActionEvent ae) {
            currentFrame.dispose();
            System.exit(0);
        }
    }

    /**
     * @class LoadButtonListener
     * @brief A „LOAD" gomb eseménykezelője
     *
     * Új LoadMenut hoz létre teljes képernyőben,
     * majd bezárja az aktuális főmenü ablakot.
     */
    class LoadButtonListener implements ActionListener {
        private final JFrame currentFrame;


        /**
         * @brief Konstruktor.
         * @param currentFrame Az aktuális főmenü ablak.
         */
        public LoadButtonListener(JFrame currentFrame) {
            this.currentFrame = currentFrame;
        }

        /**
         * @brief A „LOAD” gomb kattintásának kezelése.
         *
         * Megnyitja a mentésbetöltő menüt, teljes képernyőre állítja,
         * majd bezárja a jelenlegi főmenüt.
         *
         * @param ae Az esemény objektuma.
         */
        @Override
        public void actionPerformed(ActionEvent ae) {
            LoadMenu menu = new LoadMenu();

            menu.setUndecorated(true);
            menu.setExtendedState(JFrame.MAXIMIZED_BOTH);

            menu.setVisible(true);
            currentFrame.dispose();
        }
    }
}
