package view;

import util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MainMenu extends JFrame {

    private JButton newGameButton =  new ImageButton("/MainMenu/newgame.png", "/MainMenu/newgame_press.png", 294, 74);
    private JButton loadButton =  new ImageButton("/MainMenu/load.png", "/MainMenu/load_press.png", 294, 74);
    private JButton quit = new ImageButton("/MainMenu/quit.png", "/MainMenu/quit_press.png", 294, 74);

    public MainMenu() {
        setTitle("Onitama");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(new ImageIcon(getClass().getResource("/icon.png")).getImage());
        if (ScreenUtil.isFullScreen()) {
            setUndecorated(true); // eltávolítja az ablak keretét
            setExtendedState(JFrame.MAXIMIZED_BOTH); // teljes képernyőre vált
        }
        setMinimumSize(new Dimension(800,1000));

        // Egyedi háttérpanel, ami méretezéskor újrarajzolja a képet
        BackgroundPanel backgroundPanel = new BackgroundPanel("/MainMenu/back_ground.png");
        backgroundPanel.setLayout(new GridBagLayout());
        setContentPane(backgroundPanel);

        // Escape billentyű működése
        //new EscapeKeyBinding(this);
        newGameButton.addActionListener(new NewGameActionListener(this));
        quit.addActionListener(new QuitActionListener(this));
        loadButton.addActionListener(new LoadButtonListener(this));


        // gombokat tartalmazó panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);

        // lejjeb toljuk a gombokat
        buttonPanel.add(Box.createVerticalStrut(250));
        buttonPanel.add(newGameButton);
        buttonPanel.add(Box.createVerticalStrut(30));
        buttonPanel.add(loadButton);
        buttonPanel.add(Box.createVerticalStrut(30));
        buttonPanel.add(quit);

        // középre helyezés a GridBagLayout segítségével
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        backgroundPanel.add(buttonPanel, gbc);
    }

    class NewGameActionListener implements ActionListener {

        private final JFrame currentFrame; // referenciát tárolunk

        public NewGameActionListener(JFrame currentFrame) {
            this.currentFrame = currentFrame;
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            // Új ablak létrehozása
            NewGameMenu newGame = new NewGameMenu();

            if (ScreenUtil.isFullScreen()) {
                newGame.setUndecorated(true); // eltávolítja az ablak keretét
                newGame.setExtendedState(JFrame.MAXIMIZED_BOTH); // teljes képernyőre vált
            }
            else {
                // Átmásoljuk a méretet és pozíciót
                newGame.setSize(currentFrame.getSize());
                newGame.setLocation(currentFrame.getLocation());
            }

            // Láthatóvá tesszük
            newGame.setVisible(true);

            // Bezárjuk a régit
            currentFrame.dispose();
        }
    }

    class QuitActionListener implements ActionListener {
        private final JFrame currentFrame;

        public QuitActionListener(JFrame currentFrame) {
            this.currentFrame = currentFrame;
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            currentFrame.dispose();
        }
    }

    class LoadButtonListener implements ActionListener {
        private final JFrame currentFrame;

        public LoadButtonListener(JFrame currentFrame) {
            this.currentFrame = currentFrame;
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            LoadMenu menu = new LoadMenu();

            if (ScreenUtil.isFullScreen()) {
                menu.setUndecorated(true);
                menu.setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
            else {
                menu.setSize(currentFrame.getSize());
                menu.setLocation(currentFrame.getLocation());
            }

            menu.setVisible(true);
            currentFrame.dispose();
        }
    }
}
