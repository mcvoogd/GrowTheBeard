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
    private Image cursor;

    public SingleMenuView(SingleMenuModel model){
        this.model = model;
    }

    @Override
    public void start() {
        background = ResourceHandler.getImage("res/menu/background_Single.png");
        cursor = ResourceHandler.getImage("res/menu/cursor.png");
    }

    @Override
    public void draw(Graphics2D g) {
        g.drawImage(background, 0, 0, null);
        g.setColor(Color.red);
        if(model.getPointer() != null)
        {
            g.drawImage(cursor, (int) model.getPointer().getX(), (int) model.getPointer().getY(), null);
        }
        g.drawRect(30, 0, 100, 100);

    }

    @Override
    public void close() {

    }

    @Override
    public void onModelEvent(ModelEvent event) {

    }
}
