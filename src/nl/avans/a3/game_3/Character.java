package nl.avans.a3.game_3;

import nl.avans.a3.util.ResourceHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Character {

    private int playerNumber, x, y;
    private BufferedImage playerImage, armImage, mainImage;
    private boolean chop;
    private Timer timer;
    private int imageNumber = 0;

    public Character(int playerNumber, int x, int y){
        this.playerNumber = playerNumber;
        this.x = x;
        this.y = y;
//        initImage();
        setImage(0);

        timer = new Timer(300/3, e -> {
            setImageNumber(getImageNumber() + 2);
        });
    }

    public void update(){
        if(chop){
            timer.start();
            chop = false;
        }
        if(imageNumber > 4){
            timer.stop();
            imageNumber = 0;
        }
    }

    public void draw(Graphics2D g2){

        if(playerNumber == 1){
            g2.drawImage(playerImage, x + 307, y, -614, 480, null);
            g2.drawImage(armImage, x + 307, y, -614, 480, null);
        }else{
            g2.drawImage(playerImage, x, y, 614, 480, null);
            g2.drawImage(armImage, x, y, 614, 480, null);
        }
    }

    public void setImage(int imageNumber){
        if(playerNumber == 1){
            BufferedImage image = (BufferedImage) ResourceHandler.getImage("res/images_game3/person1.png");
            playerImage = image.getSubimage(614 * imageNumber, 0, 614, 469);
            armImage = image.getSubimage(614 * (imageNumber + 1), 0, 614, 469);
        }else{
            BufferedImage image = (BufferedImage) ResourceHandler.getImage("res/images_game3/person1.png");
            playerImage = image.getSubimage(614 * imageNumber, 0, 614, 469);
            armImage = image.getSubimage(614 * (imageNumber + 1), 0, 614, 469);
        }

    }

    public void setChop(boolean chop){
        this.chop = chop;
    }

    private void setImageNumber(int imageNumber){
        this.imageNumber = imageNumber;
        setImage(imageNumber);
    }

    private int getImageNumber(){
        return imageNumber;
    }

    public BufferedImage getPlayerImage()
    {
        return mainImage;
    }

    public void initImage(){
        if(playerNumber == 1){
            mainImage = ResourceHandler.getImage("res/images_game1/person1.png");
        }else{
            mainImage = ResourceHandler.getImage("res/images_game1/person2.png");
        }
        mainImage = (BufferedImage) mainImage.getScaledInstance(168 * 4, 246, BufferedImage.SCALE_DEFAULT);
        mainImage = mainImage.getSubimage(0, 0, 168, 246);

    }
}
