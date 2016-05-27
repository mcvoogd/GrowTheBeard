package MVC_V2;

import java.awt.*;

/**
 * Created by FlorisBob on 27-May-16.
 */
public interface View extends ModelListener{
    public void start();
    public void draw(Graphics2D graphics2D);
    public void close();
}
