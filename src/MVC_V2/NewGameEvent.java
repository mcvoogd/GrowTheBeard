package MVC_V2;

import MVC_V2.Game_01.GameBoard;

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
