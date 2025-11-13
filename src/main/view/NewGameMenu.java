package view;

import util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class NewGameMenu  extends JFrame {

    private JButton backmain = new ImageButton("/NewGameMenu/backmenu_button.png", "/NewGameMenu/backmenu_button_press.png");
    private JButton start = new ImageButton("/NewGameMenu/start_button.png", "/NewGameMenu/start_button_press.png");
    private JComboBox<ImageIcon> options;


    public  NewGameMenu(){
        setTitle("Onitama");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(new ImageIcon(getClass().getResource("/icon.png")).getImage());
        setMinimumSize(new Dimension(800,1000));


        // Egyedi háttérpanel, ami méretezéskor újrarajzolja a képet
        BackgroundPanel backgroundPanel = new BackgroundPanel("/NewGameMenu/newgame_background.png");
        backgroundPanel.setLayout(new GridBagLayout());
        setContentPane(backgroundPanel);

        //Escape billentyű működése
        new EscapeKeyBinding(this);
        backmain.addActionListener(new MainMenuActionListener(this));

        //Combo box elemei
        ImageIcon pvp = new ImageIcon(getClass().getResource("/NewGameMenu/twoplayer_button_rounded.png"));
        ImageIcon pvb = new ImageIcon(getClass().getResource("/NewGameMenu/oneplayer_button_rounded.png"));
        ImageIcon bvb = new ImageIcon(getClass().getResource("/NewGameMenu/bot_button_rounded.png"));

        //Átlátszó ComboBox létrehozása
        options = new JComboBox<>(new ImageIcon[]{pvp, pvb, bvb});
        options.setUI(new TransparentComboBox());
        options.setFocusable(false);
        options.setMaximumRowCount(3);


        JPanel comboPanel = new JPanel();
        comboPanel.setLayout(new BoxLayout(comboPanel, BoxLayout.Y_AXIS));
        comboPanel.setOpaque(false);

        comboPanel.add(Box.createVerticalStrut(340));
        comboPanel.add(options);

        GridBagConstraints cgb = new GridBagConstraints();
        cgb.gridx = 0;
        cgb.gridy = 0;
        cgb.anchor = GridBagConstraints.CENTER;
        cgb.insets = new Insets(0, 0, 0, 265);
        backgroundPanel.add(comboPanel, cgb);


        //Gombok paneljének létrehozása
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);

        buttonPanel.add(Box.createVerticalStrut(100));
        buttonPanel.add(start);
        buttonPanel.add(Box.createVerticalStrut(30));
        buttonPanel.add(backmain);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 0, 500);
        backgroundPanel.add(buttonPanel, gbc);
    }

    //BactToMenu gomb működésére ActionLissener osztály
    class MainMenuActionListener implements ActionListener {

        private NewGameMenu frame;
        public MainMenuActionListener(NewGameMenu frame) {
            this.frame = frame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            MainMenu menu = new MainMenu();

            if (ScreenUtil.isFullScreen()) {
                menu.setUndecorated(true);
                menu.setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
            else {
                menu.setSize(frame.getSize());
                menu.setLocation(frame.getLocation());
            }

            menu.setVisible(true);
            frame.dispose();
        }
    }
}
