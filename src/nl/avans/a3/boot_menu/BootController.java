package nl.avans.a3.boot_menu;

import nl.avans.a3.event.NewModel;
import nl.avans.a3.game_2.Game_2_Model;
import nl.avans.a3.mvc_handlers.ModelHandler;
import nl.avans.a3.mvc_interfaces.Controller;
import nl.avans.a3.event.ModelEvent;
import nl.avans.a3.util.Logger;
import nl.avans.a3.util.WiimoteHandler;

import java.awt.event.KeyEvent;

public class BootController implements Controller{

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
    public void onModelEvent(ModelEvent event) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode())
        {
            case KeyEvent.VK_F : ModelHandler.instance.onModelEvent(new NewModel(bootModel, new Game_2_Model())); break;
            case KeyEvent.VK_ESCAPE : System.exit(0); break;
            case KeyEvent.VK_A : aPressed = true; break;
            case KeyEvent.VK_B : bPressed = true; break;
            case KeyEvent.VK_SPACE : aPressed = bPressed = true; break;
            case KeyEvent.VK_C : wiimoteHandler.searchWiimotes();
                Logger.instance.log("searching wiimotes"); break;
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
