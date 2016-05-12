package Util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Images {

    public static BufferedImage woodBlock;
    public static BufferedImage player1;
    public static BufferedImage player2;

    public Images(){

        woodBlock = readImage("Sprite3");
        player1 = readImage("Sprite1");
        player2 = readImage("Sprite2");

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

