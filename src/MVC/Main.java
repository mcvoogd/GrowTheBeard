package MVC;

import nl.avans.a3.Logger;
import nl.avans.a3.WiimoteHandler;

public class Main {

   private WiimoteHandler wiimoteHandler;
   private GameModel model;
   private GameController controller;
   private GameView view;


    public static void main(String[] args) {
        new Main();
    }

    /**
     * Create MVC.
     * Init logger.
     */
    public Main()
    {
        Logger.init();
        model = new GameModel(new BootScreen(), new BootScreenView());
        controller = new GameController(model);
        view = new GameView(controller, model);
    }


}
