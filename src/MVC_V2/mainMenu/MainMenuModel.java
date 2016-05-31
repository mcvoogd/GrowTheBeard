package MVC_V2.mainMenu;

import MVC_V2.event.NewModel;
import MVC_V2.game_Example.Game_Example_Model;
import MVC_V2.mvcHandlers.ModelHandler;
import MVC_V2.mvcInterfaces.Model;

public class MainMenuModel implements Model {
    @Override
    public void start() {

    }

    @Override
    public void update() {

    }

    @Override
    public void close() {

    }

    public void onMenuChoose()
    {
        ModelHandler.instance.onModelEvent(new NewModel(this, new Game_Example_Model()));
    }
}
