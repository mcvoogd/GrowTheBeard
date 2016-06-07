package nl.avans.a3.game_2;
import com.sun.org.apache.xpath.internal.operations.Mod;
import nl.avans.a3.mvc_handlers.ModelHandler;
import nl.avans.a3.mvc_interfaces.Model;
import nl.avans.a3.util.MathExtended;
import nl.avans.a3.util.ResourceHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Thijs on 2-6-2016.
 */
public class Game_2_Model implements Model
{
    final int PLAYER_COUNT = 2;
    final int WORLD_WIDTH_BOUND = 1920;
    final int WORLD_HEIGHT_BOUND = 1080;
    final int PLAYER_HEIGHT = 267;
    final int PlAYER_WIDTH = 80;
    final int BLOCK_WIDTH = 50;
    final int BLOCK_HEIGHT = 20;

    final int GROUND_LEFT_X = 0;
    final int GROUND_LEFT_Y = 0;
    final int GROUND_LEFT_WIDTH = 500;
    final int GROUND_LEFT_HEIGHT = 225;

    int bgX = 1920;
    int bgY = 1080;

    boolean inGame;

    public enum PlayerState{JUMPING, ON_PLATFORM}
    public enum PlatformState{FALLING, REMOVE}

    private class Collidiable
    {
        float x, y;
        final float width, height;
        Collidiable(float width, float height)
        {
            this.width = width; this.height = height;
        }

        Rectangle getBounds() {return new Rectangle((int)x, (int)y, (int)width, (int)height);}
    }

    private class Player extends Collidiable
    {
        PlayerState state = PlayerState.JUMPING;
        final int id;
        boolean jump = false;
        float movX = 0;
        Collidiable platform = null;

        Player(int id, float x, float y)
        {
            super(PlAYER_WIDTH, PLAYER_HEIGHT);
            this.id = id;
            this.x = x;
            this.y = y;
            ModelHandler.instance.onModelEvent(new G2_NewObject(id, true, x, y));
        }

        final int JUMP_DURATION = 75;
        final double JUMP_HEIGHT = 200;
        int jumpTicks = JUMP_DURATION;

        private double heightAt(double x)
        {
            return Math.sin(x/(JUMP_DURATION/Math.PI))*JUMP_HEIGHT;
            // TODO fix the fancy curve
            //if (x < JUMP_DURATION/2.0) return Math.sin(x/(JUMP_DURATION/Math.PI))*JUMP_HEIGHT;
            //else return (Math.sin((x-(JUMP_DURATION/4.0))/(JUMP_DURATION/Math.PI))*(JUMP_HEIGHT/2))+JUMP_HEIGHT/2;
        }

        private boolean isGoodIntersect(Rectangle platform)
        {
            if (getBounds().intersects(platform) == false) return false;
            float centerX = x + width/2, centerY = y + height/2;
            float pointX = platform.x-centerX, pointY = platform.y-centerY;
            if (pointY < 0) return false;
        }

        public void update()
        {
            if (jump && state != PlayerState.JUMPING)
            {
                state = PlayerState.JUMPING;
                jumpTicks = 1;
                ModelHandler.instance.onModelEvent(new G2_PlayerStateChange(G2_PlayerStateChange.State.JUMP, id));
            }
            if (state == PlayerState.JUMPING)
            {
                x += movX*4;
                y += heightAt(jumpTicks)-heightAt(jumpTicks-1);
                ModelHandler.instance.onModelEvent(new G2_ObjectMove(id, true, x, y));
                if (++jumpTicks > JUMP_DURATION) {
                    jumpTicks--;
                }
                for (Platform platform : platforms)
                    if (getBounds().intersects(platform.getBounds())) {
                        this.platform = platform;
                        state = PlayerState.ON_PLATFORM;
                        y = platform.y + platform.height;
                        break;
                    }
            }
            else
            {
                x += movX * 5;
                y = platform.y + platform.height;
            }

            x = MathExtended.clamp(x, 0, WORLD_WIDTH_BOUND-PlAYER_WIDTH);
            y = MathExtended.clamp(y, -100, WORLD_HEIGHT_BOUND-PLAYER_HEIGHT);
            ModelHandler.instance.onModelEvent(new G2_ObjectMove(id, true, x, y));
            jump = false;
        }

        public Rectangle getBounds() {
            return new Rectangle((int)x, (int)y, PlAYER_WIDTH, PLAYER_HEIGHT);
        }
    }

    ArrayList<Platform> platforms;

    int idCounter = 0;
    protected class Platform extends Collidiable
    {
        float fallingSpeed;
        final int platformId = idCounter++;
        PlatformState state = PlatformState.FALLING;
        final boolean canMoveTrough;

        Platform(float x, float y, float width, float height, float fallingSpeed, boolean canMoveTrough) {
            super(width, height);
            this.x = x;
            this.y = y;
            this.fallingSpeed = fallingSpeed;
            this.canMoveTrough = canMoveTrough;
        }

        public void update() {
            y -= fallingSpeed;
            if (y < -height)
                state = PlatformState.REMOVE;
        }
    }

    Player[] players = new Player[PLAYER_COUNT];

    public Game_2_Model()
    {
        platforms = new ArrayList<>();
    }

    @Override
    public void start() {
        inGame = true;
        for (int i = 0; i < PLAYER_COUNT; i++)
            players[i] = new Player(i, 100+75*i, 700);

    }

    Random rand = new Random(System.currentTimeMillis());

    @Override
    public void update() {
        players[0].update();
        players[1].update();

        platforms.get(0).update();
        platforms.get(1).update();
        platforms.get(2).update();

        checkCollision();
    }

    @Override
    public void close() {

    }

    public int getTime() {
        return time;
    }

    public void checkCollision() {
        Rectangle pl1 = players[0].getBounds();
        Rectangle pl2 = players[1].getBounds();
        Rectangle bl1 = platforms.get(0).getBounds();
        Rectangle bl2 = platforms.get(1).getBounds();
        Rectangle bl3 = platforms.get(2).getBounds();

        if (pl1.intersects(bl1)) {
            players[0].movX = 0;
        }

    }

    public void setMoveHorizontal(float move, int player)
    {
        if (player > 1) return; // TODO proper warnings
        if (Math.abs(move)>1) return;
        players[player].movX = move;
    }

    public void setJump(boolean pressed, int player) {
        if (player > 1) return;
        players[player].jump = pressed;
    }
}
