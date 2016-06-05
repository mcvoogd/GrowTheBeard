package nl.avans.a3.main_menu;

import nl.avans.a3.event.MainMenuEvent;
import nl.avans.a3.util.EasyTransformer;
import nl.avans.a3.event.ModelEvent;
import nl.avans.a3.util.ResourceHandler;
import nl.avans.a3.mvc_interfaces.View;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class MainMenuView implements View {
    private Image background;
    private Image partyGame;
    private Image singleGame;
    private Image leftNail;
    private Image rightNail;
    private Image cursor;

    private final int PARTY_BOARD_X = 270;
    private final int PARTY_BOARD_Y = 290;

    private final int SINGLE_BOARD_X = 1290;
    private final int SINGLE_BOARD_Y = 220;

    private final int NAIL_LEFT_OFFSET_X = 340;
    private final int NAIL_LEFT_OFFSET_Y = 60;

    private final int NAIL_RIGHT_OFFSET_X = 200;
    private final int NAIL_RIGHT_OFFSET_Y = 35;

    
    private double rotation = 0.0;
    private boolean triggered = false;
    private double speed = 0.5;

    private Rectangle2D partymode;
    private Rectangle2D singlemode;
    private boolean hasMenuSelected = false;

    private MainMenuModel mainMenuModel;
    
    public MainMenuView(MainMenuModel model){
        this.mainMenuModel = model;
    }
    
    @Override
    public void start() {
        background = ResourceHandler.getImage("res/menu/background.png");
        partyGame = ResourceHandler.getImage("res/menu/party.png");
        singleGame = ResourceHandler.getImage("res/menu/single.png");
        leftNail = ResourceHandler.getImage("res/menu/nail1.png");
        rightNail = ResourceHandler.getImage("res/menu/nail2.png");
        cursor = ResourceHandler.getImage("res/menu/cursor.png");
        //hitboxes for pointer.
        partymode = new Rectangle2D.Double(PARTY_BOARD_X, PARTY_BOARD_Y+160, partyGame.getWidth(null), partyGame.getHeight(null)-160);
        singlemode = new Rectangle2D.Double(SINGLE_BOARD_X, SINGLE_BOARD_Y+120, singleGame.getWidth(null), singleGame.getHeight(null)-120);
    }

    @Override
    public void draw(Graphics2D g) {
        g.drawImage(background, 0, 0, null);
        if(rotation < 0){
            speed += 0.01;
        }else if(rotation > 0){
            speed -= 0.01;
        }
        rotation += speed;

        g.drawImage(leftNail, PARTY_BOARD_X + NAIL_LEFT_OFFSET_X, PARTY_BOARD_Y - NAIL_LEFT_OFFSET_Y, null);
        g.drawImage(rightNail, SINGLE_BOARD_X + NAIL_RIGHT_OFFSET_X, SINGLE_BOARD_Y - NAIL_RIGHT_OFFSET_Y, null);

        g.setColor(Color.red);

        {
            //THIS IS TO CONTROL THE POINTER WITH KEYBOARD.
            //g.fillOval(mainMenuModel.getPointX(), mainMenuModel.getPointY(), 20, 20);
            //g.draw(partymode);
            //g.draw(singlemode);

            drawChosenMenuWithPointer(g, new Point2D.Double(mainMenuModel.getPointX(), mainMenuModel.getPointY()));
            g.drawImage(cursor, mainMenuModel.getPointX(), mainMenuModel.getPointY(), null);
        }
        if(mainMenuModel.getPointer() != null) {
            drawChosenMenuWithPointer(g, mainMenuModel.getPointer());
            g.drawImage(cursor, (int) mainMenuModel.getPointer().getX(), (int)mainMenuModel.getPointer().getY(), null);
        }
    }

    @Override
    public void close() {

    }

    public void drawChosenMenuWithPointer(Graphics2D g, Point2D curser)
    {
        if(partymode.contains(curser))
        {
            hasMenuSelected = true;
            mainMenuModel.changeMode(MainMenuModel.Mode.CHOOSE_PARTY);
        }else if(singlemode.contains(curser))
        {
            hasMenuSelected = true;
            mainMenuModel.changeMode(MainMenuModel.Mode.CHOOSE_SINGLE);
        }else
        {
            hasMenuSelected = false;
            mainMenuModel.setMode(MainMenuModel.Mode.DEFAULT);
        }
        if(hasMenuSelected) {
            switch (mainMenuModel.getMode()) {
                case CHOOSE_PARTY:
                    g.drawImage(partyGame, EasyTransformer.rotateWithOffset(rotation, 350, 10, PARTY_BOARD_X, PARTY_BOARD_Y), null);
                    g.drawImage(singleGame, SINGLE_BOARD_X, SINGLE_BOARD_Y, null);
                    break;
                case CHOOSE_SINGLE:
                    g.drawImage(partyGame, PARTY_BOARD_X, PARTY_BOARD_Y, null);
                    g.drawImage(singleGame, EasyTransformer.rotateAroundCenterWithOffset(singleGame, rotation, 5, -204, SINGLE_BOARD_X, SINGLE_BOARD_Y), null);
                    break;
            }
        }
        else
        {
            g.drawImage(singleGame, SINGLE_BOARD_X, SINGLE_BOARD_Y, null);
            g.drawImage(partyGame, PARTY_BOARD_X, PARTY_BOARD_Y, null);
        }

    }

    @Override
    public void onModelEvent(ModelEvent event) {
        if(event instanceof MainMenuEvent)
        {
            MainMenuModel.Mode temp = mainMenuModel.getMode();
            switch (temp)
            {
                case CHOOSE_PARTY:   mainMenuModel.changeMode(MainMenuModel.Mode.CHOOSE_SINGLE); break;
                case CHOOSE_SINGLE:   mainMenuModel.changeMode(MainMenuModel.Mode.CHOOSE_PARTY); break;
            }
        }

    }
}
