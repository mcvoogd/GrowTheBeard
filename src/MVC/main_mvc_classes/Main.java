package MVC.main_mvc_classes;

import MVC.game_models.BootScreen;
import MVC.game_views.BootScreenView;
import nl.avans.a3.Logger;

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
