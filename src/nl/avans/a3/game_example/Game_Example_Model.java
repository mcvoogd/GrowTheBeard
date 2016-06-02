package nl.avans.a3.game_example;

import nl.avans.a3.mvc_interfaces.Model;

public class Game_Example_Model implements Model{
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
