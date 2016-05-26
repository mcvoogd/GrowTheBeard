package MVC;

public class NewModel extends ModelEvent{

    private GameModelInterface newInterface, oldInterface;

    public NewModel(GameModelInterface newInterface, GameModelInterface oldInterface)
    {
        this.newInterface = newInterface;
        this.oldInterface = oldInterface;
    }

    public GameModelInterface getNewInterface()
    {
        return newInterface;
    }

    public GameModelInterface getOldInterface()
    {
        return oldInterface;
    }


}
