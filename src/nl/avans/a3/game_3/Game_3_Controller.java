package nl.avans.a3.game_3;

import nl.avans.a3.event.ModelEvent;
import nl.avans.a3.mvc_interfaces.Controller;
import nl.avans.a3.util.WiimoteHandler;

import java.awt.event.KeyEvent;

public class Game_3_Controller implements Controller{

    private Game_3_Model gameModel;
    private WiimoteHandler wiimoteHandler;
    private boolean hitPlayer1, hitPlayer2;

    public Game_3_Controller(Game_3_Model gameModel, WiimoteHandler wiimoteHandler){
        this.gameModel = gameModel;
        this.wiimoteHandler = wiimoteHandler;
        this.wiimoteHandler.activateMotionSensing();
    }

    @Override
    public void update() {
        gameModel.update();
        if (wiimoteHandler != null && wiimoteHandler.isWiiMotesConnected()) {
            float pitch =  wiimoteHandler.getPitch(0);
//            System.out.println(pitch);

        }

        if(wiimoteHandler.getPeakValue(0)[0]) {
            if(hitPlayer1){
                gameModel.damageTree(0);
                hitPlayer1 = false;
            }
        }

        if(wiimoteHandler.getPeakValue(1)[0]) {
            if(hitPlayer2){
                gameModel.damageTree(1);
                hitPlayer2 = false;
            }
        }
        float pitch1 = wiimoteHandler.getPitch(0);
        if(pitch1 < -80 && pitch1 > -100){
            hitPlayer1 = true;
        }

        float pitch2 = wiimoteHandler.getPitch(1);
        if(pitch2 < -80 && pitch2 > -100){
            hitPlayer2 = true;
        }

        //System.out.println(wiimoteHandler.getWiimoteGForceY(0));
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            System.exit(0);
        }
        if(e.getKeyCode() == KeyEvent.VK_P){
            gameModel.damageTree(0);
        }
        if(e.getKeyCode() == KeyEvent.VK_C){
            gameModel.damageTree(1);
        }
        if(e.getKeyCode() == KeyEvent.VK_S)
        {
            gameModel.damageTree(0);
            gameModel.damageTree(1);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void onModelEvent(ModelEvent event) {

    }
}
