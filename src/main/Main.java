import view.*;
import game.*;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new BoardFrame(new GameState(new HumanPlayer(1), new HumanPlayer(2)));
        //JFrame frame = new MainMenu();
        frame.setVisible(true);
    }
}
