package nl.avans.a3;

import TimberGame.WiimoteHandler;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.font.GlyphVector;
import java.io.File;
import java.io.IOException;

public class BootScreen extends JPanel{
    private static final int AXE_THROW_SPEED = 8;
    private static final int BEARD_SPEED = 8;
    private static final int TEXT_SPEED = 2;
    private int animationTick;
    
    /**
     * Resources
     */
    private Image background;
    private Image axe;
    private Image beard;
    private Image text;

    private boolean finished = false;
    private boolean timerstarted = false;
    Shape s = null;
    private double fontsize = 0;
    private final int MAXFONT = 50;
    private final int MINFONT = 40;
    private final String TEXTAB = "Press A + B to start!";
    private final int YOFFSET = 400;

    public BootScreen(){
        animationTick = 0;
        try{
            background = ImageIO.read(new File("res/boot_bg.png"));
            axe = ImageIO.read(new File("res/boot_axe.png"));
            beard = ImageIO.read(new File("res/boot_beard.png"));
            text = ImageIO.read(new File("res/boot_text.png"));
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void update(Graphics2D g){

        g.drawImage(background, 0, 0, null);

        Font f = getFont().deriveFont(Font.BOLD, (float) fontsize);
        GlyphVector v = f.createGlyphVector(getFontMetrics(f).getFontRenderContext(), TEXTAB);
        double width = v.getPixelBounds(getFontMetrics(f).getFontRenderContext(), 0, 0).getWidth();
        s = v.getOutline((float) (GraphicsWindow.WIDTH/2 - width/2), ((GraphicsWindow.HEIGHT /2) + YOFFSET));

        if(!timerstarted) {
            timerstarted = true;
            final boolean[] triggered = {false};
            Timer t = new Timer(1000/60, e -> {

                if(fontsize < MAXFONT && !triggered[0])
                {
                    fontsize += 0.5;
                    if(fontsize >= MAXFONT)
                    {
                        triggered[0] = true;
                    }
                }
                else
                {
                    fontsize -= 0.5;
                    if(fontsize <= MINFONT)
                    {
                        triggered[0] = false;
                    }
                }
            });
            t.start();
        }
        g.setColor(Color.WHITE);
        g.fill(s);

        if(animationTick <= 720 / AXE_THROW_SPEED){
            int rotation = animationTick * AXE_THROW_SPEED;
            int x = -720 * AXE_THROW_SPEED + animationTick * AXE_THROW_SPEED * AXE_THROW_SPEED + (960 - axe.getWidth(null)/2);
            int y = 540 - axe.getHeight(null)/2;
            g.drawImage(axe, EasyTransformer.rotateAroundCenterWithOffset(axe, rotation, 100, -100, x, y), null);
        }else{
            if(animationTick <= (720 / AXE_THROW_SPEED) + beard.getHeight(null) / BEARD_SPEED){
                int clipHeight = (animationTick - 720 / AXE_THROW_SPEED) * BEARD_SPEED;
                g.setClip(860-beard.getWidth(null)/2, 610-beard.getHeight(null)/2, 860-beard.getWidth(null)/2, clipHeight);
                g.drawImage(beard, 860-beard.getWidth(null)/2, 610-beard.getHeight(null)/2, null);
                g.setClip(0, 0, 1920, 1080);
            }else{
                g.drawImage(beard, 860-beard.getWidth(null)/2, 610-beard.getHeight(null)/2, null);
                if(animationTick <= (720 / AXE_THROW_SPEED) + beard.getHeight(null) / BEARD_SPEED + 400 / TEXT_SPEED){
                    float opacity = ((animationTick - 720 / AXE_THROW_SPEED) - beard.getHeight(null) / BEARD_SPEED) / (400.0f / TEXT_SPEED);
                    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
                    g.drawImage(text, 850 - text.getWidth(null) / 2, 590 - text.getHeight(null) / 2, null);
                    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
                }else{
                    g.drawImage(text, 850 - text.getWidth(null) / 2, 590 - text.getHeight(null) / 2, null);
                }
            }
            g.drawImage(axe, 960-axe.getWidth(null)/2, 540-axe.getHeight(null)/2, null);
        }
        animationTick++;

    }
}
