package nl.avans.a3.game_2;
import com.sun.org.apache.xpath.internal.operations.Mod;
import nl.avans.a3.mvc_handlers.ModelHandler;
import nl.avans.a3.mvc_interfaces.Model;

import java.time.Duration;
import java.util.Random;

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
        final int id;
        boolean jump = false;

        Player(int id, float x, float y)
        {
            super(PlAYER_WIDTH, PLAYER_HEIGHT, false);
            this.id = id;
            this.x = x;
            this.y = y;
            ModelHandler.instance.onModelEvent(new G2_NewObject(id, true, x, y));
        }

        final int JUMP_DURATION = 75;
        final double JUMP_HEIGHT = 200;
        final double v = JUMP_DURATION/Math.PI;
        int jumpTicks = 0;

        private double heightAt(double x)
        {
            return (Math.sin(x/(JUMP_DURATION/Math.PI))*JUMP_HEIGHT);
        }

        public void update()
        {
            if (jump && state != PlayerState.JUMPING)
            {
                state = PlayerState.JUMPING;
                jumpTicks = 1;
            }
            if (state == PlayerState.JUMPING)
            {
                x += movX*4;
                y += heightAt(jumpTicks)-heightAt(jumpTicks-1);
                System.out.println("current ("+jumpTicks+") = " + heightAt(jumpTicks) +
                ", previus("+(jumpTicks-1) + ") = " + heightAt(jumpTicks-1)+
                ", diffrence = " + (heightAt(jumpTicks)-heightAt(jumpTicks-1)));
                if (++jumpTicks > JUMP_DURATION) {
                    state = PlayerState.ON_KINETIC;
                }
            }
            else
            {
                x += movX*5;
                // TODO handle the platform that you're standing on
            }
            jump = false;
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
        for (int i = 0; i < PLAYER_COUNT; i++)
            players[i] = new Player(i, 100+75*i, 400);
    }

    Random rand = new Random(System.currentTimeMillis());

    @Override
    public void update() {
        players[0].update();
        players[1].update();

        ModelHandler.instance.onModelEvent(new G2_ObjectMove(0, true, players[0].x, players[0].y));
        ModelHandler.instance.onModelEvent(new G2_ObjectMove(1, true, players[1].x, players[1].y));
    }

    @Override
    public void close() {

    }

    public void setMoveHorizontal(float move, int player)
    {
        if (player > 1) return; // TODO proper warnings
        if (Math.abs(move)>1) return;
        players[player].movX = move;
        //System.out.println("player" + player + ", moveX = " + players[player].movX);
    }

    public void setJump(boolean pressed, int player) {
        if (player > 1) return;
        players[player].jump = pressed;
    }
}
