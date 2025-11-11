package view;

import util.MainMenuUtil;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class MainMenu extends JFrame {

    private JButton newGameButton =  new JButton("New Game");
    private JButton loadButton =  new JButton("Load");
    private boolean isFullScreen = true;

    public MainMenu() {
        setTitle("Onitama");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



        setUndecorated(true); // eltávolítja az ablak keretét
        setExtendedState(JFrame.MAXIMIZED_BOTH); // teljes képernyőre vált

        // 🔹 Egyedi háttérpanel, ami méretezéskor újrarajzolja a képet
        BackgroundPanel backgroundPanel = new BackgroundPanel("/back_groundv2.png");
        backgroundPanel.setLayout(null); // hogy kézzel tudjuk elhelyezni a gombokat

        setContentPane(backgroundPanel);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    MainMenuUtil.toggleFullScreen(MainMenu.this);

                }
            }
        });

        // (Később ide jönnek a gombok)
    }



    // 🔹 Saját JPanel, ami automatikusan újraméretezi a háttérképet
    static class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
            backgroundImage = icon.getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // méretezett rajzolás
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

}
