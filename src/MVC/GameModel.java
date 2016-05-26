package MVC;

import java.util.ArrayList;

public class GameModel implements ModelListener{

    private GameModelInterface gameModelInterface;
    private GameViewInterface gameViewInterface;
    private ArrayList<ModelListener> modelListeners;

    public GameModel(GameModelInterface gameModelInterface, GameViewInterface gameViewInterface)
    {
        this.gameModelInterface = gameModelInterface;
        this.gameViewInterface = gameViewInterface;
    }

    public void registerModelListener(ModelListener listener)
    {
        if(listener != null && !modelListeners.contains(listener)) {
            modelListeners.add(listener);
        }
    }

    private void dispatchMessage(ModelEvent event)
    {
        modelListeners.forEach(modelListener -> modelListener.onModelEvent(event));
    }

    public void unregisterModelListener(ModelListener listener)
    {
        if(listener != null && modelListeners.contains(listener))
        {
            modelListeners.remove(listener);
        }
    }

    public void changeModel(GameModelInterface newInterface)
    {
        GameModelInterface temp = this.gameModelInterface;
        this.gameModelInterface = newInterface;
        dispatchMessage(new NewModel(newInterface, temp));
    }

    public GameViewInterface getGameViewInterface() {
        return gameViewInterface;
    }
    public GameModelInterface getGameModelInterface(){return gameModelInterface;}

    @Override
    public void onModelEvent(ModelEvent e) {
        if(e instanceof NewModel) {
            changeModel(((NewModel) e).getNewInterface());
        }
    }




}
