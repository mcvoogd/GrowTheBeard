package nl.avans.a3;

import Support.Logger;
import TimberGame.WiimoteHandler;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GraphicsWindow extends JPanel implements ActionListener{
    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;
    
    private int startCounter = 0;
    private boolean timerstarted = false;
    Shape s = null;
    private BufferedImage background;
    private double fontsize = 0;
    private final int MAXFONT = 50;
    private final int MINFONT = 40;
    private Timer timer;
    private Timer fonttimer;
    private boolean startmenuactive = true;
    private boolean choosemenuactive = false;
    private WiimoteHandler wiimoteHandler;
    private boolean drawDebug = false;
    private int startteller = 0;
    private boolean bootAnimation = true;
    private BootScreen bootScreen = new BootScreen();

    public GraphicsWindow(/*WiimoteHandler wiimoteHandler*/) {
        setName("Grow the Beard");
        wiimoteHandler = new WiimoteHandler();
        timer = new Timer(1000/60, e -> {
            repaint();
            startteller++;
            if(startteller > 200) {setStartmenuactive(false);} // SIMULATE PRESSING A + B

        });
        timer.start();
        wiimoteHandler.activateMotionSensing();
        try {
            System.out.println("Loading resources...");
            background = ImageIO.read(new File("start.png"));
            System.out.println("read succesvol");
        } catch (IOException e) {
            e.printStackTrace();
        }

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
    public void setStartmenuactive(boolean active) {
        startmenuactive = active;
        choosemenuactive = !active;
    }

    @Override
    public void actionPerformed(ActionEvent e){
    }
}
