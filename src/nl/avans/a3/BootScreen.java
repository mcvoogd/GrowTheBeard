package nl.avans.a3;


import com.sun.corba.se.impl.orbutil.graph.Graph;

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
    private Image logoText;
    private Image text;

    private boolean timerstarted = false;
    Shape s = null;
    private double fontsize = 0;
    private static final double MAXFONT = 1.5;
    private static final double MINFONT = 1.0;
    private static final String TEXTAB = "Press A + B to start!";
    private static final int YOFFSET = 400;
    private static final int BEARDOFFSET = 860;
    private static final int BEARDHEIGHTOFFSET = 610;
    private static final int TEXTOFFSET = 850;
    private static final int TEXTHEIGHTOFFSET = 590;
    private static final int DOUBLEROTATION = 720;
    
    public BootScreen(){
        animationTick = 0;
        try{
            background = ImageIO.read(new File("res/splash/background.png"));
            axe = ImageIO.read(new File("res/splash/axe.png"));
            beard = ImageIO.read(new File("res/splash/beard.png"));
            logoText = ImageIO.read(new File("res/splash/logo_text.png"));
            text = ImageIO.read(new File("res/splash/text.png"));
        }catch(IOException e){
            Logger.instance.log(e);
        }
    }

    public void update(Graphics2D g){

        g.drawImage(background, 0, 0, null);

        g.drawImage(text, EasyTransformer.scaleImageFromCenter(text, fontsize, GraphicsWindow.WIDTH/2 - text.getWidth(null)/2, GraphicsWindow.HEIGHT/2 + YOFFSET), null);
        if(!timerstarted) {
            timerstarted = true;
            final boolean[] triggered = {false};
            Timer t = new Timer(1000/60, e -> {

                if(fontsize < MAXFONT && !triggered[0])
                {
                    fontsize += 0.01;
                    if(fontsize >= MAXFONT)
                    {
                        triggered[0] = true;
                    }
                }
                else
                {
                    fontsize -= 0.01;
                    if(fontsize <= MINFONT)
                    {
                        triggered[0] = false;
                    }
                }
            });
            t.start();
        }

        if(animationTick <= DOUBLEROTATION / AXE_THROW_SPEED){
            int rotation = animationTick * AXE_THROW_SPEED;
            int x = -DOUBLEROTATION * AXE_THROW_SPEED + animationTick * AXE_THROW_SPEED * AXE_THROW_SPEED + (GraphicsWindow.WIDTH/2 - axe.getWidth(null)/2);
            int y = GraphicsWindow.HEIGHT/2 - axe.getHeight(null)/2;
            g.drawImage(axe, EasyTransformer.rotateAroundCenterWithOffset(axe, rotation, 100, -100, x, y), null);
        }else{
            if(animationTick <= (DOUBLEROTATION / AXE_THROW_SPEED) + beard.getHeight(null) / BEARD_SPEED){
                int clipHeight = (animationTick - DOUBLEROTATION / AXE_THROW_SPEED) * BEARD_SPEED;
                g.setClip(BEARDOFFSET-beard.getWidth(null)/2, BEARDHEIGHTOFFSET-beard.getHeight(null)/2, BEARDOFFSET - beard.getWidth(null)/2, clipHeight);
                g.drawImage(beard, BEARDOFFSET-beard.getWidth(null)/2, BEARDHEIGHTOFFSET-beard.getHeight(null)/2, null);
                g.setClip(0, 0, GraphicsWindow.WIDTH, GraphicsWindow.HEIGHT);
            }else{
                g.drawImage(beard, BEARDOFFSET-beard.getWidth(null)/2, BEARDHEIGHTOFFSET-beard.getHeight(null)/2, null);
                if(animationTick <= (DOUBLEROTATION / AXE_THROW_SPEED) + beard.getHeight(null) / BEARD_SPEED + 400 / TEXT_SPEED){
                    float opacity = ((animationTick - DOUBLEROTATION / AXE_THROW_SPEED) - beard.getHeight(null) / BEARD_SPEED) / (400.0f / TEXT_SPEED);
                    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
                    g.drawImage(logoText, TEXTOFFSET - logoText.getWidth(null) / 2, TEXTHEIGHTOFFSET - logoText.getHeight(null) / 2, null);
                    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
                }else{
                    g.drawImage(logoText, TEXTOFFSET - logoText.getWidth(null) / 2, TEXTHEIGHTOFFSET - logoText.getHeight(null) / 2, null);
                }
            }
            g.drawImage(axe, (GraphicsWindow.WIDTH/2)-axe.getWidth(null)/2, (GraphicsWindow.HEIGHT/2)-axe.getHeight(null)/2, null);
        }
        animationTick++;
    }
}
