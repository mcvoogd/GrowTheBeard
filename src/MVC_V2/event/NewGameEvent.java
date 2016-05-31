package MVC_V2.event;

import MVC_V2.game_1.GameBoard;
import MVC_V2.util.WiimoteHandler;

import javax.swing.*;

public class NewGameEvent extends ModelEvent {

    private JPanel panel;

    public NewGameEvent(WiimoteHandler wiimoteHandler)
    {
        panel = new GameBoard(wiimoteHandler);
    }

    public JPanel getPanel()
    {
        return panel;
    }
}
