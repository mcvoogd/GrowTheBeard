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
            double xPos = (Double.isNaN(wiimoteHandler.getSinglePointer(0).getX())) ? -100 : wiimoteHandler.getSinglePointer(0).getX() * ((1920.0 + SCREEN_OFFSET) / 1024.0) - SCREEN_OFFSET/2;
            double yPos = (Double.isNaN(wiimoteHandler.getSinglePointer(0).getY())) ? -100 : wiimoteHandler.getSinglePointer(0).getY() * ((1080.0 + SCREEN_OFFSET) / 900.0) - SCREEN_OFFSET/2;
            Point2D pointerLocation = new Point2D.Double(xPos, yPos);
            model.setPointer(pointerLocation);

            for(int i = 0; i < wiimoteHandler.numberOfWiimotesConnected(); i++){
                if (wiimoteHandler.getIsButtonPressed(i, WiimoteHandler.Buttons.KEY_HOME)) {
                    ModelHandler.instance.changeModel(new NewModel(model, new MainMenuModel()));
                }
//                if (wiimoteHandler.getIsButtonPressed(i, WiimoteHandler.Buttons.KEY_LEFT)) {
//                    model.switchMenu(-1);
//                }
//                if (wiimoteHandler.getIsButtonPressed(i, WiimoteHandler.Buttons.KEY_RIGHT)) {
//                    model.switchMenu(1);
//                }
//                if (wiimoteHandler.getIsButtonPressed(i, WiimoteHandler.Buttons.KEY_A)) {
//                    model.onMenuChoose(wiimoteHandler);
//                }

                if(wiimoteHandler.getIsButtonPressed(i, WiimoteHandler.Buttons.KEY_A)){
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
        model.update();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
        {
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
