package nl.avans.a3.game_2;

import nl.avans.a3.event.ModelEvent;
import nl.avans.a3.mvc_interfaces.View;
import nl.avans.a3.util.ResourceHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by flori on 19-Jun-16.
 */
public class Game_2_View_PreScreen implements View {
    Game_2_Model model;
    BufferedImage[] images;
    int counter = 0;
    final int UPDATES_PER_INDEX = 2;

    public Game_2_View_PreScreen(Game_2_Model model) {this.model = model;}

    @Override
    public void start() {
        BufferedImage image = ResourceHandler.getImage("res/images_game2/instructions.png");
        images = new BufferedImage[3];
        for (int i =0; i < 3; i++)
        {
            images[i] = image.getSubimage(0, 1080 * i, 1920, 1080);
        }
    }

    @Override
    public void draw(Graphics2D g) {
        g.drawImage(images[counter/UPDATES_PER_INDEX], 0, 0, null);

        if (++counter >= UPDATES_PER_INDEX*images.length)
            counter = 0;
    }

    @Override
    public void close() {

    }

    @Override
    public void onModelEvent(ModelEvent event) {

    }
}
