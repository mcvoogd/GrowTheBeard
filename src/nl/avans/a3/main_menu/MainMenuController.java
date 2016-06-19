package nl.avans.a3.main_menu;

import nl.avans.a3.event.ModelEvent;
import nl.avans.a3.event.NewModel;
import nl.avans.a3.game_3.Game_3_Model;
import nl.avans.a3.mvc_handlers.ModelHandler;
import nl.avans.a3.mvc_interfaces.Controller;
import nl.avans.a3.util.WiimoteHandler;

import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;

public class MainMenuController implements Controller {
    private static final double SCREEN_OFFSET = 400.0;
    private MainMenuModel model;
    private WiimoteHandler wiimoteHandler;
    private boolean tried = false;

    public MainMenuController(MainMenuModel model, WiimoteHandler wiimoteHandler)
    {
        this.model = model;
        this.wiimoteHandler = wiimoteHandler;
        wiimoteHandler.activateMotionSensing();
    }

    @Override
    public void update() {
        model.update();
        if (wiimoteHandler.isWiiMotesConnected()) {
            for(int i = 0 ; i < wiimoteHandler.numberOfWiimotesConnected(); i++) {
                if (wiimoteHandler.getIsButtonPressed(i, WiimoteHandler.Buttons.KEY_A)) {;
                    model.onMenuChoose(wiimoteHandler);
                    wiimoteHandler.activateRumble(0);
                }
            }
            wiimoteHandler.deactivateRumble(0);
            double xPos = (Double.isNaN(wiimoteHandler.getSinglePointer(0).getX())) ? -100 : wiimoteHandler.getSinglePointer(0).getX() * ((1920.0 + SCREEN_OFFSET) / 1024.0) - SCREEN_OFFSET / 2;
            double yPos = (Double.isNaN(wiimoteHandler.getSinglePointer(0).getY())) ? -100 : wiimoteHandler.getSinglePointer(0).getY() * ((1080.0 + SCREEN_OFFSET) / 900.0) - SCREEN_OFFSET / 2;
            Point2D pointerLocation = new Point2D.Double(xPos, yPos);
            model.setPointer(pointerLocation);
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
            // comments are for debug.
//            case KeyEvent.VK_H: ModelHandler.instance.onModelEvent(new NewModel(null, new Game_3_Model())); break;
//            case KeyEvent.VK_G: model.onMenuChoose(wiimoteHandler); break;
//            case KeyEvent.VK_W : model.pointToTop(); break;
//            case KeyEvent.VK_S : model.pointToBottem(); break;
//            case KeyEvent.VK_A : model.pointToLeft(); break;
//            case KeyEvent.VK_D : model.pointToRight(); break;
//            case KeyEvent.VK_B : model.changeMode(MainMenuModel.Mode.CHOOSE_PARTY); model.onMenuChoose(wiimoteHandler); break;
         }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void onModelEvent(ModelEvent event) {

    }

}
