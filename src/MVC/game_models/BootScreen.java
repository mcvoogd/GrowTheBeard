package MVC.game_models;

import MVC.interfaces_listener.ModelEvent;
import MVC.interfaces_listener.ModelInterface;
import MVC.interfaces_listener.ModelListener;

public class BootScreen implements ModelInterface {

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
