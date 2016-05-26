package MVC;

import nl.avans.a3.GraphicsWindow;
import nl.avans.a3.Logger;

import javax.swing.*;
import java.awt.*;

public class GameView {

    private GameController controller;
    private GameModel model;
    private JFrame frame = new JFrame("Grow the Beard!");
    private JPanel panel = new JPanel();

    public GameView(GameController controller, GameModel model)
    {
        this.model = model;
        this.controller = controller;
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


class PaintPanel extends JPanel
{
    public void paintComponent(Graphics g2)
    {
        super.paintComponent(g2);
        Graphics2D g = (Graphics2D) g2;
    }
}
}
