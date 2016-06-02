package nl.avans.a3.game_3;

import nl.avans.a3.util.ResourceHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Tree {

    private int hitpoints;
    private int x;
    private int y;
    private int width;
    private int height;
    private BufferedImage[] sprites;
    private BufferedImage sprite;

    public Tree(int x, int y)
    {
        System.out.println("NEW T BJR GYEIBHVBv hffycv hygfyt8v");
        hitpoints = 1000;
        this.x = x;
        this.y = y;
        width = 100;
        height = 1080;
        sprites = new BufferedImage[4];
        for(int i = 0; i < 3; i++)
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
        }
    }

    public void draw(Graphics g)
    {
        g.drawImage(sprite, x, y, width, height, null);
    }

    private void changeSprite(BufferedImage image)
    {
        this.sprite = image;
    }

    public void setHitpoints(int damage)
    {
        this.hitpoints -= damage;
    }


    public void drawFallingAnimation(Graphics2D g)
    {
        //TODO draw fall animation.
    }
}
