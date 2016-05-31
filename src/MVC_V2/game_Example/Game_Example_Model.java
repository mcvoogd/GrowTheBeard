package MVC_V2.game_Example;

import MVC_V2.mvcInterfaces.Model;

public class Game_Example_Model implements Model {
    private float pitch;
    private int x = 500;
    private int y = 500;

    public Game_Example_Model()
    {
        pitch = 0f;
    }

    @Override
    public void start() {
    }

    @Override
    public void update() {
        System.out.println("Update model" +  "X : " + x + " Pitch: " + pitch);

        this.x = (int) (x + (pitch*10.0));
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
