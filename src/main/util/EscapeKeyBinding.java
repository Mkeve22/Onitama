package util;

import java.awt.event.KeyEvent;
import javax.swing.*;
import java.awt.*;

//Key Binding kezelés (ESC-hez)
public class EscapeKeyBinding {

    public EscapeKeyBinding(JFrame frame) {
        InputMap im = frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = frame.getRootPane().getActionMap();

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "toggleFS");
        am.put("toggleFS", new EscapeAction(frame));
    }
}
