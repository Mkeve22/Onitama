package util;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Kártyák kattinthatóságát lehetővé tevő osztály
 * A MouseAdapterből származik le
 */
public class CardClickListener extends MouseAdapter {

    private CardPanel cardPanel;

    /**
     * Konstruktor
     * @param cardPanel Egy Cardpanelt kap paraméterül amikben a kártyákat tároljuk
     */
    public CardClickListener(CardPanel cardPanel) {
        this.cardPanel = cardPanel;
    }

    /**
     * Minden kattintásra megváltoztatja hogy a kártya ki van e választva (selected adattagja),
     * majd üzenetet küld hogy megváltozott ez at érték
     * @param e the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent e) {
        // Érték megváltoztatatása (true/false)
        boolean oldValue = cardPanel.isSelected();
        boolean newValue = !oldValue;

        cardPanel.setSelected(newValue);
        cardPanel.repaint();

        // Üzenet küldés a változtatásról
        cardPanel.firePropertyChange("selected", oldValue, newValue);
    }
}
