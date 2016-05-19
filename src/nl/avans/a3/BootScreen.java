package nl.avans.a3;


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

    private boolean timerstarted = false;
    Shape s = null;
    private double fontsize = 0;
    private final int MAXFONT = 50;
    private final int MINFONT = 40;
    private final String TEXTAB = "Press A + B to start!";
    private final int YOFFSET = 400;
    private final int BEARDOFFSET = 860;
    private final int BEARDHEIGHTOFFSET = 610;
    private final int TEXTOFFSET = 850;
    private final int TEXTHEIGHTOFFSET = 590;
    private final int DOUBLEROTATION = 720;


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
        g.setColor(Color.WHITE);
        g.fill(s);

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
                    g.drawImage(text, TEXTOFFSET - text.getWidth(null) / 2, TEXTHEIGHTOFFSET - text.getHeight(null) / 2, null);
                    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
                }else{
                    g.drawImage(text, TEXTOFFSET - text.getWidth(null) / 2, TEXTHEIGHTOFFSET - text.getHeight(null) / 2, null);
                }
            }
            g.drawImage(axe, (GraphicsWindow.WIDTH/2)-axe.getWidth(null)/2, (GraphicsWindow.HEIGHT/2)-axe.getHeight(null)/2, null);
        }
        animationTick++;

    }
}
