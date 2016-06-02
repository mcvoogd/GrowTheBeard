package nl.avans.a3.game_3;

import nl.avans.a3.event.ModelEvent;
import nl.avans.a3.mvc_interfaces.Controller;
import nl.avans.a3.util.WiimoteHandler;

import java.awt.event.KeyEvent;

public class Game_3_Controller implements Controller{

    private Game_3_Model gameModel;
    private WiimoteHandler wiimoteHandler;

    public Game_3_Controller(Game_3_Model gameModel, WiimoteHandler wiimoteHandler){
        this.gameModel = gameModel;
        this.wiimoteHandler = wiimoteHandler;
        wiimoteHandler.activateMotionSensing();
    }

    @Override
    public void update() {
        gameModel.update();
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

    @Override
    public void onModelEvent(ModelEvent event) {

    }
}
