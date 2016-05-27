package MVC_V2;

import java.awt.*;

public class SimpleView implements View {
    @Override
    public void start() {
        System.out.println("started!");
    }

    @Override
    public void draw(Graphics2D g) {
        g.drawString("DIT WeeeeeeeeRRRUKTTTTT", 500, 500);
    }

    @Override
    public void close() {

    }

    @Override
    public void onModelEvent(ModelEvent event) {

    }
}
