package nl.avans.a3.winScreen;


import nl.avans.a3.event.ModelEvent;
import nl.avans.a3.event.NewModel;
import nl.avans.a3.main_menu.MainMenuModel;
import nl.avans.a3.mvc_handlers.ModelHandler;
import nl.avans.a3.mvc_interfaces.Controller;
import nl.avans.a3.util.WiimoteHandler;

import java.awt.event.KeyEvent;

public class WinScreen_Controller implements Controller{

    private WinScreen_Model model;
    private WiimoteHandler wiimoteHandler;

    public WinScreen_Controller(WinScreen_Model model, WiimoteHandler wiimoteHandler){
        this.model = model;
        this.wiimoteHandler = wiimoteHandler;
    }

    @Override
    public void update() {
        model.update();
        if (wiimoteHandler.getIsButtonPressed(0, WiimoteHandler.Buttons.KEY_A) || wiimoteHandler.getIsButtonPressed(1, WiimoteHandler.Buttons.KEY_A)) {
            ModelHandler.instance.changeModel(new NewModel(null, new MainMenuModel()));
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            ModelHandler.instance.changeModel(new NewModel(null, new MainMenuModel()));
        }
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            System.exit(0);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void onModelEvent(ModelEvent event) {

    }
}