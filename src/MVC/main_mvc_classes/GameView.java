package MVC.main_mvc_classes;

import MVC.game_models.MainMenu;
import MVC.game_views.MainMenuView;
import MVC.interfaces_listener.ModelEvent;
import MVC.interfaces_listener.ModelListener;
import MVC.interfaces_listener.NewModel;
import MVC.interfaces_listener.ViewInterface;
import nl.avans.a3.Logger;

import javax.swing.*;
import java.awt.*;

public class GameView implements ModelListener {

    private GameController controller;
    private GameModel model;
    private PaintPanel panel;
    private ViewInterface viewInterface;
    private Frame frame;


    public GameView(GameController controller, GameModel model)
    {
        frame = new Frame();
        frame.addKeyListener(controller);
        this.model = model;
        this.controller = controller;
        panel = new PaintPanel(model.getViewInterface());
        Timer repainter = new Timer(1000/60, e->panel.repaint());
        repainter.start();
        setViewInterface(model.getViewInterface());
        model.registerModelListener(this);
        initFrame();

    }

    private void initFrame() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice screen = ge.getDefaultScreenDevice();

        DisplayMode oldDisplayMode = screen.getDisplayMode();
        DisplayMode newDisplayMode = new DisplayMode(screen.getDisplayMode().getWidth(),
                screen.getDisplayMode().getHeight(), screen.getDisplayMode().getBitDepth(),
                screen.getDisplayMode().getRefreshRate());

        if(!screen.isFullScreenSupported()){
            Logger.instance.log("MN001", "Fullscreen unsupported on this device", Logger.LogType.ERROR);
            System.exit(1);
        }

        try{
            frame.addKeyListener(controller);
            frame.add(panel);
            frame.setTitle("Grow the Beard");
            frame.setAutoRequestFocus(true);
            frame.setUndecorated(true);
            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
            frame.toFront();
            screen.setFullScreenWindow(frame);
            screen.setDisplayMode(newDisplayMode);
            //noinspection InfiniteLoopStatement
            while(true){
                Thread.sleep(100);  // keeps screen in fullscreen
            }
        }catch(Exception e){
            Logger.instance.log(e);
        }finally{
            screen.setDisplayMode(oldDisplayMode);
            screen.setFullScreenWindow(null);
        }
    }

    public void setViewInterface(ViewInterface viewInterface) //just a setter.
    {
        this.viewInterface = viewInterface;
        panel.setViewInterface(viewInterface);
    }

    @Override
    public void onModelEvent(ModelEvent e) {
        if(e instanceof NewModel)
        {
            NewModel newModel = (NewModel) e;
            if(newModel.getNewInterface() instanceof MainMenu)
            {
                setViewInterface(new MainMenuView());
            }
        }
    }

    public Frame getFrame()
    {
        return frame;
    }

}
