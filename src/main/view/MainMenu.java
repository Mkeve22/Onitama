package view;

import util.MainMenuUtil;
import util.MainMenuUtil.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;


public class MainMenu extends JFrame {

    private JButton newGameButton =  new ImageButton("/newgame_button.png", "/newgame_button_press.png");
    private JButton loadButton =  new ImageButton("/load_button.png", "/load_button_press.png");

    public MainMenu() {
        setTitle("Onitama");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(new ImageIcon(getClass().getResource("/icon.png")).getImage());
        setUndecorated(true); // eltávolítja az ablak keretét
        setExtendedState(JFrame.MAXIMIZED_BOTH); // teljes képernyőre vált

        // Egyedi háttérpanel, ami méretezéskor újrarajzolja a képet
        BackgroundPanel backgroundPanel = new BackgroundPanel("/back_ground.png");
        backgroundPanel.setLayout(new GridBagLayout());
        setContentPane(backgroundPanel);

        //Escape billentyű működése
        new EscapeKeyBinding(this);

        //Panel a gomboknak
        JPanel buttons  = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
        buttons.setOpaque(false);



        // gombokat tartalmazó panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);

        // lejjeb toljuk a gombokat
        buttonPanel.add(Box.createVerticalStrut(250));
        buttonPanel.add(newGameButton);
        buttonPanel.add(Box.createVerticalStrut(30));
        buttonPanel.add(loadButton);

        // középre helyezés a GridBagLayout segítségével
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        backgroundPanel.add(buttonPanel, gbc);
    }


    //Saját JPanel, ami automatikusan újraméretezi a háttérképet
     class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
            backgroundImage = icon.getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    //Saját gomb osztály a kép beszúrás miatt
     class ImageButton extends JButton {
        private final Image image;
        private final ImageIcon pressedImage;


        public ImageButton(String imagePath, String pressedImagePath) {
            image = new ImageIcon(getClass().getResource(imagePath)).getImage();
            pressedImage = new ImageIcon(getClass().getResource(pressedImagePath));
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            Dimension dimension = new Dimension(328, 80);
            setPreferredSize(dimension);
            setMinimumSize(dimension);
            setMaximumSize(dimension);

        }

        //Megrajzolja a gom képét attól függően éppen meg van e nyomva a gomb
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Image toDraw = getModel().isPressed() ? pressedImage.getImage() : image;
            g.drawImage(toDraw, 0, 0, getWidth(), getHeight(), this);
        }
    }

    //Az ESC lenyomásakor végrehajtandó művelet (kilépünk a teljes képernyőből egy előre meghatározott méretbe)
    class EscapeAction extends AbstractAction {
        private final JFrame frame;

        public EscapeAction(JFrame frame) {
            this.frame = frame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            MainMenuUtil.toggleFullScreen(frame);
        }
    }

    //Key Binding kezelés (ESC-hez)
    class EscapeKeyBinding {
        public EscapeKeyBinding(JFrame frame) {
            InputMap im = frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            ActionMap am = frame.getRootPane().getActionMap();

            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "toggleFS");
            am.put("toggleFS", new EscapeAction(frame));
        }
    }

}
