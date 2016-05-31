package MVC_V2.game_2;

import MVC_V2.mvcInterfaces.Model;

public class Game_2_Model implements Model {
    private float pitch;
    private int x = 500;
    private int y = 500;

    public Game_2_Model()
    {
        pitch = 0f;
    }

    @Override
    public void start() {
    }

    @Override
    public void update() {
        this.x = (int) (x + pitch);
    }

    @Override
    public void close() {

    }

    public void setPitch(float pitch)
    {
        this.pitch = pitch;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public float getPitch()
    {
        return pitch;
    }

}
