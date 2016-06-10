package nl.avans.a3.start_screen;

import nl.avans.a3.event.ModelEvent;
import nl.avans.a3.mvc_interfaces.View;
import nl.avans.a3.util.ResourceHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Start_View implements View {

    private BufferedImage warningScreen;
    private BufferedImage game1Explanation;
    private BufferedImage game2Explanation;
    private BufferedImage game3Explanation;

    private Start_Model model;

    public Start_View(Start_Model model)
    {
        this.model = model;
    }

    @Override
    public void start() {
        warningScreen = ResourceHandler.getImage("res/warning.png");
        game1Explanation = ResourceHandler.getImage("res/warning.png");
        game2Explanation = ResourceHandler.getImage("res/warning.png");
        game3Explanation = ResourceHandler.getImage("res/warning.png");

    }

    @Override
    public void draw(Graphics2D g) {
        switch(model.getChosenGame())
        {
            case BOOT:  g.drawImage(warningScreen, 0, 0, null);
                break;
            case GAME1: g.drawImage(game1Explanation, 0, 0, null);
                break;
            case GAME2: g.drawImage(game2Explanation, 0, 0, null);
                break;
            case GAME3: g.drawImage(game3Explanation, 0, 0, null);
                break;
        }
    }

    @Override
    public void close() {

    }

    @Override
    public void onModelEvent(ModelEvent event) {

    }
}
