package util;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboPopup;
import java.awt.*;

/**
 * Egy átlátszó megjelenésű kombóbox lenyíló menü (popup) megvalósítása.
 */
public class TransparentComboPopup extends BasicComboPopup {

    /**
     * Létrehoz egy új átlátszó popupot a megadott kombóboxhoz.
     * @param comboBox - az a kombóbox, amelyhez ez a popup tartozik
     */
    public TransparentComboPopup(JComboBox comboBox) {
        super(comboBox);
    }

    /**
     * Egy átlátszó görgetősávot hoz létre a lenyíló lista számára.
     * A lista elemei a TransparentScrollPane osztály segítségével
     * átlátszó környezetben jelennek meg.
     *
     * @return - egy átlátszó JScrollPane, amely a listát tartalmazza
     */
    @Override
    protected JScrollPane createScroller() {
        return new TransparentScrollPane(getList());
    }

    /**
     * Megjeleníti a popupot, de előtte átlátszóvá teszi:
     * - a listát tartalmazó JList elemet,
     * - a kijelölési hátteret és színt,
     * - a popupot körülvevő JPopupMenu elemet.
     *
     * Ez biztosítja, hogy a lenyíló rész teljesen átlátszó legyen,
     * kivéve a tényleges szövegeket.
     */
    @Override
    public void show() {
        JList<?> list = getList();
        list.setOpaque(false);
        list.setBackground(new Color(0, 0, 0, 0));
        list.setSelectionBackground(new Color(0, 0, 0, 0));
        list.setSelectionForeground(new Color(0, 0, 0, 0));

        JPopupMenu pm = (JPopupMenu) getParent();
        if (pm != null) {
            pm.setOpaque(false);
            pm.setBorder(null);
            pm.setBackground(new Color(0, 0, 0, 0));

        }
        super.show();
    }
}