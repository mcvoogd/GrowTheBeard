package nl.avans.a3.game_3;

import nl.avans.a3.util.EasyTransformer;
import nl.avans.a3.util.ResourceHandler;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Bird {

    private int x, y;
    private final int WIDTH = 441/4, HEIGHT = 247/4;
    private boolean direction, wait;
    private BufferedImage image;

    public Bird(){
        y = 250;
        image = ResourceHandler.getImage("res/images_game3/bird.png");
    }

    public void update(){
        if(!wait){
            if(direction){
                x = x + 5;
                if(x > 2010){
                    direction = false;
                    wait = true;
                }
            }else{
                x = x - 5;
                if(x < - 150){
                    direction = true;
                    wait = true;
                }
            }
        }

        y += (Math.random() - 0.5) * 5;
        if(y > 300){
            y = 300;
        }
        if(y < 200){
            y = 200;
        }
    }

    public void draw(Graphics2D g){
        if(direction){
            g.drawImage(image, x, y, WIDTH, HEIGHT, null);
        }else{
            g.drawImage(image, x + WIDTH, y, -WIDTH, HEIGHT, null);
        }
    }

    public boolean getWait(){
        return wait;
    }

    public void setWait(int chance){
        if(chance > 290){
            wait = false;
        }
    }
}
