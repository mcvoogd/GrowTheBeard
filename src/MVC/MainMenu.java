package MVC;

import java.awt.image.BufferedImage;

public class MainMenu implements GameModelInterface {

    private double rotation = 0.0;
    private boolean triggered = false;
    private double speed = 0.5;
    private ModelListener listener;
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

    @Override
    public void setListener(ModelListener listener) {
        if(listener != null)
        {
            this.listener = listener;
        }
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
