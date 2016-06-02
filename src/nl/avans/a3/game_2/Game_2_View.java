package nl.avans.a3.game_2;

import nl.avans.a3.event.ModelEvent;
import nl.avans.a3.game_example.Game_Example_Model;
import nl.avans.a3.mvc_interfaces.View;
import nl.avans.a3.util.ResourceHandler;

import java.awt.*;

/**
 * Created by Thijs on 2-6-2016.
 */
public class Game_2_View implements View {

    private Game_Example_Model model;
    private Image axe;

    public Game_2_View(Game_Example_Model model)
    {
        this.model = model;
    }

    @Override
    public void start() {
        axe = ResourceHandler.getImage("res/splash/axe.png");

    }

    @Override
    public void draw(Graphics2D g) {
        g.drawImage(axe, model.getX(), model.getY(),null);
    }

    @Override
    public void close() {

    }

    @Override
    public void onModelEvent(ModelEvent event) {

    }

    public void drawAxe(int x, int y, float pitch)
    {

    }
}
