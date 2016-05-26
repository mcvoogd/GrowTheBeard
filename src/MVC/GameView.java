package MVC;

import nl.avans.a3.Logger;

import javax.swing.*;
import java.awt.*;

public class GameView implements ModelListener {

    private GameController controller;
    private GameModel model;
    private JFrame frame = new JFrame("Grow the Beard!");
    private PaintPanel panel = new PaintPanel();
    private GameViewInterface viewInterface;


    public GameView(GameController controller, GameModel model)
    {
        this.model = model;
        this.controller = controller;
        initFrame();
        Timer repainter = new Timer(1000/60, e->panel.repaint());
        repainter.start();
        setViewInterface(model.getGameViewInterface());
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
            while(true){  // TODO fix lockup here
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


    class PaintPanel extends JPanel
{
    public void paintComponent(Graphics g2)
    {
        super.paintComponent(g2);
        Graphics2D g = (Graphics2D) g2;
        if(viewInterface != null){
            viewInterface.draw(g);
        }
    }
}

}
