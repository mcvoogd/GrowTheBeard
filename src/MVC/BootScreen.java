package MVC;

public class BootScreen implements ModelInterface{

    private ModelListener listener;
    @Override
    public void update() {

    }

    @Override
    public void setListener(ModelListener listener) {
        this.listener = listener;
    }

    public void onABPressed()
    {
        if(listener != null)
        {
            listener.onModelEvent(new ModelEvent());
        }
    }
}
