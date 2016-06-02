package nl.avans.a3.util;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by FlorisBob on 27-May-16.
 */
public class ResourceHandler {
    private static HashMap<String, BufferedImage> imageHashMap = new HashMap<>();

    public static final BufferedImage getImage(String imageKey)
    {
        if (imageKey == null)
        {
           Logger.instance.log("RH001");
            return null;
        }

        if (imageHashMap.containsKey(imageKey)) return imageHashMap.get(imageKey);

        File imageFile = new File(imageKey);
        if (imageFile.exists() == false) {Logger.instance.log("RH002", imageKey); return null;}
        if (imageFile.canRead() == false) {Logger.instance.log("RH003", imageKey); return null;}

        BufferedImage image = null;

        try {
            image = ImageIO.read(imageFile);
        } catch (IOException e) {
            Logger.instance.log(e);
            return null;
        }

        imageHashMap.put(imageKey, image);
        Logger.instance.log("RH005", imageKey);
        return image;
    }

    public static void unloadImage(String imageKey)
    {
        if (imageKey == null) Logger.instance.log("RH001");
        else if (imageHashMap.containsKey(imageKey) == false) Logger.instance.log("RH004", imageKey);
        else { imageHashMap.remove(imageKey); Logger.instance.log("RH006", imageKey);}
    }

    public void dump()
    {
        imageHashMap.clear();
        Logger.instance.log("RH007");
    }

}
