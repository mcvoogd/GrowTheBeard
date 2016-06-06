package nl.avans.a3.single_menu;

import nl.avans.a3.event.ModelEvent;
import nl.avans.a3.mvc_interfaces.View;
import nl.avans.a3.util.ResourceHandler;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Created by Harmen on 3-6-2016.
 */
public class SingleMenuView implements View {

    private SingleMenuModel model;
    private BufferedImage background;

    public SingleMenuView(SingleMenuModel model){
        this.model = model;
    }

    @Override
    public void start() {
        background = ResourceHandler.getImage("res/menu/background_Single.png");
    }

    @Override
    public void draw(Graphics2D g) {
        g.drawImage(background, 0, 0, null);
        g.setColor(Color.red);
        if(model.getPointer() != null)
            g.fillOval((int) model.getPointer().getX(), (int) model.getPointer().getY(), 10, 10);
        switch (model.getModeNumber()){
            case 0: g.drawRect(160, 100, 830, 450); break;
            case 1: g.drawRect(1010, 100, 830, 450); break;
            case 2: g.drawRect(160, 585, 830, 450); break;
            case 3: g.drawRect(1010, 585, 830, 450); break;
        }
    }

    @Override
    public void close() {

    }

    @Override
    public void onModelEvent(ModelEvent event) {

    }
}
