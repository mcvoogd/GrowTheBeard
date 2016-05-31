package MVC_V2.bootMenu;

import MVC_V2.mvcInterfaces.Controller;
import MVC_V2.event.ModelEvent;
import MVC_V2.util.WiimoteHandler;

import java.awt.event.KeyEvent;

public class BootController implements Controller {

    private boolean aPressed = false;
    private boolean bPressed = false;
    private BootModel bootModel;
    private WiimoteHandler wiimoteHandler = null;

    public BootController(BootModel model, WiimoteHandler wiimoteHandler)
    {
        this.bootModel = model;
        this.wiimoteHandler = wiimoteHandler;
    }
    @Override
    public void onModelEvent(ModelEvent event) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode())
        {
            case KeyEvent.VK_A : aPressed = true; break;
            case KeyEvent.VK_B : bPressed = true; break;
            case KeyEvent.VK_SPACE : aPressed = bPressed = true; break;
            case KeyEvent.VK_C : wiimoteHandler.SearchWiimotes();
                System.out.println("searching wiimotes");break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void update() {
        if(aPressed && bPressed)
        {
            bootModel.onABPressed();
        }
        aPressed = bPressed = false;
        if(wiimoteHandler.isWiiMotesConnected()) {
            if (wiimoteHandler.getIsButtonDown(0, WiimoteHandler.Buttons.KEY_A) && wiimoteHandler.getIsButtonDown(0, WiimoteHandler.Buttons.KEY_B)) {
                bootModel.onABPressed();
            }
        }
    }
}
