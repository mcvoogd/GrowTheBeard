package nl.avans.a3.single_menu;

import nl.avans.a3.event.NewGameEvent;
import nl.avans.a3.event.NewModel;
import nl.avans.a3.game_1.DummyMVC.DummyModel;
import nl.avans.a3.game_2.Game_2_Model;
import nl.avans.a3.game_3.Game_3_Model;
import nl.avans.a3.main_menu.MainMenuModel;
import nl.avans.a3.mvc_handlers.ModelHandler;
import nl.avans.a3.mvc_interfaces.Model;
import nl.avans.a3.util.Beard;
import nl.avans.a3.util.WiimoteHandler;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class SingleMenuModel implements Model {
    private Point2D pointer;
    private int modeNumber;
    private MainMenuModel.Mode chosenMode = MainMenuModel.Mode.CHOOSE_SINGLE;
    private Rectangle2D game1 = new Rectangle2D.Double(160, 100, 830, 450);
    private Rectangle2D game2 = new Rectangle2D.Double(1010, 100, 830, 450);
    private Rectangle2D game3 = new Rectangle2D.Double(160, 585, 830, 450);
    private Rectangle2D back = new Rectangle2D.Double(0, 0, 100, 100);

    public enum Mode{
        WOOD_DODGING, WOOD_JUMPING, WOOD_CHOPPING, MAINMENU, DEFAULT
    }

    public Mode mode = Mode.DEFAULT;

    @Override
    public void start() {
        Beard.beardPlayer1 = 0;
        Beard.beardPlayer2 = 0;
    }

    @Override
    public void update() {
        getIRchosenMenu(pointer);
    }

    @Override
    public void close() {

    }


    private void getIRchosenMenu(Point2D cursor) {
        if(game1.contains(cursor))
        {
            setMode(SingleMenuModel.Mode.WOOD_DODGING);
        }else if(game2.contains(cursor))
        {
            setMode(SingleMenuModel.Mode.WOOD_CHOPPING);
        }else if(game3.contains(cursor))
        {
            setMode(SingleMenuModel.Mode.WOOD_JUMPING);
        }else if(back.contains(cursor))
        {
            setMode(SingleMenuModel.Mode.MAINMENU);
        }else
        {
            setMode(SingleMenuModel.Mode.DEFAULT);
        }
    }

    public void onMenuChoose(WiimoteHandler wiimoteHandler)
    {
        if(mode != Mode.DEFAULT) {
            switch (mode) {
                case WOOD_DODGING:
                    ModelHandler.instance.changeModel(new NewModel(null, new DummyModel())); ModelHandler.instance.onModelEvent(new NewGameEvent(wiimoteHandler)); break;
                case WOOD_JUMPING:
                    ModelHandler.instance.onModelEvent(new NewModel(this, new Game_2_Model()));
                    break;
                case WOOD_CHOPPING:
                    ModelHandler.instance.onModelEvent(new NewModel(this, new Game_3_Model()));
                    break;
                case MAINMENU:
                    ModelHandler.instance.changeModel(new NewModel(this, new MainMenuModel()));
                    break;
            }
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

    public Mode getMode()
    {
        return mode;
    }

    public void setMode(Mode mode)
    {
        this.mode = mode;
    }

    public int getModeNumber(){
        return modeNumber;
    }


}
