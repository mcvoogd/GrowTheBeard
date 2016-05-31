package nl.avans.a3.event;

import nl.avans.a3.game_1.GameBoard;
import nl.avans.a3.util.WiimoteHandler;

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
