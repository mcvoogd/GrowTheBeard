package nl.avans.a3.boot_menu;


import nl.avans.a3.util.EasyTransformer;
import nl.avans.a3.event.ModelEvent;
import nl.avans.a3.util.ResourceHandler;
import nl.avans.a3.mvc_interfaces.View;

import javax.swing.*;
import java.awt.*;

public class BootView implements View{

    private static final int AXE_THROW_SPEED = 8;
    private static final int BEARD_SPEED = 8;
    private static final int TEXT_SPEED = 2;
    private int animationTick;

    /**
     * Resources
     */

    private Image background;
    private Image  axe;
    private Image beard;
    private Image logoText;
    private Image text;
    private Image warningImage;

    private double textScale = 0;
    private static final double CHANGE_SPEED = 0.01;
    private double change = CHANGE_SPEED;
    private static final double MAX_SCALE = 1.5;
    private static final double MIN_SCALE = 1.0;

    private static final int Y_OFFSET = 400;
    private static final int BEARD_OFFSET = 860;
    private static final int BEARD_HEIGHT_OFFSET = 610;
    private static final int TEXT_OFFSET = 850;
    private static final int TEXT_HEIGHT_OFFSET = 590;
    private static final int DOUBLE_ROTATION = 720;

    private final int WIDTH = 1920;
    private final int HEIGHT = 1080;

    private double rotation = 5;
    private double speed = 0.5;
    private boolean warning = true;

    @Override
    public void start() {
        animationTick = 0;
        background =  ResourceHandler.getImage("res/splash/background.png");
        axe = ResourceHandler.getImage("res/splash/axe.png");
        beard =  ResourceHandler.getImage("res/splash/beard.png");
        logoText = ResourceHandler.getImage("res/splash/logo_text.png");
        text = ResourceHandler.getImage("res/splash/text.png");
        warningImage = ResourceHandler.getImage("res/warning.png");

    }

    @Override
        public void draw(Graphics2D g) {
        if(warning){
            Timer timer = new Timer(3000, e -> {warning = false;});
            timer.start();
            g.drawImage(warningImage, 0, 0, null);
        }else {
            if (text == null)
                return;
            g.drawImage(background, 0, 0, null);

            textScale += change;
            if (textScale > MAX_SCALE) {
                change = -CHANGE_SPEED;
            } else if (textScale < MIN_SCALE) {
                change = CHANGE_SPEED;
            }

            g.drawImage(text, EasyTransformer.scaleImageFromCenter(text, textScale, WIDTH / 2 - text.getWidth(null) / 2, HEIGHT / 2 + Y_OFFSET), null);

            if (animationTick <= DOUBLE_ROTATION / AXE_THROW_SPEED) {
                int rotation = animationTick * AXE_THROW_SPEED;
                int x = -DOUBLE_ROTATION * AXE_THROW_SPEED + animationTick * AXE_THROW_SPEED * AXE_THROW_SPEED + (WIDTH / 2 - axe.getWidth(null) / 2);
                int y = HEIGHT / 2 - axe.getHeight(null) / 2;
                g.drawImage(axe, EasyTransformer.rotateAroundCenterWithOffset(axe, rotation, 100, -100, x, y), null);
            } else {
                if (animationTick <= (DOUBLE_ROTATION / AXE_THROW_SPEED) + beard.getHeight(null) / BEARD_SPEED) {
                    int clipHeight = (animationTick - DOUBLE_ROTATION / AXE_THROW_SPEED) * BEARD_SPEED;
                    g.setClip(BEARD_OFFSET - beard.getWidth(null) / 2, BEARD_HEIGHT_OFFSET - beard.getHeight(null) / 2, BEARD_OFFSET - beard.getWidth(null) / 2, clipHeight);
                    g.drawImage(beard, BEARD_OFFSET - beard.getWidth(null) / 2, BEARD_HEIGHT_OFFSET - beard.getHeight(null) / 2, null);
                    g.setClip(0, 0, WIDTH, HEIGHT);
                } else {
                    g.drawImage(beard, BEARD_OFFSET - beard.getWidth(null) / 2, BEARD_HEIGHT_OFFSET - beard.getHeight(null) / 2, null);
                    if (animationTick <= (DOUBLE_ROTATION / AXE_THROW_SPEED) + beard.getHeight(null) / BEARD_SPEED + 400 / TEXT_SPEED) {
                        float opacity = ((animationTick - DOUBLE_ROTATION / AXE_THROW_SPEED) - beard.getHeight(null) / BEARD_SPEED) / (400.0f / TEXT_SPEED);
                        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
                        g.drawImage(logoText, TEXT_OFFSET - logoText.getWidth(null) / 2, TEXT_HEIGHT_OFFSET - logoText.getHeight(null) / 2, null);
                        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
                    } else {
                        g.drawImage(logoText, TEXT_OFFSET - logoText.getWidth(null) / 2, TEXT_HEIGHT_OFFSET - logoText.getHeight(null) / 2, null);
                    }
                }
                g.drawImage(axe, (WIDTH / 2) - axe.getWidth(null) / 2, (HEIGHT / 2) - axe.getHeight(null) / 2, null);
            }
            animationTick++;
        }
    }

    @Override
    public void close() {
        ResourceHandler.unloadImage("res/splash/background.png");
        ResourceHandler.unloadImage("res/splash/axe.png");
        ResourceHandler.unloadImage("res/splash/beard.png");
        ResourceHandler.unloadImage("res/splash/logo_text.png");
        ResourceHandler.unloadImage("res/splash/text.png");

    }

    @Override
    public void onModelEvent(ModelEvent event) {

    }
}
