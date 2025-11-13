package util;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;

//Icon alapú ComboBox UI
public  class TransparentComboBox extends BasicComboBoxUI {

    @Override
    protected JButton createArrowButton() {
        JButton b = new JButton();
        b.setVisible(false);
        return b;
    }

    @Override
    public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
    }

    @Override
    protected ComboPopup createPopup() {
        return new TransparentComboPopup(comboBox);
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        JComboBox<?> cb = (JComboBox<?>) c;
        cb.setOpaque(false);
        cb.setBackground(new Color(0, 0, 0, 0));
        cb.setBorder(BorderFactory.createEmptyBorder());
        cb.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
        cb.putClientProperty("JComponent.sizeVariant", "mini");
        cb.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 0));
        cb.setRenderer(new TransparentRenderer());
    }
}
