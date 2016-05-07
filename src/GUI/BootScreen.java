package GUI;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class BootScreen{
    private static final int AXE_THROW_SPEED = 4;
    private static final int BEARD_SPEED = 8;
    private int animationTick;
    
    /**
     * Resources
     */
    private Image background;
    private Image axe;
    private Image beard;
    private Image text;
    
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
        if(animationTick <= 720 / AXE_THROW_SPEED){
            int rotation = animationTick * AXE_THROW_SPEED;
            int x = -720 * AXE_THROW_SPEED + animationTick * AXE_THROW_SPEED * AXE_THROW_SPEED + (960 - axe.getWidth(null)/2);
            int y = 540 - axe.getHeight(null)/2;
            g.drawImage(axe, EasyTransformer.rotateAroundCenterWithOffset(axe, rotation, 100, -100, x, y), null);
        }else{
            if(animationTick <= (720 / AXE_THROW_SPEED) + beard.getHeight(null)/BEARD_SPEED){
                int clipHeight = (animationTick - 720 / AXE_THROW_SPEED) * BEARD_SPEED;
                g.setClip(860-beard.getWidth(null)/2, 610-beard.getHeight(null)/2, 860-beard.getWidth(null)/2, clipHeight);
                g.drawImage(beard, 860-beard.getWidth(null)/2, 610-beard.getHeight(null)/2, null);
                g.setClip(0, 0, 1920, 1080);
            }else{
                g.drawImage(beard, 860-beard.getWidth(null)/2, 610-beard.getHeight(null)/2, null);
            }
            g.drawImage(axe, 960-axe.getWidth(null)/2, 540-axe.getHeight(null)/2, null);
        }
        animationTick++;
    }
}
