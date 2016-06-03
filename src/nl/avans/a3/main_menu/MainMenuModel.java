package nl.avans.a3.main_menu;

import nl.avans.a3.event.NewModel;
import nl.avans.a3.game_3.Game_3_Model;
import nl.avans.a3.game_example.Game_Example_Model;
import nl.avans.a3.mvc_handlers.ModelHandler;
import nl.avans.a3.mvc_interfaces.Model;

import java.awt.geom.Point2D;

public class MainMenuModel implements Model{
    private Point2D pointer;



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
    
    public void onMenuChoose()
    {
        ModelHandler.instance.onModelEvent(new NewModel(this, new Game_3_Model()));
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
