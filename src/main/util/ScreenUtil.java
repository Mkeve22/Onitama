package util;

import javax.swing.*;


//Segéd osztály a MainMenunek
public class ScreenUtil {
    // Globális állapotváltozó — mindig tudjuk, épp fullscreenben vagyunk-e
    private static boolean isFullScreen = true;

    public static void toggleFullScreen(JFrame frame) {
        frame.dispose(); // kell, hogy újra lehessen építeni a dekorációkat

        //váltás fullscreenbe/vissza ablakos módba
        if (!isFullScreen) {
            frame.setUndecorated(true);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            isFullScreen = true;
        } else {
            frame.setUndecorated(false);
            frame.setExtendedState(JFrame.NORMAL);
            frame.setSize(1280, 720);
            frame.setLocationRelativeTo(null);
            isFullScreen = false;
        }

        frame.setVisible(true);
    }

    // lekérdezés bárhonnan
    public static boolean isFullScreen() {
        return isFullScreen;
    }

}
