package util;

import javax.swing.*;
import java.awt.*;

//Átlátszó scroll panel a popup-hoz
public class TransparentScrollPane extends JScrollPane {
    public TransparentScrollPane(JComponent view) {
        super(view);
        setOpaque(false);
        getViewport().setOpaque(false);
        setBorder(null);
        setBackground(new Color(0, 0, 0, 0));
    }
}
