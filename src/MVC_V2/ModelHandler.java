package MVC_V2;

import com.sun.org.apache.xpath.internal.operations.Mod;

import java.util.ArrayList;

/**
 * Created by FlorisBob on 27-May-16.
 */
public class ModelHandler implements ModelListener {
    public ArrayList<ModelListener> listeners = new ArrayList<>();
    public void addListener(ModelListener listener) {if (listener != null && listeners.contains(listener) == false) listeners.add(listener);}
    public void removeListener(ModelListener listener) {if (listener != null && listeners.contains(listener)) listeners.remove(listener);}
    private void dispatchEvent(ModelEvent event) {listeners.forEach(modelListener -> modelListener.onModelEvent(event));}

    public static ModelHandler instance = new ModelHandler();

    private Model model;

    public void start()
    {
        //start with boot.
        changeModel(new NewModel(null, new BootModel()));
    }

    public void changeModel(NewModel event)
    {
        model = event.newModel;
        if(event.oldModel != null) {
            event.oldModel.close();
        }
            dispatchEvent(event);
        model.start();
    }

    @Override
    public void onModelEvent(ModelEvent event) {
        if (event instanceof NewModel)
            changeModel((NewModel)event);
        else
            dispatchEvent(event);
    }
}
