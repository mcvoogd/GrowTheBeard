package nl.avans.a3.mvc_handlers;

import nl.avans.a3.event.ModelEvent;
import nl.avans.a3.game_2.Game_2_Controller;
import nl.avans.a3.game_2.Game_2_Model;
import nl.avans.a3.game_3.Game_3_Controller;
import nl.avans.a3.game_3.Game_3_Model;
import nl.avans.a3.game_example.Game_Example_Controller;
import nl.avans.a3.game_example.Game_Example_Model;
import nl.avans.a3.mvc_interfaces.ModelListener;
import nl.avans.a3.event.NewModel;
import nl.avans.a3.util.WiimoteHandler;
import nl.avans.a3.boot_menu.BootController;
import nl.avans.a3.boot_menu.BootModel;
import nl.avans.a3.main_menu.MainMenuController;
import nl.avans.a3.main_menu.MainMenuModel;
import nl.avans.a3.mvc_interfaces.Controller;
import nl.avans.a3.mvc_interfaces.Model;

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
        if (model instanceof Game_2_Model)
        {
            return new Game_2_Controller((Game_2_Model)model, wiimoteHandler);
        }

        if(model instanceof Game_3_Model){
            return new Game_3_Controller((Game_3_Model) model, wiimoteHandler);
        }
        return null;
    }
}
