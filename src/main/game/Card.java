package game;

import javax.swing.*;


/**
 * Ez a kártyál osztálya ami a kártya adatait tárolja,
 * -Nevét
 * -Képének az elérési útját
 * -Az összes lehetséges lépést a kártyával
 */
public class Card {
    private String name;
    private String imagepath;
    private int[][] moves;

    /**
     * Konstruktor
     * létrehozza a kártyát
     * @param name - név
     * @param imagepath - kép elérési út
     * @param moves - lépések
     */
    public Card(String name, String imagepath,  int[][] moves) {
        this.name = name;
        this.imagepath = imagepath;
        this.moves = moves;
    }

    /**
     * @return A kártya neve
     */
    public String getName() {
        return name;
    }

    /**
     * @return A kép elérési útja
     */
    public String getImagepath() {
        return imagepath;
    }

    /**
     * @return Lépései
     */
    public int[][] getMoves() {
        return moves;
    }


}
