package nl.avans.a3.main_menu;

import nl.avans.a3.event.NewModel;
import nl.avans.a3.mvc_handlers.ModelHandler;
import nl.avans.a3.mvc_interfaces.Model;
import nl.avans.a3.party_mode_handler.PartyModeHandler;
import nl.avans.a3.single_menu.SingleMenuModel;
import nl.avans.a3.util.Beard;
import nl.avans.a3.util.ResourceHandler;
import nl.avans.a3.util.WiimoteHandler;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class MainMenuModel implements Model{
    private Point2D pointer;
    private PartyModeHandler partyModeHandler;
    private Rectangle2D partymode;
    private Rectangle2D singlemode;
    private boolean hasMenuSelected = false;
    private boolean isRumbling = false;
    private final int PARTY_BOARD_X = 270;
    private final int PARTY_BOARD_Y = 290;

    private final int SINGLE_BOARD_X = 1290;
    private final int SINGLE_BOARD_Y = 220;
    private Image partyGame;
    private Image singleGame;

    private int pointX = 0;
    private int pointY = 0;

    public enum Mode{
        CHOOSE_PARTY, CHOOSE_SINGLE, DEFAULT
    }

    private Mode mode = Mode.DEFAULT;

    @Override
    public void start() {
        partyGame = ResourceHandler.getImage("res/menu/party.png");
        singleGame = ResourceHandler.getImage("res/menu/single.png");
        partymode = new Rectangle2D.Double(PARTY_BOARD_X, PARTY_BOARD_Y+160, partyGame.getWidth(null), partyGame.getHeight(null)-160);  // needs to go to model, should be there to be able to 'click' on it
        singlemode = new Rectangle2D.Double(SINGLE_BOARD_X, SINGLE_BOARD_Y+120, singleGame.getWidth(null), singleGame.getHeight(null)-120);
        Beard.beardPlayer1 = 0;
        Beard.beardPlayer2 = 0;
        if(partyModeHandler != null) {
            PartyModeHandler.setCurrentMode(PartyModeHandler.Mode.DEFAULT);
        }
    }

    @Override
    public void update() {
        checkIRinMenu(pointer);
    }

    @Override
    public void close() {

    }


    public void checkIRinMenu(Point2D cursor)
    {
        if(cursor != null) {
            if (partymode.contains(cursor)) {
                hasMenuSelected = true;
                isRumbling = true;
                changeMode(MainMenuModel.Mode.CHOOSE_PARTY);
            } else if (singlemode.contains(cursor)) {
                hasMenuSelected = true;
                isRumbling = true;
                changeMode(MainMenuModel.Mode.CHOOSE_SINGLE);
            } else {
                hasMenuSelected = false;
                isRumbling = false;
                setMode(MainMenuModel.Mode.DEFAULT);
            }
        }
    }

    public boolean doRumble()
    {
        return isRumbling;
    }

    public int getPointY() {
        return pointY;
    }

    public int getPointX() {
        return pointX;
    }

    public void pointToRight()
    {
        pointX+= 10;
    }

    public void pointToLeft()
    {
        pointX-= 10;
    }

    public void pointToTop()
    {
        pointY-= 10;
    }

    public void pointToBottem()
    {
        pointY+= 10;
    }

    public boolean getHasMenuSelected()
    {
        return hasMenuSelected;
    }

    public void onMenuChoose(WiimoteHandler wiimoteHandler)
    {
        switch (mode) {
            case CHOOSE_PARTY:
                if (partyModeHandler == null)
                {
                    partyModeHandler = new PartyModeHandler(PartyModeHandler.Mode.CHOOSE_PARTY, wiimoteHandler, this);
                    PartyModeHandler.update();
                }
                else
                {
                    PartyModeHandler.setCurrentMode(PartyModeHandler.Mode.CHOOSE_PARTY);
                    PartyModeHandler.resetGameCounter();
                    PartyModeHandler.update();
                }
                break;
            case CHOOSE_SINGLE:  ModelHandler.instance.onModelEvent(new NewModel(this, new SingleMenuModel())); break;
        }
    }

    public Point2D getPointer(){
        return pointer;
    }

    public void setPointer(Point2D pointer){
        this.pointer = pointer;
    }

    public Mode getMode()
    {
        return mode;
    }

    public void setMode(Mode newMode){mode = newMode;}

    public void changeMode(Mode chosenmode)
    {
        switch (chosenmode)
        {
            case CHOOSE_PARTY: mode = Mode.CHOOSE_PARTY; break;
            case CHOOSE_SINGLE: mode = Mode.CHOOSE_SINGLE; break;
        }
    }
}
