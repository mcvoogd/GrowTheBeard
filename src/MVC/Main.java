package MVC;

import nl.avans.a3.Logger;
import nl.avans.a3.WiimoteHandler;

public class Main {
    public static void main(String[] args) {
        new Main();
    }

    /**
     * Create MVC
     * Init logger
     */
    public Main()
    {
        Logger.init();
        GameModel model = new GameModel(new BootScreen(), new BootScreenView());
        GameController controller = new GameController(model);
        GameView view = new GameView(controller, model);
    }
}
