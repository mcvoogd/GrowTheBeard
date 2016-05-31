package MVC_V2;

import java.awt.event.KeyEvent;

public class WoodDodgingController implements Controller {

    private Model model;
    private WiimoteHandler wiimoteHandler;

    public WoodDodgingController(WoodDodgingModel model, WiimoteHandler wiimoteHandler) {
        this.model = model;
        this.wiimoteHandler = wiimoteHandler;
    }

    @Override
    public void update() {

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
