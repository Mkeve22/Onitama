package util;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CardClickListener extends MouseAdapter {

    private CardPanel cardPanel;

    public CardClickListener(CardPanel cardPanel) {
        this.cardPanel = cardPanel;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        boolean oldValue = cardPanel.isSelected();
        boolean newValue = !oldValue;

        cardPanel.setSelected(newValue);
        cardPanel.repaint();

        cardPanel.firePropertyChange("selected", oldValue, newValue);
    }
}
