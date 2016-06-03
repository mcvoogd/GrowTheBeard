package nl.avans.a3.main_menu;

import nl.avans.a3.event.MainMenuEvent;
import nl.avans.a3.util.EasyTransformer;
import nl.avans.a3.event.ModelEvent;
import nl.avans.a3.util.ResourceHandler;
import nl.avans.a3.mvc_interfaces.View;

import java.awt.*;

public class MainMenuView implements View {
    private Image background;
    private Image partyGame;
    private Image singleGame;
    private Image leftNail;
    private Image rightNail;

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

        switch(mainMenuModel.getMode()){
            case CHOOSE_PARTY:
                g.drawImage(partyGame, EasyTransformer.rotateWithOffset(rotation, 350, 10, PARTY_BOARD_X, PARTY_BOARD_Y), null);
                g.drawImage(singleGame, SINGLE_BOARD_X, SINGLE_BOARD_Y, null);
                break;
            case CHOOSE_SINGLE:
                g.drawImage(partyGame, PARTY_BOARD_X, PARTY_BOARD_Y, null);
                g.drawImage(singleGame, EasyTransformer.rotateAroundCenterWithOffset(singleGame, rotation, 5, -204, SINGLE_BOARD_X, SINGLE_BOARD_Y), null);
                break;
        }
        g.drawImage(leftNail, PARTY_BOARD_X + NAIL_LEFT_OFFSET_X, PARTY_BOARD_Y - NAIL_LEFT_OFFSET_Y, null);
        g.drawImage(rightNail, SINGLE_BOARD_X + NAIL_RIGHT_OFFSET_X, SINGLE_BOARD_Y - NAIL_RIGHT_OFFSET_Y, null);
        g.setColor(Color.red);
        if(mainMenuModel.getPointer() != null)
            g.fillOval((int) mainMenuModel.getPointer().getX(), (int) mainMenuModel.getPointer().getY(), 10, 10);
    }

    @Override
    public void close() {

    }

    @Override
    public void onModelEvent(ModelEvent event) {
        if(event instanceof MainMenuEvent)
        {
            mainMenuModel.changeMode();
        }

    }
}
