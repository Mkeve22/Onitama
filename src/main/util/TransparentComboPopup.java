package util;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboPopup;
import java.awt.*;


//ComboBox legördülő listáját módósítja
public class TransparentComboPopup extends BasicComboPopup {

    public TransparentComboPopup(JComboBox comboBox) {
        super(comboBox);
    }

    @Override
    protected JScrollPane createScroller() {
        return new TransparentScrollPane(getList());
    }

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