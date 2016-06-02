package nl.avans.a3.game_2;

import nl.avans.a3.mvc_interfaces.Model;

/**
 * Created by Thijs on 2-6-2016.
 */
public class Game_2_Model implements Model {

    private class Player {
        float x, y;
        float pitch;
        boolean isJumping;
    }



    public Game_2_Model()
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

    public void setPitch(float pitch, int player)
    {
        this.pitch = pitch;
    }

    public void setAButtonPressed(boolean pressed, int player) {

    }
}
