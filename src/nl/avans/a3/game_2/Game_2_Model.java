package nl.avans.a3.game_2;
import nl.avans.a3.mvc_interfaces.Model;

/**
 * Created by Thijs on 2-6-2016.
 */
public class Game_2_Model implements Model
{
    final int PLAYER_COUNT = 2;
    final int WORLD_WIDTH_BOUND = 1920;
    final int WORLD_HEIGHT_BOUND = 1080;
    final int PLAYER_HEIGHT = 164;
    final int PlAYER_WIDTH = 112;
    final int BLOCK_WIDTH = 50;
    final int BLOCK_HEIGHT = 20;

    public enum PlayerState{JUMPING, ON_KINETIC}

    private class Collidiable
    {
        float x, y;
        final float width, height;
        float movX, movY;
        boolean kinetic;
        Collidiable(float width, float height, boolean kinetic)
        {
            this.width = width; this.height = height; this.kinetic = kinetic;
            x = y = movX = movY = 0;
        }
    }

    private class Player extends Collidiable
    {
        PlayerState state;

        float pitch = 0;
        boolean aPressed = false;

        Player() {
            super(PlAYER_WIDTH, PLAYER_HEIGHT, false);
        }
    }

    private class WoodBlock extends Collidiable
    {

        WoodBlock() {
            super(BLOCK_WIDTH, BLOCK_HEIGHT, true);
        }
    }

    Player[] players = new Player[PLAYER_COUNT];

    public Game_2_Model()
    {

    }

    @Override
    public void start() {
    }

    @Override
    public void update() {

    }

    @Override
    public void close() {

    }

    public void setPitch(float pitch, int player)
    {
        players[player].pitch = pitch;
    }

    public void setAButtonPressed(boolean pressed, int player) {
    }
}
