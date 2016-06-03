package nl.avans.a3.single_menu;

import nl.avans.a3.event.NewGameEvent;
import nl.avans.a3.event.NewModel;
import nl.avans.a3.game_2.Game_2_Model;
import nl.avans.a3.game_3.Game_3_Model;
import nl.avans.a3.mvc_handlers.ModelHandler;
import nl.avans.a3.mvc_interfaces.Model;
import nl.avans.a3.util.WiimoteHandler;

import java.awt.geom.Point2D;

/**
 * Created by Harmen on 3-6-2016.
 */
public class SingleMenuModel implements Model {

    private Point2D pointer;


    public enum Mode{
        WOOD_DODGING, WOOD_JUMPING, WOOD_CHOPPING
    }

    private Mode mode = Mode.WOOD_CHOPPING;

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
            case WOOD_DODGING: ModelHandler.instance.onModelEvent(new NewGameEvent(wiimoteHandler)); break;
            case WOOD_JUMPING:  ModelHandler.instance.onModelEvent(new NewModel(this, new Game_2_Model())); break;
            case WOOD_CHOPPING: ModelHandler.instance.onModelEvent(new NewModel(this, new Game_3_Model())); break;
        }

    }

    public void setPointer(Point2D pointer){
        this.pointer = pointer;
    }

    public Point2D getPointer(){
        return pointer;
    }


}
