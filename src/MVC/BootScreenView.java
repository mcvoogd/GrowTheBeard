package MVC;

import nl.avans.a3.EasyTransformer;
import nl.avans.a3.GraphicsWindow;
import nl.avans.a3.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class BootScreenView implements GameViewInterface {
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

    private double textScale = 0;
    private static final double MAX_SCALE = 1.5;
    private static final double MIN_SCALE = 1.0;
    private static final int Y_OFFSET = 400;
    private static final int BEARD_OFFSET = 860;
    private static final int BEARD_HEIGHT_OFFSET = 610;
    private static final int TEXT_OFFSET = 850;
    private static final int TEXT_HEIGHT_OFFSET = 590;
    private static final int DOUBLE_ROTATION = 720;
    private static final double CHANGE_SPEED = 0.01;
    private double change = CHANGE_SPEED;

    public BootScreenView()
    {
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
    @Override
    public void draw(Graphics2D g) {
        g.drawImage(background, 0, 0, null);

        g.drawImage(text, EasyTransformer.scaleImageFromCenter(text, textScale, GraphicsWindow.WIDTH/2 - text.getWidth(null)/2, GraphicsWindow.HEIGHT/2 + Y_OFFSET), null);

        if(animationTick <= DOUBLE_ROTATION / AXE_THROW_SPEED){
            int rotation = animationTick * AXE_THROW_SPEED;
            int x = -DOUBLE_ROTATION * AXE_THROW_SPEED + animationTick * AXE_THROW_SPEED * AXE_THROW_SPEED + (GraphicsWindow.WIDTH/2 - axe.getWidth(null)/2);
            int y = GraphicsWindow.HEIGHT/2 - axe.getHeight(null)/2;
            g.drawImage(axe, EasyTransformer.rotateAroundCenterWithOffset(axe, rotation, 100, -100, x, y), null);
        }else{
            if(animationTick <= (DOUBLE_ROTATION / AXE_THROW_SPEED) + beard.getHeight(null) / BEARD_SPEED){
                int clipHeight = (animationTick - DOUBLE_ROTATION / AXE_THROW_SPEED) * BEARD_SPEED;
                g.setClip(BEARD_OFFSET -beard.getWidth(null)/2, BEARD_HEIGHT_OFFSET -beard.getHeight(null)/2, BEARD_OFFSET - beard.getWidth(null)/2, clipHeight);
                g.drawImage(beard, BEARD_OFFSET -beard.getWidth(null)/2, BEARD_HEIGHT_OFFSET -beard.getHeight(null)/2, null);
                g.setClip(0, 0, GraphicsWindow.WIDTH, GraphicsWindow.HEIGHT);
            }else{
                g.drawImage(beard, BEARD_OFFSET -beard.getWidth(null)/2, BEARD_HEIGHT_OFFSET -beard.getHeight(null)/2, null);
                if(animationTick <= (DOUBLE_ROTATION / AXE_THROW_SPEED) + beard.getHeight(null) / BEARD_SPEED + 400 / TEXT_SPEED){
                    float opacity = ((animationTick - DOUBLE_ROTATION / AXE_THROW_SPEED) - beard.getHeight(null) / BEARD_SPEED) / (400.0f / TEXT_SPEED);
                    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
                    g.drawImage(logoText, TEXT_OFFSET - logoText.getWidth(null) / 2, TEXT_HEIGHT_OFFSET - logoText.getHeight(null) / 2, null);
                    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
                }else{
                    g.drawImage(logoText, TEXT_OFFSET - logoText.getWidth(null) / 2, TEXT_HEIGHT_OFFSET - logoText.getHeight(null) / 2, null);
                }
            }
            g.drawImage(axe, (GraphicsWindow.WIDTH/2)-axe.getWidth(null)/2, (GraphicsWindow.HEIGHT/2)-axe.getHeight(null)/2, null);
        }
        animationTick++;
    }


    @Override
    public void update() {
        textScale += change;
        if(textScale > MAX_SCALE){
            change = -CHANGE_SPEED;
        }else if(textScale < MIN_SCALE){
            change = CHANGE_SPEED;
        }
    }
}
