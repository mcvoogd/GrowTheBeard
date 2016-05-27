package MVC_V2;

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
