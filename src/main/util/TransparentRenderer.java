package util;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;


/**
 * Egy átlátszó listacella-renderer, amelyet olyan JComboBox vagy JList elemekhez
 * lehet használni, amelyek ikonokat jelenítenek meg szöveg nélkül.
 */
public class TransparentRenderer extends DefaultListCellRenderer {

    /**
     * Létrehozza és visszaadja az adott listaelemhez tartozó
     * megjelenítő komponenst.
     *
     * A cella:
     * - nem tartalmaz szöveget,
     * - az értéket ikonként jeleníti meg,
     * - átlátszó hátteret kap,
     * - eltávolítja a szegélyeket és a kijelölési hátteret.
     *
     * @param list          a lista, amelyhez a cella tartozik
     * @param value         a lista elemének objektuma
     * @param index         a cella indexe
     * @param isSelected    jelzi, hogy a cella ki van-e jelölve
     * @param cellHasFocus  jelzi, hogy a cella fókuszt kapott-e
     * @return a cella megjelenítésére használt komponens
     */
    @Override
    public Component getListCellRendererComponent(
            JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        JLabel l = (JLabel) super.getListCellRendererComponent(list, "", index, isSelected, cellHasFocus);
        l.setIcon((ImageIcon) value);
        l.setText(null);
        l.setOpaque(false);
        l.setBorder(null);
        l.setBackground(new Color(0, 0, 0, 0));

        list.setOpaque(false);
        list.setBorder(null);
        list.setBackground(new Color(0, 0, 0, 0));
        list.setSelectionBackground(new Color(0, 0, 0, 0));
        list.setSelectionForeground(new Color(0, 0, 0, 0));

        return l;
    }
}
