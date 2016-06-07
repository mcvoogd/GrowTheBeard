package nl.avans.a3.event;

import nl.avans.a3.main_menu.MainMenuModel;
import nl.avans.a3.mvc_interfaces.Model;

public class NewModel extends ModelEvent {
    public final Model oldModel, newModel;

    public NewModel(Model oldModel, Model newModel)
    {
        this.oldModel = oldModel;
        this.newModel = newModel;
    }
}
