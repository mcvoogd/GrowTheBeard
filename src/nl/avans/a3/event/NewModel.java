package nl.avans.a3.event;

import nl.avans.a3.mvc_interfaces.Model;

/**
 * Created by FlorisBob on 27-May-16.
 */
public class NewModel extends ModelEvent {
    public final Model oldModel, newModel;

    public NewModel(Model oldModel, Model newModel)
    {
        this.oldModel = oldModel;
        this.newModel = newModel;
    }
}
