package MVC.main_mvc_classes;

import MVC.interfaces_listener.*;

import java.util.ArrayList;

public class GameModel implements ModelListener {

    private ModelInterface modelInterface;
    private ViewInterface viewInterface;
    private ArrayList<ModelListener> modelListeners = new ArrayList<>();

    public GameModel(ModelInterface modelInterface, ViewInterface viewInterface)
    {
        this.modelInterface = modelInterface;
        this.viewInterface = viewInterface;
    }

    public void registerModelListener(ModelListener listener)
    {
        if(listener != null && !modelListeners.contains(listener)) {
            modelListeners.add(listener);
        }
    }

    private void dispatchMessage(ModelEvent event)
    {
        if(!modelListeners.isEmpty()) {
            modelListeners.forEach(modelListener -> modelListener.onModelEvent(event));
            System.out.println("listeners not empty!");
        }
    }

    public void unregisterModelListener(ModelListener listener)
    {
        if(listener != null && modelListeners.contains(listener))
        {
            modelListeners.remove(listener);

        }
    }

    public void changeModel(ModelInterface newInterface)
    {
        ModelInterface temp = this.modelInterface;
        this.modelInterface = newInterface;
        dispatchMessage(new NewModel(newInterface, temp));
    }

    public ViewInterface getViewInterface() {
        return viewInterface;
    }
    public ModelInterface getModelInterface(){return modelInterface;}




    @Override
    public void onModelEvent(ModelEvent e) {
        if(e instanceof NewModel) {
            changeModel(((NewModel) e).getNewInterface());
        }
    }
}
