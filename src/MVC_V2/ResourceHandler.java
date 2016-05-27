package MVC_V2;

import nl.avans.a3.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by FlorisBob on 27-May-16.
 */
public class ResourceHandler {
    private static HashMap<String, Image> imageHashMap = new HashMap<>();

    public final Image getImage(String imageKey)
    {
        if (imageKey == null)
        {
            Logger.instance.log("RH001", "image can't be null", Logger.LogType.ERROR);
            return null;
        }

        if (imageHashMap.containsKey(imageKey)) return imageHashMap.get(imageKey);

        File imageFile = new File(imageKey);
        if (imageFile.exists() == false) {Logger.instance.log("RH002", "image (" + imageKey + ") doesn't exist", Logger.LogType.ERROR); return null;}
        if (imageFile.canRead() == false) {Logger.instance.log("RH003", "image (" + imageKey + ") canot be red", Logger.LogType.ERROR); return null;}

        Image image = null;

        try {
            image = ImageIO.read(imageFile);
        } catch (IOException e) {
            Logger.instance.log(e);
            return null;
        }

        imageHashMap.put(imageKey, image);
        return image;
    }
}
