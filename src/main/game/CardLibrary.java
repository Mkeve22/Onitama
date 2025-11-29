package game;

import java.util.List;

/**
 * Olyan objektumok közös interfésze, amelyek kártyagyűjteményt szolgáltatnak.
 *
 * A CardLibrary célja, hogy a játék során használt kártyák
 * elérésére egységes felületet biztosítson.
 */
public interface CardLibrary {

    /**
     * Visszaadja a kártyákat tartalmazó listát.
     * A lista a könyvtár összes elérhető kártyáját tartalmazza.
     *
     * @return a kártyák listája
     */
    List<Card> getCards();
}
