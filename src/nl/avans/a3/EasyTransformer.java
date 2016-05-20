package nl.avans.a3;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class EasyTransformer{

    /**
     * Applies an AffineTransform to an Image to rotate it around it's center at (0, 0)
     * 
     * @param image image to apply transform to (to get height and with from image)
     * @param rotation in degrees
     * @return the AffineTransform to be applied on an image
     */
    public static AffineTransform rotateAroundCenter(Image image, int rotation){
        return rotateAroundCenter(image, rotation, 0, 0);
    }

    /**
     * Applies an AffineTransform to an Image to rotate it around it's center and translation to (xPos, yPos)
     * 
     * @param image image to apply transform to (to get height and with from image)
     * @param rotation in degrees
     * @param xPos position on screen
     * @param yPos position on screen
     * @return the AffineTransform to be applied on an image
     */
    public static AffineTransform rotateAroundCenter(Image image, int rotation, int xPos, int yPos){
        return rotateAroundCenterWithOffset(image, rotation, 0, 0, xPos, yPos);
    }

    /**
     * Applies an AffineTransform to an Image to rotate it around it's center plus x and y offset at (0, 0)
     * 
     * @param image image to apply transform to (to get height and with from image)
     * @param rotation in degrees
     * @param xOffset from center of image
     * @param yOffset from center of image
     * @return the AffineTransform to be applied on an image
     */
    public  static AffineTransform rotateAroundCenterWithOffset(Image image, int rotation, int xOffset, int yOffset){
        return rotateAroundCenterWithOffset(image, rotation, xOffset, yOffset, 0, 0);
    }

    /**
     * Applies an AffineTransform to an Image to rotate it around it's center plus x and y offset at (xPos, yPos)
     * 
     * @param image image to apply transform to (to get height and with from image)
     * @param rotation in degrees
     * @param xOffset from center of image
     * @param yOffset from center of image
     * @param xPos position on screen
     * @param yPos position on screen
     * @return the AffineTransform to be applied on an image
     */
    public static AffineTransform rotateAroundCenterWithOffset(Image image, int rotation, int xOffset, int yOffset, int xPos, int yPos){
        AffineTransform at = new AffineTransform();
        at.translate(xPos, yPos);
        at.translate(image.getWidth(null)/2 + xOffset, image.getHeight(null)/2 + yOffset);
        at.rotate(Math.toRadians(rotation));
        at.translate(-(image.getWidth(null)/2 + xOffset), -(image.getHeight(null)/2 + yOffset));
        return at;
    }

    /**
     * Applies an AffineTransform to an Image at (0, 0)
     * 
     * @param rotation in degrees
     * @return the AffineTransform to be applied on an image
     */
    public static AffineTransform rotate(int rotation){
        return rotate(rotation, 0, 0);
    }

    /**
     * Applies an AffineTransform to an Image at (xPos, yPos)
     * 
     * @param rotation in degrees
     * @param xPos position on screen
     * @param yPos position on screen
     * @return the AffineTransform to be applied on an image
     */
    public static AffineTransform rotate(int rotation, int xPos, int yPos){
        return rotateWithOffset(rotation, 0, 0, xPos, yPos);
    }

    /**
     * Applies an AffineTransform to an Image around xOffset and yOffset at (0, 0)
     * 
     * @param rotation in degrees
     * @param xOffset from left
     * @param yOffset from top
     * @return the AffineTransform to be applied on an image
     */
    public static AffineTransform rotateWithOffset(int rotation, int xOffset, int yOffset){
        return rotateWithOffset(rotation, xOffset, yOffset, 0, 0);
    }

    /**
     * Applies an AffineTransform to an Image around xOffset and yOffset at (xPos, yPos)
     * 
     * @param rotation in degrees
     * @param xOffset from left
     * @param yOffset from top
     * @param xPos position on screen
     * @param yPos position on screen
     * @return the AffineTransform to be applied on an image
     */
    public static AffineTransform rotateWithOffset(int rotation, int xOffset, int yOffset, int xPos, int yPos){
        AffineTransform at = new AffineTransform();
        at.translate(xPos + xOffset, yPos + yOffset);
        at.rotate(Math.toRadians(rotation));
        at.translate(-xOffset, -yOffset);
        return at;
    }
}
