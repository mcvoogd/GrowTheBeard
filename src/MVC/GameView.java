package MVC;

import nl.avans.a3.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameView implements ModelListener {

    private GameController controller;
    private GameModel model;
    private JFrame frame = new JFrame("Grow the Beard!");
    private PaintPanel panel;
    private GameViewInterface viewInterface;


    public GameView(GameController controller, GameModel model)
    {
        this.model = model;
        this.controller = controller;
        panel = new PaintPanel(model.getGameViewInterface());
        Timer repainter = new Timer(1000/60, e->panel.repaint());
        repainter.start();
        setViewInterface(model.getGameViewInterface());

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
            Frame frame = new Frame();
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
                Thread.sleep(1);  // keeps screen in fullscreen
            }
        }catch(Exception e){
            Logger.instance.log(e);
        }finally{
            screen.setDisplayMode(oldDisplayMode);
            screen.setFullScreenWindow(null);
        }
    }

    public void setViewInterface(GameViewInterface viewInterface) //just a setter.
    {
        this.viewInterface = viewInterface;
        panel.setGameViewInterface(viewInterface);
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

}
