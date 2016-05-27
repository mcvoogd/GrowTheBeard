package MVC.interfaces_listener;

import java.awt.*;

public interface ViewInterface extends ModelListener {

    void draw(Graphics2D g);
    void update();

}
