package util;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;

/**
 * Egy egyedi megjelenésű JComboBox UI osztály,
 * amely teljesen átlátszóvá teszi a kombóbox hátterét
 * és eltünteti a lenyíló nyíl gombot.
 */
public  class TransparentComboBox extends BasicComboBoxUI {

    /**
     * Létrehoz egy láthatatlan nyíl gombot.
     * A gomb visszaadása kötelező a JComboBox működéséhez,
     * de mivel átlátszó UI-t szeretnénk, a gomb nem kerül kirajzolásra
     * és el van rejtve.
     *
     * @return egy láthatatlan JButton példány
     */
    @Override
    protected JButton createArrowButton() {
        JButton b = new JButton();
        b.setVisible(false);
        return b;
    }

    /**
     * Üresen hagyja a háttér kirajzolását,
     * hogy a JComboBox ne rajzoljon semmilyen hátteret
     * az aktuális érték mögé.
     *
     * @param g an instance of {@code Graphics}
     * @param bounds a bounding rectangle to render to
     * @param hasFocus is focused
     */
    @Override
    public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
    }

    /**
     * Egy egyedi lenyíló menu popupot hoz létre,
     * amely szintén átlátszó stílust használ.
     *
     * @return saját popup megjelenés
     */
    @Override
    protected ComboPopup createPopup() {
        return new TransparentComboPopup(comboBox);
    }

    /**
     * Telepíti az UI komponenst a JComboBox-ra.
     * Eltávolít minden szegélyt és alap háttérstílust,
     * valamint átlátszó renderert állít be az elem megjelenítéséhez.
     *
     * @param c the component where this UI delegate is being installed
     *
     */
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
