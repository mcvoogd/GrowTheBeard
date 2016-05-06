package GUI;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BootScreen{
    private int animationTick;
    
    /**
     * Resources
     */
    private Image background;
    private BufferedImage axe;
    private BufferedImage beard;
    private BufferedImage text;
    
    public BootScreen(){
        animationTick = 0;
        try{
            background = ImageIO.read(new File("res/boot_bg.png")).getScaledInstance(1920, 1080, Image.SCALE_DEFAULT);
            axe = ImageIO.read(new File("res/boot_axe.png"));
            beard = ImageIO.read(new File("res/boot_beard.png"));
            text = ImageIO.read(new File("res/boot_text.png"));
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void update(Graphics2D g){
        g.drawImage(background, 0, 0, null);
        if(animationTick < 1000){
            g.drawImage(axe, EasyTransformer.rotateAroundCenterWithOffset(axe, animationTick, 0, -100), null);
        }
        animationTick++;
    }
}
