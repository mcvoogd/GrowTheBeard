package nl.avans.a3.single_menu;

import nl.avans.a3.event.NewGameEvent;
import nl.avans.a3.event.NewModel;
import nl.avans.a3.game_2.Game_2_Model;
import nl.avans.a3.game_3.Game_3_Model;
import nl.avans.a3.main_menu.MainMenuModel;
import nl.avans.a3.mvc_handlers.ModelHandler;
import nl.avans.a3.mvc_interfaces.Model;
import nl.avans.a3.util.WiimoteHandler;

import java.awt.geom.Point2D;

/**
 * Created by Harmen on 3-6-2016.
 */
public class SingleMenuModel implements Model {

    private Point2D pointer;
    private int modeNumber;
    public enum Mode{
        WOOD_DODGING, WOOD_JUMPING, WOOD_CHOPPING, MAINMENU
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
            case MAINMENU: ModelHandler.instance.changeModel(new NewModel(this, new MainMenuModel())); break;
        }

    }

    public void setPointer(Point2D pointer){
        this.pointer = pointer;
    }

    public Point2D getPointer(){
        return pointer;
    }

    public void switchMenu(int mode){
        modeNumber += mode;
        if(modeNumber < 0){
            modeNumber = 3;
        }
        if(modeNumber > 4){
            modeNumber = 0;
        }
        switch (modeNumber){
            case 0: this.mode = Mode.WOOD_DODGING; break;
            case 1: this.mode = Mode.WOOD_CHOPPING; break;
            case 2: this.mode = Mode.WOOD_JUMPING; break;
            case 3: this.mode = Mode.MAINMENU; break;
        }
    }

    public int getModeNumber(){
        return modeNumber;
    }


}
