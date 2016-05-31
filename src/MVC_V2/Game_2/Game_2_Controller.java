package MVC_V2.game_2;

import MVC_V2.bootMenu.BootModel;
import MVC_V2.mvcInterfaces.Controller;
import MVC_V2.event.ModelEvent;
import MVC_V2.util.WiimoteHandler;

import java.awt.event.KeyEvent;

public class Game_2_Controller implements Controller {

    private Game_2_Model gameModel;
    private WiimoteHandler wiimoteHandler;

    public Game_2_Controller(Game_2_Model model, WiimoteHandler wiimoteHandler)
    {
        this.gameModel = model;
         this.wiimoteHandler = wiimoteHandler;
    }

    @Override
    public void update() {
        //check wiimote stuff.
        if (wiimoteHandler != null && wiimoteHandler.isWiiMotesConnected()) {
            gameModel.setPitch(wiimoteHandler.getPitch(0));
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode())
        {
            case KeyEvent.VK_ESCAPE :
                System.exit(0);
                break;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void onModelEvent(ModelEvent event) {

    }
}
