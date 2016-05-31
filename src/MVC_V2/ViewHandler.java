package MVC_V2;

import nl.avans.a3.Logger;

import javax.swing.*;
import java.awt.*;

public class ViewHandler implements ModelListener {
    private View view;
    private JFrame frame;
    private Timer repainter = new Timer(1000/60, e -> frame.repaint());

    public ViewHandler(ControllerHandler controllerHandler)
    {
        ModelHandler.instance.addListener(this);
        Logger.instance.log("VH003", "ViewHandler created", Logger.LogType.DEBUG);
        try{

            frame = new JFrame("Grow the Beard");
            frame.setAutoRequestFocus(true);
            frame.setUndecorated(true);
            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
            frame.toFront();
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice screen = ge.getDefaultScreenDevice();

            DisplayMode oldDisplayMode = screen.getDisplayMode();
            DisplayMode newDisplayMode = new DisplayMode(screen.getDisplayMode().getWidth(), screen.getDisplayMode().getHeight(), screen.getDisplayMode().getBitDepth(), screen.getDisplayMode().getRefreshRate());

            if(!screen.isFullScreenSupported()){
                Logger.instance.log("MN001", "Fullscreen unsupported on this device", Logger.LogType.ERROR);
                System.exit(1);
            }
            screen.setFullScreenWindow(frame);
            screen.setDisplayMode(newDisplayMode);
            frame.addKeyListener(controllerHandler);
            frame.setContentPane(new JPanel(){
                @Override
                protected void paintComponent(Graphics g2) {
                    super.paintComponent(g2);
                    Graphics2D g = (Graphics2D) g2;
                    RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g.setRenderingHints(renderingHints);
                    g.scale(getWidth()/1920.0, getHeight()/1080.0);
    
                    if (view != null) view.draw(g);
    
                    // fixes stutter on Linux systems
                    Toolkit.getDefaultToolkit().sync();
                }
            });
            frame.setVisible(true);
        }catch(Exception e) {
            Logger.instance.log(e);
        }
    }

    @Override
    public void onModelEvent(ModelEvent event) {
        if (event instanceof NewModel){
            if (view != null) view.close();
            view = selectedView(((NewModel)event).newModel);
            Logger.instance.log("VH001", "new view ("+view.getClass().getName()+") has been loaded", Logger.LogType.DEBUG);
            view.start();
        }else{
            if (view != null) view.onModelEvent(event);
        }

        if(event instanceof NewGameEvent)
        {
            frame.setContentPane(((NewGameEvent) event).getPanel());
        }
    }

    private static View selectedView(Model model){
        if(model instanceof BootModel){
            return new BootView();
        }
        if(model instanceof MainMenuModel){
            return new MainMenuView();
        }

        return null;
    }

    public void startTimer(){
        Logger.instance.log("VH002", "timer started", Logger.LogType.LOG);
        repainter.start();
    }
}
