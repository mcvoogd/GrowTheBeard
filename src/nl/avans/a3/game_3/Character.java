package nl.avans.a3.game_3;

import com.sun.org.apache.regexp.internal.RE;
import nl.avans.a3.util.Beard;
import nl.avans.a3.util.ResourceHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Character {

    private int playerNumber, x, y;
    private BufferedImage playerImage, armImage, mainImage, beardImage;
    private boolean chop;
    private Timer timer;
    private int imageNumber = 0;
    private int beardNumber;

    public Character(int playerNumber, int x, int y){
        this.playerNumber = playerNumber;
        this.x = x;
        this.y = y;
        if(playerNumber == 1){
            beardNumber = Beard.beardPlayer1;
        }else{
            beardNumber = Beard.beardPlayer2;
        }
        initImage();
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
            g2.drawImage(beardImage, x + 307, y, -614, 469, null);
            g2.drawImage(armImage, x + 307, y, -614, 480, null);
        }else{
            g2.drawImage(playerImage, x, y, 614, 480, null);
            g2.drawImage(beardImage, x, y, 614, 469, null);
            g2.drawImage(armImage, x, y, 614, 480, null);
        }
    }

    public void setImage(int imageNumber){
        // TODO way to manny magic numbers these could all be generated based on the image and the image number
        if(playerNumber == 1){
            BufferedImage image = ResourceHandler.getImage("res/images_game3/person1.png");
            playerImage = image != null ? image.getSubimage(614 * imageNumber, 0, 614, 469) : null;
            armImage = image != null ? image.getSubimage(614 * (imageNumber + 1), 0, 614, 469) : null;
        }else{
            BufferedImage image = ResourceHandler.getImage("res/images_game3/person2.png");
            playerImage = image != null ? image.getSubimage(614 * imageNumber, 0, 614, 469) : null;
            armImage = image != null ? image.getSubimage(614 * (imageNumber + 1), 0, 614, 469) : null;
        }
        BufferedImage beardImage = ResourceHandler.getImage("res/images_game3/beard.png").getSubimage(614 * beardNumber, 0, 614, 469 * 3);
        switch (imageNumber){
            case 0:
                this.beardImage = beardImage.getSubimage(0, 469, 614, 469);
                break;
            case 2:
                this.beardImage = beardImage.getSubimage(0, 0, 614, 469);
                break;
            case 4:
                this.beardImage = beardImage.getSubimage(0, 0, 614, 469);
                break;
            case 6:
                this.beardImage = beardImage.getSubimage(0, 469*2, 614, 469);
                break;
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
            mainImage = ResourceHandler.getImage("res/images_game2/person1.png");
        }else{
            mainImage = ResourceHandler.getImage("res/images_game2/person2.png");
        }
            Image temp = mainImage;
            BufferedImage bufferedImage = new BufferedImage(200, 600, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = bufferedImage.createGraphics();
            g2.drawImage(temp, 0, 0, null);
            mainImage = bufferedImage.getSubimage(0, 0, 200, 300);
    }
}
