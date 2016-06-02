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
    private Game_Example_Model gameModel;
    private WiimoteHandler wiimoteHandler;

    public Game_2_Controller(Game_Example_Model model, WiimoteHandler wiimoteHandler)
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
            float pitch =  wiimoteHandler.getPitch(0);
            System.out.println("Controller Pitch :" + pitch);
            gameModel.setPitch(pitch);
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
