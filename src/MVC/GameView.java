package MVC;

import javax.swing.*;

public class GameView {

    private GameController controller;
    private GameModel model;
    private JFrame frame = new JFrame("Grow the Beard!");
    private JPanel panel = new JPanel();

    public GameView(GameController controller, GameModel model)
    {
        this.model = model;
        this.controller = controller;
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize();
    }




}
