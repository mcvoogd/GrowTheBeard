package nl.avans.a3.main_menu;

import nl.avans.a3.event.MainMenuEvent;
import nl.avans.a3.event.ModelEvent;
import nl.avans.a3.event.NewGameEvent;
import nl.avans.a3.event.NewModel;
import nl.avans.a3.game_2.Game_2_Model;
import nl.avans.a3.mvc_handlers.ModelHandler;
import nl.avans.a3.mvc_interfaces.Controller;
import nl.avans.a3.util.WiimoteHandler;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;

public class MainMenuController implements Controller {
    private static final double SCREEN_OFFSET = 200.0;
    private boolean exit = false;
    private MainMenuModel model;
    private WiimoteHandler wiimoteHandler;
    private Point2D pointerLocation;

    public MainMenuController(MainMenuModel model, WiimoteHandler wiimoteHandler)
    {
        this.model = model;
        this.wiimoteHandler = wiimoteHandler;
        wiimoteHandler.activateMotionSensing();
    }
    @Override
    public void update() {
        if(wiimoteHandler.isWiiMotesConnected()) {
            if (wiimoteHandler.getIsButtonPressed(0, WiimoteHandler.Buttons.KEY_A)) {
                ModelHandler.instance.onModelEvent(new MainMenuEvent());
            }
            pointerLocation = new Point2D.Double(wiimoteHandler.getPointer(0).getX()*((1920.0 + SCREEN_OFFSET)/1024.0) - SCREEN_OFFSET/2, wiimoteHandler.getPointer(0).getY()*((1080.0 + SCREEN_OFFSET)/900.0) - SCREEN_OFFSET/2);
            model.setPointer(pointerLocation);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent a) {
        switch (a.getKeyCode())
        {
            case KeyEvent.VK_F : ModelHandler.instance.changeModel(new NewModel(model, new Game_2_Model())); break;
            case KeyEvent.VK_ESCAPE : System.exit(0); break;
            case KeyEvent.VK_A: ModelHandler.instance.onModelEvent(new MainMenuEvent()); break;
            case KeyEvent.VK_D: model.onMenuChoose(wiimoteHandler); break;
            //case KeyEvent.VK_ALT : ModelHandler.instance.onModelEvent(new NewGameEvent(wiimoteHandler)); break;
         }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void onModelEvent(ModelEvent event) {

    }
}
