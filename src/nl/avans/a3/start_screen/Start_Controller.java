package nl.avans.a3.start_screen;

import nl.avans.a3.event.ModelEvent;
import nl.avans.a3.mvc_interfaces.Controller;
import nl.avans.a3.party_mode_handler.PartyModeHandler;
import nl.avans.a3.util.WiimoteHandler;

import java.awt.event.KeyEvent;

public class Start_Controller implements Controller {
    WiimoteHandler handler;

    public Start_Controller(WiimoteHandler handler)
    {
        this.handler = handler;
    }
    @Override
    public void update() {
        if(handler.isWiiMotesConnected())
        {
            for(int i = 0; i < handler.numberOfWiimotesConnected(); i++)
            {
                if(handler.getIsButtonPressed(i, WiimoteHandler.Buttons.KEY_A))
                {
                    PartyModeHandler.notifyNextGame();
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
