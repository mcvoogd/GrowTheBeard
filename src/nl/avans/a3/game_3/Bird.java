package nl.avans.a3.game_3;

import nl.avans.a3.util.EasyTransformer;
import nl.avans.a3.util.ResourceHandler;
import sun.util.BuddhistCalendar;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Bird {

    private int x, y;
    private final int WIDTH = 103, HEIGHT = 142;
    private boolean direction, wait;
    private BufferedImage image;
    private BufferedImage[] images = new BufferedImage[4];
    private int imageNumber = 0;
    private boolean upImage = true;

    public Bird(){
        y = 250;
        image = ResourceHandler.getImage("res/images_game3/bird.png");
        for (int i = 0; i < 4; i++) {
            images[i] = image.getSubimage(103 * i,0, 103, 142);
        }
        image = images[imageNumber];

        Timer timer = new Timer(1000/8, e_-> animate());
        timer.start();
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

    private void animate(){
        if(upImage){
            imageNumber++;
        }else{
            imageNumber--;
        }

        if(imageNumber > 3 || imageNumber < 0){
            imageNumber = 0;
            upImage = !upImage;
        }
        image = images[imageNumber];

    }
}
