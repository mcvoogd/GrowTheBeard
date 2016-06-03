package nl.avans.a3.main_menu;

import nl.avans.a3.event.NewGameEvent;
import nl.avans.a3.event.NewModel;
import nl.avans.a3.game_2.Game_2_Model;
import nl.avans.a3.mvc_handlers.ModelHandler;
import nl.avans.a3.mvc_interfaces.Model;
import nl.avans.a3.party_mode_handler.PartyModeHandler;
import nl.avans.a3.single_menu.SingleMenuModel;
import nl.avans.a3.util.WiimoteHandler;

import java.awt.geom.Point2D;

public class MainMenuModel implements Model{
    private Point2D pointer;
    PartyModeHandler partyModeHandler;
    
    public enum Mode{
        CHOOSE_PARTY, CHOOSE_SINGLE
    }

    private Mode mode = Mode.CHOOSE_SINGLE;

    @Override
    public void start() {

    }

    @Override
    public void update() {

    }

    @Override
    public void close() {

    }
    
    public void onMenuChoose(WiimoteHandler wiimoteHandler)
    {
        switch (mode)
        {
            case CHOOSE_PARTY: /**ModelHandler.instance.onModelEvent(new NewGameEvent(wiimoteHandler));*/
                partyModeHandler = new PartyModeHandler(PartyModeHandler.Mode.CHOOSE_PARTY, wiimoteHandler, this); PartyModeHandler.update();break;
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

    public void changeMode()
    {
        switch (mode)
        {
            case CHOOSE_PARTY: mode = Mode.CHOOSE_SINGLE; break;
            case CHOOSE_SINGLE: mode = Mode.CHOOSE_PARTY; break;
        }
    }
}
