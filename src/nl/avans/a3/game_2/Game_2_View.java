package nl.avans.a3.game_2;

import nl.avans.a3.event.ModelEvent;
import nl.avans.a3.mvc_interfaces.View;
import nl.avans.a3.util.ResourceHandler;

import java.awt.*;

/**
 * Created by Thijs on 2-6-2016.
 */
public class Game_2_View implements View {

    private Game_2_Model model;

    public Game_2_View(Game_2_Model model)
    {
        this.model = model;
    }

    @Override
    public void start() {

    }

    @Override
    public void draw(Graphics2D g) {

    }

    @Override
    public void close() {

    }

    @Override
    public void onModelEvent(ModelEvent event) {

    }
}
