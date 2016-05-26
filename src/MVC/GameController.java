package MVC;

import nl.avans.a3.WiimoteHandler;

public class GameController {

    private GameModel model;
    private WiimoteHandler handler;

    public GameController(GameModel model, WiimoteHandler handler)
    {
        this.model = model;
        this.handler = handler;
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

