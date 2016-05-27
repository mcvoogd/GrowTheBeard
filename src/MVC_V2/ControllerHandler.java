package MVC_V2;

import com.sun.org.apache.bcel.internal.generic.NEW;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by FlorisBob on 27-May-16.
 */
public class ControllerHandler implements ModelListener, KeyListener {
    private Controller controller;
    private Timer updateControllerTimer;

    public ControllerHandler()
    {
        ModelHandler.instance.addListener(this);
        updateControllerTimer = new Timer(1000/60, e -> controller.update());
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
            return new BootController((BootModel) model);
        }
        if(model instanceof MainMenuModel)
        {
            return new MainMenuController((MainMenuModel) model);
        }
        return null;
    }
}
