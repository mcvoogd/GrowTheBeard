package MVC;

import nl.avans.a3.EasyTransformer;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MainMenuView implements ViewInterface{

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

    public enum Mode{
        CHOOSEPARTY, CHOOSESINGLE
    }

    private Mode mode = Mode.CHOOSESINGLE;


    @Override
    public void draw(Graphics2D g) {
        g.drawImage(background, 0, 0, null);
        switch(mode) {
            case CHOOSEPARTY:
                drawParty(g);
                break;
            case CHOOSESINGLE:
                drawSingle(g);
                break;
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

}
