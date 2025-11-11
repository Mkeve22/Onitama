package util;

import view.*;
import javax.swing.*;

import javax.swing.*;

public class MainMenuUtil {

    private static boolean isFullScreen = true;

    public static void toggleFullScreen(JFrame frame) {
        frame.dispose(); // kötelező újraépítéshez

        if (isFullScreen) {
            // --- vissza ablakos módba ---
            frame.setUndecorated(false);
            frame.setExtendedState(JFrame.NORMAL);
            frame.setSize(1280, 720);
            frame.setLocationRelativeTo(null);
        } else {
            // --- vissza fullscreen módba ---
            frame.setUndecorated(true);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }

        isFullScreen = !isFullScreen;
        frame.setVisible(true);
    }
}
