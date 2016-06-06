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
    private Rectangle2D game1 = new Rectangle2D.Double(160, 100, 830, 450);
    private Rectangle2D game2 = new Rectangle2D.Double(1010, 100, 830, 450);
    private Rectangle2D game3 = new Rectangle2D.Double(160, 585, 830, 450);
    private Rectangle2D back = new Rectangle2D.Double(1010, 585, 830, 450);
    private boolean isMenuChosen = false;

    private enum ChosenMenu
    {
        GAME1, GAME2, GAME3, BACK, DEFAULT
    }

    public ChosenMenu current = ChosenMenu.GAME1;

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

        getIRchosenMenu(model.getPointer(), g);
    }

    private void getIRchosenMenu(Point2D cursor, Graphics2D g) {
        if(game1.contains(cursor))
        {
            this.current = ChosenMenu.GAME1;
            model.setMode(SingleMenuModel.Mode.WOOD_DODGING);
            isMenuChosen = true;
        }else if(game2.contains(cursor))
        {
            this.current = ChosenMenu.GAME2;
            model.setMode(SingleMenuModel.Mode.WOOD_CHOPPING);
            isMenuChosen = true;

        }else if(game3.contains(cursor))
        {
            this.current = ChosenMenu.GAME3;
            model.setMode(SingleMenuModel.Mode.WOOD_JUMPING);
            isMenuChosen = true;

        }else if(back.contains(cursor))
        {
            this.current = ChosenMenu.BACK;
            model.setMode(SingleMenuModel.Mode.MAINMENU);
            isMenuChosen = true;
        }else
        {
            this.current = ChosenMenu.DEFAULT;
            model.setMode(SingleMenuModel.Mode.DEFAULT);
            isMenuChosen = false;
        }
    }

    @Override
    public void close() {

    }

    @Override
    public void onModelEvent(ModelEvent event) {

    }
}
