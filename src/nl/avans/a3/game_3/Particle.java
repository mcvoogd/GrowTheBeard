package nl.avans.a3.game_3;

import nl.avans.a3.game_1.Util.Images;
import nl.avans.a3.util.ResourceHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Particle{

    private int xPos, yPos;
    private double angle;
    private BufferedImage image;
    private int life, speed = 5;

    public Particle(int xPos, int yPos, double angle){
        this.xPos = xPos;
        this.yPos = yPos;
        this.angle = angle;
        BufferedImage mainImage = ResourceHandler.getImage("res/images_game3/wood_particles.png");
        int imageNumber = (int) (Math.random() * 9);
        image = mainImage.getSubimage(25 * imageNumber, 0, 25, 21);

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
