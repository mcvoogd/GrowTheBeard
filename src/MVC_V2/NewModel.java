package MVC_V2;

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
