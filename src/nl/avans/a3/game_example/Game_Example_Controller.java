package nl.avans.a3.game_example;

import nl.avans.a3.mvc_interfaces.Controller;
import nl.avans.a3.event.ModelEvent;
import nl.avans.a3.util.WiimoteHandler;

import java.awt.event.KeyEvent;

public class Game_Example_Controller implements Controller{

    private Game_Example_Model gameModel;
    private WiimoteHandler wiimoteHandler;

    public Game_Example_Controller(Game_Example_Model model, WiimoteHandler wiimoteHandler)
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
