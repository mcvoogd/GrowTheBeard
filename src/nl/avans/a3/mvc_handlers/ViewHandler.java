package nl.avans.a3.mvc_handlers;


import nl.avans.a3.boot_menu.BootModel;
import nl.avans.a3.boot_menu.BootView;
import nl.avans.a3.event.ModelEvent;
import nl.avans.a3.event.NewGameEvent;
import nl.avans.a3.event.NewModel;
import nl.avans.a3.game_example.Game_Example_Model;
import nl.avans.a3.game_example.Game_Example_View;
import nl.avans.a3.main_menu.MainMenuModel;
import nl.avans.a3.main_menu.MainMenuView;
import nl.avans.a3.mvc_interfaces.Model;
import nl.avans.a3.mvc_interfaces.ModelListener;
import nl.avans.a3.mvc_interfaces.View;
import nl.avans.a3.util.Logger;

import javax.swing.*;
import java.awt.*;

public class ViewHandler implements ModelListener{
    private View view;
    private JFrame frame;
    private JPanel panel;
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
            panel = new PaintPanel();
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
            frame.setContentPane(panel);
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
            frame.repaint();
            frame.invalidate();
            frame.revalidate();
        }

    }

    private static View selectedView(Model model){
        if(model instanceof BootModel){
            return new BootView();
        }
        if(model instanceof MainMenuModel){
            return new MainMenuView();
        }
        if(model instanceof Game_Example_Model)
        {
            return new Game_Example_View((Game_Example_Model) model);
        }

        return null;
    }

    public void startTimer(){
        Logger.instance.log("VH002", "timer started", Logger.LogType.LOG);
        repainter.start();
    }

    class PaintPanel extends JPanel
    {
        public void paintComponent(Graphics g2)
        {
            super.paintComponent(g2);
            Graphics2D g = (Graphics2D) g2;
            RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHints(renderingHints);
            g.scale(getWidth()/1920.0, getHeight()/1080.0);

            if (view != null) view.draw(g);
             // fixes stutter on Linux systems
            Toolkit.getDefaultToolkit().sync();
        }
    }
}
