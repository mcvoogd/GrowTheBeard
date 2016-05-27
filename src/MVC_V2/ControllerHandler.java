package MVC_V2;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by FlorisBob on 27-May-16.
 */
public class ControllerHandler implements ModelListener, KeyListener {
    private Controller controller;

    public ControllerHandler()
    {
        ModelHandler.instance.addListener(this);
    }

    @Override
    public void onModelEvent(ModelEvent event) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
