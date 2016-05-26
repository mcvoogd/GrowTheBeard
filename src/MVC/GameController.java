package MVC;

import nl.avans.a3.WiimoteHandler;

import javax.swing.*;


public class GameController {

    private GameModel model;
    private WiimoteHandler handler;
    private Timer refreshTimer;

    public GameController(GameModel model)
    {
        this.model = model;
        this.handler = new WiimoteHandler();
        this.refreshTimer = new Timer(1000/60, e ->
        {
            model.getGameModelInterface().update();
            model.getGameViewInterface().update();

        });
        refreshTimer.start();
        //central timer

    }

    /**
     * Changes the model so the view changes.
     * @param model the model to change to.
     */
    public void changeModel(GameModel model)
    {
        this.model = model;
    }
}

