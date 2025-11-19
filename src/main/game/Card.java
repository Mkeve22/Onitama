package game;

import javax.swing.*;

public class Card {
    private String name;
    private String imagepath;
    private int[][] moves;

    public Card(String name, String imagepath,  int[][] moves) {
        this.name = name;
        this.imagepath = imagepath;
        this.moves = moves;
    }

    public String getName() {
        return name;
    }

    public String getImagepath() {
        return imagepath;
    }

    public int[][] getMoves() {
        return moves;
    }
}
