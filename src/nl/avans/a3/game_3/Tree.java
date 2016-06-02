package nl.avans.a3.game_3;

import nl.avans.a3.util.EasyTransformer;
import nl.avans.a3.util.ResourceHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Tree {

    private int hitpoints;
    private int x;
    private int y;
    private int width;
    private int height;
    private BufferedImage[] sprites;
    private BufferedImage sprite;
    private double rotation;
    private int maxRotation = 95;
    private boolean fallen;
    Timer rotator;

    public Tree(int x, int y)
    {
        hitpoints = 1000;
        this.x = x;
        this.y = y;
        width = 100;
        height = 1080;
        rotation = 0;
        sprites = new BufferedImage[4];
        for(int i = 0; i < 4; i++)
        {
            sprites[i] = (BufferedImage) ResourceHandler.getImage("res/images_game3/tree_" + i + ".png");
        }
        changeSprite(sprites[0]);
    }

    public void update()
    {
        if(hitpoints < 750)
        {
            changeSprite(sprites[0]);
        }
        if(hitpoints < 500)
        {
            changeSprite(sprites[1]);
        }
        if(hitpoints < 250)
        {
            changeSprite(sprites[2]);
        }
        if(hitpoints <= 0)
        {
            changeSprite(sprites[3]);
            if(!fallen){
                Random rand = new Random();
                drawFallingAnimation(rand.nextBoolean());
                fallen = true;
            }
        }
        if(rotation > maxRotation || rotation < -maxRotation)
        {
            rotator.stop();}
    }

    public void draw(Graphics2D g)
    {
        g.drawImage(sprite,  EasyTransformer.rotateAroundCenterWithOffset(sprite, rotation, 0, 300, x, y), null);
    }

    private void changeSprite(BufferedImage image)
    {
        this.sprite = image;
    }

    public void damageTree(int damage)
    {

        this.hitpoints -= damage;
        if(hitpoints < 0){
            hitpoints = 0;
        }
    }

    /**
     * read this to see what the boolean is used for.
     * @param leftOrRight : TRUE for left, FALSE for RIGHT.
     */
    public void drawFallingAnimation(boolean leftOrRight)
    {
            rotator = new Timer(1000/60, e->
        {
            if(leftOrRight)
            {
                if(rotation < maxRotation)
                {
                    if(rotation >= 0 && rotation <= 20)
                    {
                        rotation += 0.2;
                    }else
                    if(rotation > 20 && rotation <= 30)
                    {
                        rotation += 0.4;
                    }else
                    if(rotation > 30 && rotation <= 50)
                    {
                        rotation += 1.2;
                    }else
                    if(rotation > 50 && rotation <= 80)
                    {
                        rotation += 1.8;
                    }else
                    if(rotation > 80 && rotation <= maxRotation)
                    {
                        rotation += 2.2;
                    }

                }
            }else
            {
                if(rotation > -maxRotation)
                    if(rotation <= 0 && rotation >= -20)
                    {
                        rotation -= 0.2;
                    }else
                    if(rotation < -20 && rotation >= -30)
                    {
                        rotation -= 0.4;
                    }else
                    if(rotation < -30 && rotation >= -50)
                    {
                        rotation -= 1.2;
                    }else
                    if(rotation < -50 && rotation >= -80)
                    {
                        rotation -= 1.8;
                    }else
                    if(rotation < -80 && rotation >= -maxRotation)
                    {
                        rotation -= 2.2;
                    }
            }

        });
        rotator.start();
    }
}
