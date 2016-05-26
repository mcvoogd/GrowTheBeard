package MVC;

import nl.avans.a3.EasyTransformer;
import nl.avans.a3.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainMenu implements GameModelInterface {

    private BufferedImage background;
    private BufferedImage partyGame;
    private BufferedImage singleGame;
    private BufferedImage leftNail;
    private BufferedImage rightNail;

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

    private Mode mode = Mode.CHOOSESINGLE;

    public enum Mode{
        CHOOSEPARTY, CHOOSESINGLE
    }

    public MainMenu()
    {
        Logger.instance.log("MM001", "MainMenu entered", Logger.LogType.LOG);
        try{
            background = ImageIO.read(new File("res/menu/background.png"));
            partyGame = ImageIO.read(new File("res/menu/party.png"));
            singleGame = ImageIO.read(new File("res/menu/single.png"));
            leftNail = ImageIO.read(new File("res/menu/nail1.png"));
            rightNail = ImageIO.read(new File("res/menu/nail2.png"));


            Logger.instance.log("MM003", "Loading images successful!", Logger.LogType.LOG);
        } catch (IOException e) {
            Logger.instance.log("MM002", "Loading images failed", Logger.LogType.ERROR);
        }
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

    private void drawSingle(Graphics2D g) {
        g.drawImage(partyGame, PARTY_BOARD_X, PARTY_BOARD_Y, null);
        g.drawImage(leftNail, PARTY_BOARD_X + NAIL_LEFT_OFFSET_X, PARTY_BOARD_Y - NAIL_LEFT_OFFSET_Y, null);
        g.drawImage(singleGame, EasyTransformer.rotateAroundCenterWithOffset(singleGame, rotation, 5, -204, SINGLE_BOARD_X, SINGLE_BOARD_Y), null);
        g.drawImage(rightNail, SINGLE_BOARD_X + NAIL_RIGHT_OFFSET_X, SINGLE_BOARD_Y - NAIL_RIGHT_OFFSET_Y, null);
    }

    private void drawParty(Graphics2D g) {
        g.drawImage(partyGame, EasyTransformer.rotateWithOffset(rotation, 350, 10, PARTY_BOARD_X, PARTY_BOARD_Y), null);
        g.drawImage(leftNail, PARTY_BOARD_X + NAIL_LEFT_OFFSET_X, PARTY_BOARD_Y - NAIL_LEFT_OFFSET_Y, null);
        g.drawImage(singleGame, SINGLE_BOARD_X, SINGLE_BOARD_Y, null);
        g.drawImage(rightNail, SINGLE_BOARD_X + NAIL_RIGHT_OFFSET_X, SINGLE_BOARD_Y - NAIL_RIGHT_OFFSET_Y, null);
    }

    public void setMode(Mode mode){
        this.mode = mode;
    }

    public Mode getMode()
    {
        return mode;
    }

    public void setTriggered(boolean triggered)
    {
        this.triggered = triggered;
    }

    public boolean getTriggered()
    {
        return triggered;
    }
}
