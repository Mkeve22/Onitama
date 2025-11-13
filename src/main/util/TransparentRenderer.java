package util;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;


//Csak az kép megjelenítéséért felelős osztály a ComboBox-nál
public class TransparentRenderer extends DefaultListCellRenderer {
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
