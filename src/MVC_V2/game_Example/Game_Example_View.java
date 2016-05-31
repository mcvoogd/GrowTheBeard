package MVC_V2.game_Example;

import MVC_V2.event.ModelEvent;
import MVC_V2.mvcInterfaces.View;
import MVC_V2.util.ResourceHandler;

import java.awt.*;

public class Game_Example_View implements View {
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
