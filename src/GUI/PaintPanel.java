package GUI;

import TimberGame.WiimoteHandler;

import javax.swing.*;
import java.awt.*;

/**
 * Created by kevin on 28/04/2016.
 */
public class PaintPanel extends JPanel {
    private int startteller = 0;
    private int fontsize = 20;
    private final int MAXFONT = 30;
    private final int MINFONT = 5;
    
    private WiimoteHandler wiimoteHandler = new WiimoteHandler();
    private boolean drawDebug = true;  // TODO: I'd like a keylistener for this, F3 please


    public PaintPanel() {
        System.out.println("Paint Panel constructed");
        new Timer(10, e -> repaint()).start();
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setFont(new Font("Purisa", Font.PLAIN, fontsize));
        g2d.drawString("Timber Game", 960, 540);
        drawStart(g2d);
        
        if(drawDebug){
            wiimoteHandler.drawDebug(g2d);
        }
    }

    public void drawStart(Graphics2D g2d)
    {

    }
}
