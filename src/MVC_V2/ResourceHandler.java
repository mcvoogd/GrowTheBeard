package MVC_V2;


import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import nl.avans.a3.Logger;

/**
 * Created by FlorisBob on 27-May-16.
 */
public class ResourceHandler {
    private static HashMap<String, Image> imageHashMap = new HashMap<>();

    public static final Image getImage(String imageKey)
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
        Logger.instance.log("RH005", "image ("+imageKey+") has been loaded", Logger.LogType.DEBUG);
        return image;
    }

    public static void unloadImage(String imageKey)
    {
        if (imageKey == null) Logger.instance.log("RH001", "image can't be null", Logger.LogType.ERROR);
        else if (imageHashMap.containsKey(imageKey) == false) Logger.instance.log("RH004", "image ("+imageKey+") is not loaded", Logger.LogType.WARNING);
        else { imageHashMap.remove(imageKey); Logger.instance.log("RH006", "image ("+imageKey+") has been unloaded", Logger.LogType.DEBUG);}
    }

    public void dump()
    {
        imageHashMap.clear();
        Logger.instance.log("RH007", "resources have been dumped", Logger.LogType.DEBUG);
    }

}
