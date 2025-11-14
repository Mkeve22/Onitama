package view;

import util.BackgroundPanel;
import util.EscapeKeyBinding;
import util.ImageButton;
import util.ScreenUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoadMenu extends JFrame {

    private JButton save1 = new ImageButton("/LoadMenu/save1.png", "/LoadMenu/save1_press.png", 381, 110);
    private JButton save2 = new ImageButton("/LoadMenu/save2.png", "/LoadMenu/save2_press.png", 381, 110);
    private JButton backmenu = new ImageButton("/LoadMenu/back.png", "/LoadMenu/back_press.png", 381, 146);

    public LoadMenu() {
        setTitle("Onitama");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(new  ImageIcon(getClass().getResource("/icon.png")).getImage());
        setMinimumSize(new Dimension(800,1000));

        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Egyedi háttérpanel, ami méretezéskor újrarajzolja a képet
        BackgroundPanel backgroundPanel = new BackgroundPanel("/LoadMenu/back_ground.png");
        backgroundPanel.setLayout(new GridBagLayout());
        setContentPane(backgroundPanel);

        //new EscapeKeyBinding(this);
        backmenu.addActionListener(new BackButtonListener(this));


        JPanel buttons =  new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
        buttons.setOpaque(false);

        buttons.add(Box.createVerticalStrut(360));
        buttons.add(save1);
        buttons.add(Box.createVerticalStrut(30));
        buttons.add(save2);
        buttons.add(Box.createVerticalStrut(30));
        buttons.add(backmenu);

        buttons.add(save1);
        buttons.add(Box.createVerticalStrut(20));
        buttons.add(save2);
        buttons.add(Box.createVerticalStrut(20));
        buttons.add(backmenu);


       GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0,1155,0,0);
        backgroundPanel.add(buttons, gbc);

    }

    class BackButtonListener implements ActionListener {

        private LoadMenu loadMenu;

        public BackButtonListener(LoadMenu loadMenu) {
            this.loadMenu = loadMenu;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            MainMenu mainMenu = new MainMenu();

            if (ScreenUtil.isFullScreen()){
                mainMenu.setUndecorated(true);
                mainMenu.setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
            else {
                mainMenu.setSize(loadMenu.getSize());
                mainMenu.setLocation(loadMenu.getLocation());
            }

            mainMenu.setVisible(true);
            loadMenu.dispose();
        }
    }
}
