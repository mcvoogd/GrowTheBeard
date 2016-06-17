package nl.avans.a3.game_2;

import nl.avans.a3.event.ModelEvent;
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

    private float clamp(float val, float min, float max)
    {
        if (val < min) return min;
        if (val > max) return max;
        return val;
    }

    @Override
    public void update() {
        //check wiimote stuff.
        gameModel.update();
        if (wiimoteHandler != null && wiimoteHandler.isWiiMotesConnected()) {
            gameModel.setMoveHorizontal(-clamp(wiimoteHandler.getPitch(0), -10, 10)/10, 0);
            gameModel.setMoveHorizontal(-clamp(wiimoteHandler.getPitch(1), -10, 10)/10, 1);
            gameModel.setJump(wiimoteHandler.getIsButtonPressed(0, WiimoteHandler.Buttons.KEY_A), 0);
            gameModel.setJump(wiimoteHandler.getIsButtonPressed(1, WiimoteHandler.Buttons.KEY_A), 1);
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
            case KeyEvent.VK_A : gameModel.setMoveHorizontal(-1, 0); break;
            case KeyEvent.VK_D : gameModel.setMoveHorizontal(1, 0); break;
            case KeyEvent.VK_W : gameModel.setJump(true, 0); break;
            case KeyEvent.VK_NUMPAD4 : gameModel.setMoveHorizontal(-1, 1); break;
            case KeyEvent.VK_NUMPAD6 : gameModel.setMoveHorizontal(1, 1); break;
            case KeyEvent.VK_NUMPAD8 : gameModel.setJump(true, 1); break;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode())
        {
            case KeyEvent.VK_ESCAPE :
                System.exit(0);
                break;
            case KeyEvent.VK_A : gameModel.setMoveHorizontal(0, 0); break;
            case KeyEvent.VK_D : gameModel.setMoveHorizontal(0, 0); break;
            case KeyEvent.VK_W : gameModel.setJump(false, 0); break;
            case KeyEvent.VK_NUMPAD4 : gameModel.setMoveHorizontal(0, 1); break;
            case KeyEvent.VK_NUMPAD6 : gameModel.setMoveHorizontal(0, 1); break;
            case KeyEvent.VK_NUMPAD8 : gameModel.setJump(false, 1); break;
        }

    }

    @Override
    public void onModelEvent(ModelEvent event) {

    }
}
