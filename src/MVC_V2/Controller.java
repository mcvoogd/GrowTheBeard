package MVC_V2;

import java.awt.event.KeyListener;

/**
 * Created by FlorisBob on 27-May-16.
 */
public interface Controller extends ModelListener, KeyListener{

    /**
     * this method is constantly called, put specific controls for the controller in here.
     */
    public void update();
}
