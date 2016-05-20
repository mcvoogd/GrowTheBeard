package nl.avans.a3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GraphicsWindow extends JPanel{

    private Timer timer = new Timer(1000/60, e -> repaint());
    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;
    private WiimoteHandler wiimoteHandler;
    private boolean drawDebug = false;
    private boolean bootAnimation = true;
    private BootScreen bootScreen = new BootScreen();

    public GraphicsWindow(/*WiimoteHandler wiimoteHandler*/) {
        setName("Grow the Beard");
        wiimoteHandler = new WiimoteHandler();
        timer.start();
        wiimoteHandler.activateMotionSensing();

        setFocusable(true);
        requestFocus();
        addKeyListener(new KeyListener(){
            @Override
            public void keyTyped(KeyEvent e){

            }

            @Override
            public void keyPressed(KeyEvent e){
                switch(e.getKeyCode()){
                    case KeyEvent.VK_F3:
                        drawDebug = !drawDebug;
                        break;
                    case KeyEvent.VK_F5:
                        wiimoteHandler.SearchWiimotes();
                        break;
                    case KeyEvent.VK_M:
                        wiimoteHandler.activateMotionSensing();
                        break;
                    case KeyEvent.VK_ESCAPE:
                        Logger.instance.log("GW001", "Program exited by keypress", Logger.LogType.LOG);
                        System.exit(0);
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e){

            }
        });
    }

    @Override
    public void paintComponent(Graphics graphics)
    {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics;
        g.scale(getWidth()/1920.0, getHeight()/1080.0);
        
        if(bootAnimation){
            bootScreen.update(g);
        }

        // always as last
        if(drawDebug){
            wiimoteHandler.drawDebug(g);
        }

        // fixes stutter on Linux systems
        Toolkit.getDefaultToolkit().sync();
    }
}
