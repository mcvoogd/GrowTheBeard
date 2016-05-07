package GUI;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class EasyTransformer{
    public static AffineTransform rotateAroundCenter(Image image, int rotation){
        return rotateAroundCenter(image, rotation, 0, 0);
    }
    
    public static AffineTransform rotateAroundCenter(Image image, int rotation, int xPos, int yPos){
        return rotateAroundCenterWithOffset(image, rotation, 0, 0, xPos, yPos);
    }
    
    public  static AffineTransform rotateAroundCenterWithOffset(Image image, int rotation, int xOffset, int yOffset){
        return rotateAroundCenterWithOffset(image, rotation, xOffset, yOffset, 0, 0);
    }
    
    public static AffineTransform rotateAroundCenterWithOffset(Image image, int rotation, int xOffset, int yOffset, int xPos, int yPos){
        AffineTransform at = new AffineTransform();
        at.translate(xPos, yPos);
        at.translate(image.getWidth(null)/2 + xOffset, image.getHeight(null)/2 + yOffset);
        at.rotate(Math.toRadians(rotation));
        at.translate(-(image.getWidth(null)/2 + xOffset), -(image.getHeight(null)/2 + yOffset));
        return at;
    }
}
