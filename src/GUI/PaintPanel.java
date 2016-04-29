package GUI;

import TimberGame.WiimoteHandler;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PaintPanel extends JPanel{
    private int startteller = 0;
    private int fontsize = 30;
    private final int MAXFONT = 50;
    private final int MINFONT = 20;
    private boolean timerstarted = false;
    Shape s = null;
    private BufferedImage background;
    private int fontsize = 20;
    private final int MAXFONT = 30;
    private final int MINFONT = 5;
    private Timer timer;

    private WiimoteHandler wiimoteHandler;
    private boolean drawDebug = true;  // TODO: I'd like a keylistener for this, F3 please

    public PaintPanel(WiimoteHandler wiimoteHandler) {
        System.out.println("Paint Panel constructed");
        timer = new Timer(1000/60, e -> repaint());
        timer.start();
        this.wiimoteHandler = wiimoteHandler;
        wiimoteHandler.activateMotionSensing();
        new Timer(1000/60, e -> repaint()).start();
        try {
            background = ImageIO.read(new File("start.png"));
            System.out.println("read succesvol");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if(drawDebug){
            wiimoteHandler.drawDebug(g2d);
        }
        g2d.drawImage(background, 0, 0, null);
        drawStart(g2d, "Press A + B to start");
    }

    public void drawStart(Graphics2D g2d, String text)
    {
        Font f = getFont().deriveFont(Font.BOLD, fontsize);
        GlyphVector v = f.createGlyphVector(getFontMetrics(f).getFontRenderContext(), text);
        double width = v.getPixelBounds(getFontMetrics(f).getFontRenderContext(), 0, 0).getWidth();
        s = v.getOutline((float) (getWidth()/2 - width/2), 1800 /2);

        if(!timerstarted) {
            timerstarted = true;
            final boolean[] triggered = {false};
            Timer t = new Timer(1000/30, e -> {

                if(fontsize < MAXFONT && !triggered[0])
                {
                    fontsize++;
                     if(fontsize == MAXFONT)
                    {
                        triggered[0] = true;
                    }
                }
                else
                {
                    fontsize--;
                    if(fontsize == MINFONT)
                    {
                        triggered[0] = false;
                    }
                }
           });
            t.start();
          }
       g2d.setColor(Color.GREEN);
       g2d.fill(s);

    }
}