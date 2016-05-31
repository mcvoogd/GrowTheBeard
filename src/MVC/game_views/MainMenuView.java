package MVC.game_views;

import MVC.interfaces_listener.ModelEvent;
import MVC.interfaces_listener.ViewInterface;
import nl.avans.a3.EasyTransformer;
import nl.avans.a3.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainMenuView implements ViewInterface {

    private BufferedImage background;
    private BufferedImage partyGame;
    private BufferedImage singleGame;
    private BufferedImage leftNail;
    private BufferedImage rightNail;

    private static final int PARTY_BOARD_X = 270;
    private static final int PARTY_BOARD_Y = 290;

    private static final int SINGLE_BOARD_X = 1290;
    private static final int SINGLE_BOARD_Y = 220;

    private static final int NAIL_LEFT_OFFSET_X = 340;
    private static final int NAIL_LEFT_OFFSET_Y = 60;

    private static final int NAIL_RIGHT_OFFSET_X = 200;
    private static final int NAIL_RIGHT_OFFSET_Y = 35;

    private double rotation = 5;
    private double speed = 0.5;

    @Override
    public void onModelEvent(ModelEvent e) {

    }

    public enum Mode{
        CHOOSE_PARTY, CHOOSE_SINGLE
    }

    private Mode mode = Mode.CHOOSE_SINGLE;
    
    public MainMenuView(){
        try{
            background = ImageIO.read(new File("res/menu/background.png"));
            partyGame = ImageIO.read(new File("res/menu/party.png"));
            singleGame = ImageIO.read(new File("res/menu/single.png"));
            leftNail = ImageIO.read(new File("res/menu/nail1.png"));
            rightNail = ImageIO.read(new File("res/menu/nail2.png"));
        } catch (IOException e) {
            Logger.instance.log("??000", "Loading images failed", Logger.LogType.ERROR);
        }
    }

    @Override
    public void draw(Graphics2D g) {
        g.drawImage(background, 0, 0, null);
        switch(mode){
            case CHOOSE_PARTY:
                g.drawImage(singleGame, EasyTransformer.rotateAroundCenterWithOffset(singleGame, rotation, 5, -204, SINGLE_BOARD_X, SINGLE_BOARD_Y), null);
                g.drawImage(partyGame, PARTY_BOARD_X, PARTY_BOARD_Y, null);
                break;
            case CHOOSE_SINGLE:
                g.drawImage(partyGame, EasyTransformer.rotateWithOffset(rotation, 350, 10, PARTY_BOARD_X, PARTY_BOARD_Y), null);
                g.drawImage(singleGame, SINGLE_BOARD_X, SINGLE_BOARD_Y, null);
                break;
        }
        g.drawImage(leftNail, PARTY_BOARD_X + NAIL_LEFT_OFFSET_X, PARTY_BOARD_Y - NAIL_LEFT_OFFSET_Y, null);
        g.drawImage(rightNail, SINGLE_BOARD_X + NAIL_RIGHT_OFFSET_X, SINGLE_BOARD_Y - NAIL_RIGHT_OFFSET_Y, null);
    }

    @Override
    public void update() {
        if(rotation < 0){
            speed += 0.01;
        }else if(rotation > 0){
            speed -= 0.01;
        }
        rotation += speed;
    }
}
