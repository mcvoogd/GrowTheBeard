package MVC_V2.mainMenu;

import MVC_V2.event.MainMenuEvent;
import MVC_V2.event.ModelEvent;
import MVC_V2.event.NewGameEvent;
import MVC_V2.mvcHandlers.ModelHandler;
import MVC_V2.mvcInterfaces.Controller;
import MVC_V2.mvcInterfaces.Model;
import MVC_V2.util.WiimoteHandler;

import java.awt.event.KeyEvent;

public class MainMenuController implements Controller {
    private boolean exit = false;
    private Model model;
    private WiimoteHandler wiimoteHandler;

    public MainMenuController(MainMenuModel model, WiimoteHandler wiimoteHandler)
    {
        this.model = model;
        this.wiimoteHandler = wiimoteHandler;
    }
    @Override
    public void update() {
        if(wiimoteHandler.isWiiMotesConnected()) {
            if (wiimoteHandler.getIsButtonPressed(0, WiimoteHandler.Buttons.KEY_A)) {
                ModelHandler.instance.onModelEvent(new MainMenuEvent());
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent a) {
        switch (a.getKeyCode())
        {
            case KeyEvent.VK_ESCAPE : System.exit(0); break;
            case KeyEvent.VK_A: ModelHandler.instance.onModelEvent(new MainMenuEvent()); break;
            case KeyEvent.VK_ALT : ModelHandler.instance.onModelEvent(new NewGameEvent(wiimoteHandler)); break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void onModelEvent(ModelEvent event) {

    }
}
