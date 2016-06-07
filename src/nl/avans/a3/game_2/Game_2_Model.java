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

    private Timer gameTimer;
    private Timer viewTimer;
    private int time = 30;

    int bgX = 1920;
    int bgY = 1080;

    boolean inGame;

    public enum PlayerState{JUMPING, ON_KINETIC}
    public enum PlatformState{FALLING, REMOVE}

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

    boolean intersects(Collidiable a, Collidiable b)
    {
        return a.x < b.x + b.width && a.x + a.width > b.x && a.y < b.y + b.height && a.y + a.height < b.y;
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
            return Math.sin(x/(JUMP_DURATION/Math.PI))*JUMP_HEIGHT;
            // TODO fix the fancy curve
            //if (x < JUMP_DURATION/2.0) return Math.sin(x/(JUMP_DURATION/Math.PI))*JUMP_HEIGHT;
            //else return (Math.sin((x-(JUMP_DURATION/4.0))/(JUMP_DURATION/Math.PI))*(JUMP_HEIGHT/2))+JUMP_HEIGHT/2;
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
                    state = PlayerState.ON_KINETIC;
                    ModelHandler.instance.onModelEvent(new G2_PlayerStateChange(G2_PlayerStateChange.State.WALK, id));
                }
            }
            else
            {
                if (movX != 0) {
                    x += movX * 5;
                    // TODO handle the platform that you're standing on
                }
            }

            x = MathExtended.clamp(x, 0, WORLD_WIDTH_BOUND-PlAYER_WIDTH-2f);
            ModelHandler.instance.onModelEvent(new G2_ObjectMove(id, true, x, y));
            jump = false;
        }

        public Rectangle getBounds() {
            return new Rectangle((int)x, (int)y, PlAYER_WIDTH, PLAYER_HEIGHT);
        }
    }

    ArrayList<Platform> platforms;

    protected class Platform extends Collidiable
    {
        PlatformState state;
        boolean falling = false;
        float x, y;
        BufferedImage image;

        Platform(float x, float y) {
            super(BLOCK_WIDTH, BLOCK_HEIGHT, true);
            this.x = x;
            this.y = y;
            image = ResourceHandler.getImage("res/images_game2/wood.png");
        }

        final int FALL_DURATION = 500;
        int fallTicks = 0;

        public void update() {
            if (falling) {
                for (int i = 0; i <= 2; i++) {
                    platforms.get(i).y--;
                    if (platforms.get(i).y < 100) {
                        platforms.get(i).y = 1000;
                    }
                }
            }
            else {

            }
        }

        public Rectangle getBounds() {
            return new Rectangle((int)x, (int)y, BLOCK_WIDTH, BLOCK_HEIGHT);
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
            players[i] = new Player(i, 100+75*i, 500);
        for (int i = 0; i <= 2; i++)
            platforms.add(new Platform(550 + 300 * i, 500 + 100 * i));
        gameTimer = new Timer(time * 1000, e -> {
            inGame = false;
        });
        gameTimer.start();
        viewTimer = new Timer(1000, e -> {
           time--;
        });
        viewTimer.start();

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
