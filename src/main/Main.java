import view.*;
import game.*;

import javax.swing.*;
import java.awt.*;

/**
 * Main
 * A program belépési pontja, amely elindítja az alkalmazást.
 *
 * A main metódus létrehozza és megjeleníti a főmenüt (MainMenu),
 * ezzel indítva a grafikus felhasználói felületet.
 */
public class Main {

    /**
     * Az alkalmazás indítási pontja.
     *
     * Létrehozza a főmenü ablakát, maximális méretben láthatóvá teszi,
     * és átadja az irányítást a grafikus felhasználói felületnek.
     *
     * @param args Parancssori argumentumok (a program nem használja őket).
     */
    public static void main(String[] args) {
        JFrame frame = new MainMenu();
        frame.setVisible(true);
    }
}
