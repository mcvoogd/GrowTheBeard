package nl.avans.a3.single_menu;


import nl.avans.a3.event.ModelEvent;
import nl.avans.a3.event.NewModel;
import nl.avans.a3.main_menu.MainMenuModel;
import nl.avans.a3.mvc_handlers.ModelHandler;
import nl.avans.a3.mvc_interfaces.Controller;
import nl.avans.a3.util.WiimoteHandler;

import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;

import static nl.avans.a3.single_menu.SingleMenuModel.Mode.WOOD_CHOPPING;

public class SingleMenuController implements Controller {

    private static final double SCREEN_OFFSET = 200.0;
    private SingleMenuModel model;
    private WiimoteHandler wiimoteHandler;
    private Point2D pointerLocation;

    public SingleMenuController(SingleMenuModel model, WiimoteHandler wiimoteHandler){
        this.model = model;
        this.wiimoteHandler = wiimoteHandler;
        wiimoteHandler.activateMotionSensing();
    }

    @Override
    public void update() {
        if(wiimoteHandler.isWiiMotesConnected()) {
            pointerLocation = new Point2D.Double(wiimoteHandler.getCenteredPointer(0).getX()*((1920.0 + SCREEN_OFFSET)/1024.0) - SCREEN_OFFSET/2, wiimoteHandler.getCenteredPointer(0).getY()*((1080.0 + SCREEN_OFFSET)/900.0) - SCREEN_OFFSET/2);
            model.setPointer(pointerLocation);

            if (wiimoteHandler.getIsButtonPressed(0, WiimoteHandler.Buttons.KEY_HOME) || wiimoteHandler.getIsButtonPressed(1, WiimoteHandler.Buttons.KEY_HOME)) {
                ModelHandler.instance.changeModel(new NewModel(model, new MainMenuModel()));
            }
            if (wiimoteHandler.getIsButtonPressed(0, WiimoteHandler.Buttons.KEY_LEFT) || wiimoteHandler.getIsButtonPressed(1, WiimoteHandler.Buttons.KEY_LEFT)) {
                model.switchMenu(-1);
            }
            if (wiimoteHandler.getIsButtonPressed(0, WiimoteHandler.Buttons.KEY_RIGHT) || wiimoteHandler.getIsButtonPressed(1, WiimoteHandler.Buttons.KEY_RIGHT)) {
                model.switchMenu(1);
            }
            if (wiimoteHandler.getIsButtonPressed(0, WiimoteHandler.Buttons.KEY_A) || wiimoteHandler.getIsButtonPressed(1, WiimoteHandler.Buttons.KEY_A)) {
                model.onMenuChoose(wiimoteHandler);
            }

            if(wiimoteHandler.getIsButtonPressed(0, WiimoteHandler.Buttons.KEY_A) || wiimoteHandler.getIsButtonPressed(1, WiimoteHandler.Buttons.KEY_A))

            {
                switch (model.getMode()) {
                    case WOOD_CHOPPING:
                        model.onMenuChoose(wiimoteHandler);
                    case WOOD_DODGING:
                        model.onMenuChoose(wiimoteHandler);
                    case WOOD_JUMPING:
                        model.onMenuChoose(wiimoteHandler);
                    case MAINMENU:
                        model.onMenuChoose(wiimoteHandler);
                }
            }
        }
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
