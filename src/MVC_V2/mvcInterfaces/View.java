package MVC_V2.mvcInterfaces;

import java.awt.*;

/**
 * Created by FlorisBob on 27-May-16.
 */
public interface View extends ModelListener {
    /**
     * this method is the first to be called when initializing.
     */
    public void start();

    /**
     * this is the method that draws its view.
     * @param g graphics object to draw with.
     */
    public void draw(Graphics2D g);

    /**
     * this method is called when switching views, use it to close active items
     * and unload images.
     */
    public void close();
}
