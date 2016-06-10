package nl.avans.a3.mvc_handlers;

import nl.avans.a3.game_2.Game_2_Model;
import nl.avans.a3.game_3.Game_3_Model;
import nl.avans.a3.main_menu.MainMenuModel;
import nl.avans.a3.start_screen.Start_Model;
import nl.avans.a3.util.Logger;
import nl.avans.a3.event.ModelEvent;
import nl.avans.a3.mvc_interfaces.ModelListener;
import nl.avans.a3.event.NewModel;
import nl.avans.a3.boot_menu.BootModel;
import nl.avans.a3.mvc_interfaces.Model;


import java.util.ArrayList;


public class ModelHandler implements ModelListener{
    public static final boolean DEBUG_MODE = true;

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

    private Model model = null;

    public void start()
    {
        //start with boot.
        changeModel(new NewModel(null, new BootModel()));
    }

    public void changeModel(NewModel event)
    {
        if(event.newModel != null) {
                Logger.instance.log("MH001", "model changed from (" + ((event.oldModel == null) ? "" : event.oldModel.getClass().getName()) + ") to (" + event.newModel.getClass().getName() + ")", Logger.LogType.DEBUG);
                model = event.newModel;
            }
        if(event.oldModel != null)
        {
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
