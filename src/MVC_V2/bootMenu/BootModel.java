package MVC_V2.bootMenu;

import MVC_V2.mainMenu.MainMenuModel;
import MVC_V2.mvcInterfaces.Model;
import MVC_V2.mvcHandlers.ModelHandler;
import MVC_V2.event.NewModel;

public class BootModel implements Model
{
    @Override
    public void start() {

    }

    @Override
    public void update() {

    }

    @Override
    public void close() {

    }

    public void onABPressed()
    {
        ModelHandler.instance.onModelEvent(new NewModel(this, new MainMenuModel()));
    }
}
