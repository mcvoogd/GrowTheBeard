package nl.avans.a3.game_1;

import nl.avans.a3.game_1.Util.Images;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Particle{

    private int xPos, yPos;
    private double angle;
    private BufferedImage image;
    private int life, speed = 5;

    public Particle(int xPos, int yPos, double angle){  // why giving the angle with the constructor, should the angle not be random?
        this.xPos = xPos;
        this.yPos = yPos;
        this.angle = angle;
        image = Images.rescaleImage(16,16, Images.particle);
    }

    public void update(){
        xPos += Math.cos(angle) * speed;
        yPos += Math.sin(angle) * speed;
        life++;
    }

    public void draw(Graphics2D g2){
        g2.drawImage(image, xPos, yPos, null);
    }

    public int getLife() {
        return life;
    }

}
