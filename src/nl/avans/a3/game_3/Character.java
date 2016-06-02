package nl.avans.a3.game_3;

import nl.avans.a3.util.ResourceHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Character {

    private int playerNumber, x, y;
    private BufferedImage playerImage;

    public Character(int playerNumber, int x, int y){
        this.playerNumber = playerNumber;
        this.x = x;
        this.y = y;
        setImage();
    }

    public void update(){

    }

    public void draw(Graphics2D g2){
        g2.drawImage(playerImage, x, y, 328, 480, null);
    }

    private void setImage(){
        if(playerNumber == 1){
            BufferedImage image = (BufferedImage) ResourceHandler.getImage("res/images_game1/person1.png");
            playerImage = image.getSubimage(0, 0, 1315, 1922);
        }else{
            BufferedImage image = (BufferedImage) ResourceHandler.getImage("res/images_game1/person2.png");
            playerImage = image.getSubimage(0, 0, 1315, 1922);
        }
    }

    public BufferedImage getPlayerImage()
    {
        return playerImage;
    }

}
