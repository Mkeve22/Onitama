package view;

import game.*;
import util.*;

import javax.swing.*;
import java.awt.*;

public class BoardFrame extends JFrame {

    private CardPanel selectedCardPanel = null;


    public BoardFrame() {
        setTitle("Onitama");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(new ImageIcon(getClass().getResource("/icon.png")).getImage());
        setMinimumSize(new Dimension(800, 1000));
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        //HÁTTÉRPANEL
        BackgroundPanel backgroundPanel = new BackgroundPanel("/BoardFrame/background.png");
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);

        //Deck létrehozása
        DeckManager deck = new DeckManager(new BaseCardLibrary());
        deck.dealcard();

        //FELSŐ 2 KÁRTYAHELY
        JPanel topCards = new JPanel();
        topCards.setOpaque(false);
        topCards.setPreferredSize(new Dimension(0, 250));
        topCards.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 20));
        topCards.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0)); // <<< ITT


        backgroundPanel.add(topCards, BorderLayout.NORTH);
        for (Card c : deck.getPlayer2Cards()){
            CardPanel cp = new CardPanel(c);
            cp.addPropertyChangeListener("selected", evt -> handleCardSelection(cp));
            topCards.add(cp);
        }



        //ALSÓ 2 KÁRTYAHELY
        JPanel bottomCards = new JPanel();
        bottomCards.setOpaque(false);
        bottomCards.setPreferredSize(new Dimension(0, 250));
        bottomCards.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 20));
        bottomCards.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0)); // <<< ITT

        backgroundPanel.add(bottomCards, BorderLayout.SOUTH);
        for (Card c : deck.getPlayer1Cards()){
            CardPanel cp = new CardPanel(c);
            cp.addPropertyChangeListener("selected", evt -> handleCardSelection(cp));
            bottomCards.add(cp);
        }



        //BAL OLDALI 5. KÁRTYA
        JPanel sideCard = new JPanel();
        sideCard.setOpaque(false);
        sideCard.setPreferredSize(new Dimension(600, 0));
        sideCard.setLayout(new GridBagLayout());
        backgroundPanel.add(sideCard, BorderLayout.WEST);
        sideCard.add(new CardPanel(deck.getCenter()));


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

        // mezők szélének szinezése
        for (int i = 0; i < 25; i++) {
            JLabel tile = new JLabel();
            tile.setOpaque(false);
            tile.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2)); // DEBUG
            centerBoard.add(tile);
        }

        setVisible(true);
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
    }

}
