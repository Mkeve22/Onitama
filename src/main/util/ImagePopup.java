package util;


import javax.swing.*;
import java.awt.*;

/**
 * Egy felugró, keret nélküli ablakot jelenít meg,
 * amelyben egy kép látható egy meghatározott ideig.
 */
public class ImagePopup {

    /**
     * Megjelenít egy időzített, keret nélküli képes popup ablakot.
     *
     * @param parent         A szülő ablak (ennek közepére igazodik a popup).
     * @param imgPath        A megjelenítendő kép elérési útja (resource path).
     * @param displayMillis  A megjelenítés időtartama milliszekundumban.
     * @param width          A kép és a popup kívánt szélessége.
     * @param height         A kép és a popup kívánt magassága.
     * @param afterClose     Opcionális callback, amely a bezárás után fut le
     *                       (lehet null), ilyen esetben semmi sem történik.
     */
    public static void show(JFrame parent, String imgPath, int displayMillis, int width, int height, Runnable afterClose) {

        // kép betöltése
        ImageIcon icon = new ImageIcon(parent.getClass().getResource(imgPath));
        Image scaled = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        icon = new ImageIcon(scaled);


        // JDialog – keret nélkül
        JDialog dialog = new JDialog(parent, true);
        dialog.setUndecorated(true);
        dialog.setLayout(new BorderLayout());

        JLabel label = new JLabel(icon);
        label.setOpaque(false);
        dialog.add(label, BorderLayout.CENTER);

        dialog.pack();
        dialog.setLocationRelativeTo(parent);

        // időzített bezárás
        Timer t = new Timer(displayMillis, e -> {
            dialog.dispose();
            if (afterClose != null) afterClose.run();
        });
        t.setRepeats(false);
        t.start();

        dialog.setVisible(true);
    }
}


