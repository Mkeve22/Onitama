package util;

import javax.swing.*;
import java.awt.*;

/**
 * Egy teljesen átlátszó JScrollPane megvalósítás.
 */
public class TransparentScrollPane extends JScrollPane {

    /**
     * Létrehoz egy új átlátszó scroll panelt a megadott komponenshez.
     *
     * @param view az a tartalom, amely a görgetőpanelben megjelenik
     */
    public TransparentScrollPane(JComponent view) {
        super(view);
        setOpaque(false);
        getViewport().setOpaque(false);
        setBorder(null);
        setBackground(new Color(0, 0, 0, 0));
    }
}
