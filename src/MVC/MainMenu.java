package MVC;

import java.awt.image.BufferedImage;

public class MainMenu implements GameModelInterface {

    private double rotation = 0.0;
    private boolean triggered = false;
    private double speed = 0.5;

    private Mode mode = Mode.CHOOSESINGLE;

    public enum Mode{
        CHOOSEPARTY, CHOOSESINGLE
    }

    @Override
    public void update() {
        if(rotation < 0){
            speed += 0.01;
        }else if(rotation > 0){
            speed -= 0.01;
        }
        rotation += speed;
    }

    public void setMode(Mode mode){
        this.mode = mode;
    }

    public Mode getMode()
    {
        return mode;
    }

    public void setTriggered(boolean triggered)
    {
        this.triggered = triggered;
    }

    public boolean getTriggered()
    {
        return triggered;
    }
}
