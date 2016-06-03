package nl.avans.a3.game_example;

import nl.avans.a3.event.ModelEvent;
import nl.avans.a3.mvc_interfaces.View;
import nl.avans.a3.util.ResourceHandler;

import java.awt.*;

public class Game_Example_View implements View{
    private Game_Example_Model model;
    private Image axe;

    public Game_Example_View(Game_Example_Model model)
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
