package nl.avans.a3.game_2;

import nl.avans.a3.event.ModelEvent;
import nl.avans.a3.game_example.Game_Example_Model;
import nl.avans.a3.mvc_interfaces.Controller;
import nl.avans.a3.util.WiimoteHandler;

import java.awt.event.KeyEvent;

/**
 * Created by Thijs on 2-6-2016.
 */
public class Game_2_Controller implements Controller {
    private Game_2_Model gameModel;
    private WiimoteHandler wiimoteHandler;

    public Game_2_Controller(Game_2_Model model, WiimoteHandler wiimoteHandler)
    {
        this.gameModel = model;
        this.wiimoteHandler = wiimoteHandler;
        this.wiimoteHandler.activateMotionSensing();
    }

    @Override
    public void update() {
        //check wiimote stuff.
        gameModel.update();
        if (wiimoteHandler != null && wiimoteHandler.isWiiMotesConnected()) {
            gameModel.setPitch(wiimoteHandler.getPitch(0), 0);
            gameModel.setPitch(wiimoteHandler.getPitch(1), 1);
            gameModel.setAButtonPressed(wiimoteHandler.getIsButtonPressed(0, WiimoteHandler.Buttons.KEY_A), 0);
            gameModel.setAButtonPressed(wiimoteHandler.getIsButtonPressed(1, WiimoteHandler.Buttons.KEY_A), 1);
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
