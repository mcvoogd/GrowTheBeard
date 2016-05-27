package MVC;

public class NewModel extends ModelEvent{

    private ModelInterface newInterface, oldInterface;

    public NewModel(ModelInterface newInterface, ModelInterface oldInterface)
    {
        this.newInterface = newInterface;
        this.oldInterface = oldInterface;
    }

    public ModelInterface getNewInterface()
    {
        return newInterface;
    }

    public ModelInterface getOldInterface()
    {
        return oldInterface;
    }


}
