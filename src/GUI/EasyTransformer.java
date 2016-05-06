package GUI;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class EasyTransformer{
    public static AffineTransform rotateAroundCenter(Image image, int rotation){
        return rotateAroundCenterWithOffset(image, rotation, 0, 0);
    }
    
    public static AffineTransform rotateAroundCenterWithOffset(Image image, int rotation, int x, int y){
        AffineTransform at = new AffineTransform();
        at.translate(image.getWidth(null)/2 + x, image.getHeight(null)/2 + y);
        at.rotate(Math.toRadians(rotation));
        at.translate(-(image.getWidth(null)/2 + x), -(image.getHeight(null)/2 + y));
        return at;
    }
}
