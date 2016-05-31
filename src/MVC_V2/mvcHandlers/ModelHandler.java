package MVC_V2.mvcHandlers;

import MVC_V2.util.Logger;
import MVC_V2.event.ModelEvent;
import MVC_V2.mvcInterfaces.ModelListener;
import MVC_V2.event.NewModel;
import MVC_V2.bootMenu.BootModel;
import MVC_V2.mvcInterfaces.Model;


import java.util.ArrayList;

/**
 * Created by FlorisBob on 27-May-16.
 */
public class ModelHandler implements ModelListener {
    public ArrayList<ModelListener> listeners = new ArrayList<>();
    public void addListener(ModelListener listener) {
        if (listener != null && listeners.contains(listener) == false) {
            listeners.add(listener);
            Logger.instance.log("MH002", Thread.currentThread().getStackTrace()[2].getClassName() + " is attached to ModelHandler", Logger.LogType.DEBUG);
        }
    }
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
        Logger.instance.log("MH001", "model changed from ("+((event.oldModel == null) ? "" : event.oldModel.getClass().getName())+") to ("+event.newModel.getClass().getName()+")", Logger.LogType.DEBUG);
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
