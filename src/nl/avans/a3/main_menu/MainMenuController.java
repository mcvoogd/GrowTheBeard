package nl.avans.a3.main_menu;

import nl.avans.a3.event.MainMenuEvent;
import nl.avans.a3.event.ModelEvent;
import nl.avans.a3.event.NewGameEvent;
import nl.avans.a3.event.NewModel;
import nl.avans.a3.game_2.Game_2_Model;
import nl.avans.a3.mvc_handlers.ModelHandler;
import nl.avans.a3.mvc_interfaces.Controller;
import nl.avans.a3.util.WiimoteHandler;

import java.awt.event.KeyEvent;

public class MainMenuController implements Controller {
    private boolean exit = false;
    private MainMenuModel model;
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
            case KeyEvent.VK_F : ModelHandler.instance.changeModel(new NewModel(model, new Game_2_Model())); break;
            case KeyEvent.VK_ESCAPE : System.exit(0); break;
            case KeyEvent.VK_A: ModelHandler.instance.onModelEvent(new MainMenuEvent()); break;
            case KeyEvent.VK_D: model.onMenuChoose(); break;
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
