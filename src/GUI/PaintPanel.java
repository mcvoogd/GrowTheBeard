package GUI;

import TimberGame.WiimoteHandler;
import nl.avans.a3.BootScreen;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
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
    private Timer fonttimer;
    private boolean startmenuactive = true;
    private boolean choosemenuactive = false;
    private WiimoteHandler wiimoteHandler;
    private boolean drawDebug = false;
    private int startteller = 0;
    private boolean bootAnimation = true;
    private BootScreen bootScreen = new BootScreen();

    public PaintPanel(WiimoteHandler wiimoteHandler) {
        System.out.println("Paint Panel constructed");
        timer = new Timer(1000/60, e -> {
            repaint();
            startteller++;
            if(startteller > 200) {setStartmenuactive(false);} // SIMULATE PRESSING A + B

        });
        timer.start();
        this.wiimoteHandler = wiimoteHandler;
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
                        System.exit(1);
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

        if(bootAnimation){
            bootScreen.update(g);
        }

        if(startmenuactive) {
          //  g.drawImage(background, 0, 0, null);
            drawStart(g, "Press A + B to start");
        }else if(choosemenuactive){
            fonttimer.stop();
            drawChooseMenu(g);
        }
        // always as last
        if(drawDebug){
            wiimoteHandler.drawDebug(g);
        }

        // fixes stutter on Linux systems
        Toolkit.getDefaultToolkit().sync();
    }

    public void drawChooseMenu(Graphics2D g2d)  // https://youtu.be/H07zYvkNYL8?t=3s
    {
        // https://youtu.be/H07zYvkNYL8?t=3s
        //screensize will be 1920x1080. // too bad you can't ask java the screen size.
                                        //
                                        //
                                        // no wait... you can.
        System.out.println(getWidth());
        System.out.println(getHeight());
        System.out.println("WOOOO MAGIC NUMBERS!");
        Shape /*trololo*/ single = new Rectangle2D.Double(50, 50, 885, 450);  // https://youtu.be/H07zYvkNYL8?t=3s
        System.out.println("SO MAGIC! SUCH UNBELIEVABLE!");
        /* COMMENTS, COMMENTS EVERYWHERE! */ Shape multi = new Rectangle2D.Double(1920- /* fuck you */ 100-855, 50, 885, 450);
        System.out.println("Huehuehue i'm too lazy to use proper readable code");
        System.out.println("but here's a star for at least trying:\n" + 
                "                .\n" +
                "               ,O,\n" +
                "              ,OOO,\n" +
                "        'oooooOOOOOooooo'\n" +
                "          `OOOOOOOOOOO`\n" +
                "            `OOOOOOO`\n" +
                "            OOOO'OOOO\n" +
                "           OOO'   'OOO\n" +
                "          O'         'O");
        Shape scoreboard /*lmao*/ = new Rectangle2D.Double(50, 1080-465-100, 885, 450);  // too bad for you, i'm raping your code
        System.out.println("\r\n\r\nMy face when seeing this~");
        System.out.println("https://youtu.be/H07zYvkNYL8?t=3s\r\n\r\n");
        /* One does not simply use magic numbers */Shape gallery = /*lel*/ new Rectangle2D.Double(1920-100-855, 1080-465-100, 885, 450);  //https://youtu.be/H07zYvkNYL8?t=3s

        g2d.fill(single);
        g2d.fill(multi);
        g2d.fill(scoreboard);
        g2d.fill(gallery);

        g2d.setColor(Color.YELLOW);
        g2d.drawString("SinglePlayer!", (float)(single.getBounds().getWidth()/2), (float) (single.getBounds().getHeight()/2));
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
        choosemenuactive = !active;
    }

    @Override
    public void actionPerformed(ActionEvent e){

    }
}