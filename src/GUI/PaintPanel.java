package GUI;

import TimberGame.WiimoteHandler;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PaintPanel extends JPanel implements ActionListener{
    private int startCounter = 0;
    private boolean timerstarted = false;
    Shape s = null;
    private BufferedImage background;
    private double fontsize = 0;
    private final int MAXFONT = 50;
    private final int MINFONT = 40;
    private Timer timer;
    private boolean startmenuactive = true;
    private WiimoteHandler wiimoteHandler;
    private boolean drawDebug = false;  // TODO: I'd like a keylistener for this, F3 please

    public PaintPanel(WiimoteHandler wiimoteHandler) {
        System.out.println("Paint Panel constructed");
        timer = new Timer(1000/60, e -> {
            repaint();
            startCounter++;
            if(startCounter > 1000) {setStartmenuactive(false);} // SIMULATE PRESSING A + B
        });
        timer.start();
        this.wiimoteHandler = wiimoteHandler;
        try {
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
                }
            }

            @Override
            public void keyReleased(KeyEvent e){
                
            }
        });
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;


        if(startmenuactive) {
            g2d.drawImage(background, 0, 0, null);
            drawStart(g2d, "Press A + B to start");
        }
        
        // always as last
        if(drawDebug){
            wiimoteHandler.drawDebug(g2d);
        }
    }

    public void drawStart(Graphics2D g2d, String text)
    {
        Font f = getFont().deriveFont(Font.BOLD, (float) fontsize);
        GlyphVector v = f.createGlyphVector(getFontMetrics(f).getFontRenderContext(), text);
        double width = v.getPixelBounds(getFontMetrics(f).getFontRenderContext(), 0, 0).getWidth();
        s = v.getOutline((float) (getWidth()/2 - width/2), 1800 /2);

        if(!timerstarted) {
            timerstarted = true;
            final boolean[] triggered = {false};
            Timer t = new Timer(1000/60, e -> {

                if(fontsize < MAXFONT && !triggered[0])
                {
                    fontsize += 0.3;
                     if(fontsize >= MAXFONT)
                    {
                        triggered[0] = true;
                    }
                }
                else
                {
                    fontsize -= 0.3;
                    if(fontsize <= MINFONT)
                    {
                        triggered[0] = false;
                    }
                }
           });
            t.start();
          }
       g2d.setColor(Color.WHITE);
       g2d.fill(s);
    }

    public void setStartmenuactive(boolean active) {
        startmenuactive = active;
    }

    @Override
    public void actionPerformed(ActionEvent e){
        
    }
}