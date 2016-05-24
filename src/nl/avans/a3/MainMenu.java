package nl.avans.a3;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by kevin on 20/05/2016.
 */
public class MainMenu extends JPanel {

    private BufferedImage background;
    private BufferedImage partyGame;
    private BufferedImage singleGame;

    private final int PARTYBOARDX = 285;
    private final int PARTYBOARDY = 290;

    private Timer rotatePartyTimer;
    private int rotation = 0;
    private final int MINROTATION = -10;
    private final int MAXROTATION = 20;
    private boolean triggered = false;

    public enum Mode
    {
        CHOOSEPARTY, CHOOSESINGLE
    }

    private Mode mode = Mode.CHOOSEPARTY;

    public MainMenu()
    {
        Logger.instance.log("MM001", "MainMenu entered", Logger.LogType.LOG);
        try
        {
            background = ImageIO.read(new File("res/images_menu/menu_background.png"));
            partyGame = ImageIO.read(new File("res/images_menu/menu_party.png"));
            singleGame = ImageIO.read(new File("res/images_menu/menu_single.png"));
            Logger.instance.log("MM003", "Loading images succesvol!", Logger.LogType.LOG);

        } catch (IOException e) {
            Logger.instance.log("MM002", "Loading images failed", Logger.LogType.ERROR);
        }

        rotatePartyTimer = new Timer(1000/60, e -> {

            if (rotation < MAXROTATION && !triggered) {
                rotation += 1;
                if (rotation >= MAXROTATION) {
                    triggered = true;
                }
            } else {
                rotation -= 1;
                if (rotation <= MINROTATION) {
                    triggered = false;
                }
            }
        });

    }

    public void update(Graphics2D g)
    {

        g.drawImage(background, 0, 0, null);
        switch(mode)
        {
            case CHOOSEPARTY : drawParty(g); break;
            case CHOOSESINGLE : drawSingle(g); break;
        }
    }

    private void drawSingle(Graphics2D g) {

    }

    private void drawParty(Graphics2D g) {
        if(!rotatePartyTimer.isRunning())
        {
            rotatePartyTimer.start();
        }
       g.drawImage(partyGame, EasyTransformer.rotateAroundCenterWithOffset(partyGame, rotation, -55,
               -partyGame.getHeight()/2 , PARTYBOARDX, PARTYBOARDY), null);
       g.setColor(Color.DARK_GRAY);
       g.fillOval(PARTYBOARDX + 280, PARTYBOARDY - 10, 50, 50); //TODO spijker image.!
    }

    public void setMode(Mode mode)
    {
       this.mode = mode;
    }
}
