package nl.avans.a3.boot_menu;

import nl.avans.a3.main_menu.MainMenuModel;
import nl.avans.a3.mvc_interfaces.Model;
import nl.avans.a3.mvc_handlers.ModelHandler;
import nl.avans.a3.event.NewModel;

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
