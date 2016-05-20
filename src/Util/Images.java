package Util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Images {

    public static BufferedImage woodBlock, woodBlock2, woodBlock3;
    public static BufferedImage player1;
    public static BufferedImage player2;
    public static BufferedImage game1Background;

    public Images(){

        woodBlock = readImage("images_game1\\wood1");
        woodBlock2 = readImage("images_game1\\wood2");
        woodBlock3 = readImage("images_game1\\wood3");
        player1 = readImage("images_game1\\person1");
        player2 = readImage("images_game1\\person2");
        game1Background = readImage("images_game1\\background");

    }

    public BufferedImage readImage(String file) {
        try {
            return ImageIO.read(Images.class.getResource(("/" + file + ".png")));
        } catch (IOException e) {
            System.err.println("Could not load Image: " + e);
        }

        return null;
    }
}

