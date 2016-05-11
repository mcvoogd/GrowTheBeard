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
    private boolean drawDebug = false;
    private boolean bootAnimation = true;
    private BootScreen bootScreen = new BootScreen();

    public PaintPanel(WiimoteHandler wiimoteHandler) {
        timer = new Timer(1000/60, e -> {
            repaint();
            startCounter++;
            if(startCounter > 1000) {setStartmenuactive(false);} // SIMULATE PRESSING A + B
        });
        timer.start();
        this.wiimoteHandler = wiimoteHandler;
        try {
            System.out.println("Loading resources...");
            background = ImageIO.read(new File("start.png"));
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
                        System.exit(1);
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e){
                
            }
        });
    }

    public void paintComponent(Graphics graphics)
    {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics;
        
        if(bootAnimation){
            bootScreen.update(g);
        }
        
        if(startmenuactive) {
            //g.drawImage(background, 0, 0, null);
            drawStart(g, "Press A + B to start");
        }
        
        // always draw debug last
        if(drawDebug){
            wiimoteHandler.drawDebug(g);
        }

        // fixes stutter on Linux systems
        Toolkit.getDefaultToolkit().sync();
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