package MVC_V2.mvcHandlers;

import MVC_V2.event.ModelEvent;
import MVC_V2.game_Example.Game_Example_Controller;
import MVC_V2.game_Example.Game_Example_Model;
import MVC_V2.mvcInterfaces.ModelListener;
import MVC_V2.event.NewModel;
import MVC_V2.util.WiimoteHandler;
import MVC_V2.bootMenu.BootController;
import MVC_V2.bootMenu.BootModel;
import MVC_V2.mainMenu.MainMenuController;
import MVC_V2.mainMenu.MainMenuModel;
import MVC_V2.mvcInterfaces.Controller;
import MVC_V2.mvcInterfaces.Model;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by FlorisBob on 27-May-16.
 */
public class ControllerHandler implements ModelListener, KeyListener {
    private Controller controller;
    private Timer updateControllerTimer;
    private static WiimoteHandler wiimoteHandler;

    public ControllerHandler()
    {
        ModelHandler.instance.addListener(this);
        updateControllerTimer = new Timer(1000/60, e -> controller.update());
        wiimoteHandler = new WiimoteHandler();
    }

    @Override
    public void onModelEvent(ModelEvent event) {
        if(event instanceof NewModel)
        {
            this.controller = selectController(((NewModel) event).newModel);
            if(!updateControllerTimer.isRunning())
            updateControllerTimer.start();
        }else
        {
            controller.onModelEvent(event);
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {
        controller.keyTyped(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        controller.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        controller.keyReleased(e);
    }

    private static Controller selectController(Model model)
    {
        if(model instanceof BootModel)
        {
            return new BootController((BootModel) model, wiimoteHandler);
        }
        if(model instanceof MainMenuModel)
        {
            return new MainMenuController((MainMenuModel) model, wiimoteHandler);
        }
        if(model instanceof Game_Example_Model)
        {
            return new Game_Example_Controller((Game_Example_Model) model, wiimoteHandler);
        }

        return null;
    }
}
