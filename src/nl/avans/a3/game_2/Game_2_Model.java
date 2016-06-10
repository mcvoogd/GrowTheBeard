package nl.avans.a3.game_2;
import com.sun.org.apache.xpath.internal.operations.Mod;
import nl.avans.a3.event.NewModel;
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

    final int BLOCK_WIDTH = 160;
    final int BLOCK_HEIGHT = 50;
    final float BLOCK_FALL_SPEED = -3;

    final int WORLD_HEIGHT_LOW_BOUND = (-PLAYER_HEIGHT-BLOCK_HEIGHT)*2;

    final int GROUND_LEFT_X = -500;
    final int GROUND_LEFT_Y = -245;
    final int GROUND_LEFT_WIDTH = 1000;
    final int GROUND_LEFT_HEIGHT = 470;

    final int PLAYER_SPAWN_Y = GROUND_LEFT_Y+GROUND_LEFT_HEIGHT+1;
    final int PLAYER_SPAWN_X_1 = PlAYER_WIDTH;
    final int PLAYER_SPAWN_X_2 = (int)(PlAYER_WIDTH*3);

    final int GROUND_RIGHT_X = 1400;
    final int GROUND_RIGHT_Y = -275;
    final int GROUND_RIGHT_WIDTH = 1000;
    final int GROUND_RIGHT_HEIGHT = 500;

    final int BLOCK_SPAWN_Y = WORLD_HEIGHT_BOUND+BLOCK_HEIGHT;
    final int BLOCK_SPAWN_Y_SECTION_COUNT = 9;
    final int BLOCK_SPAWN_Y_SECTION = (BLOCK_SPAWN_Y-WORLD_HEIGHT_LOW_BOUND)/BLOCK_SPAWN_Y_SECTION_COUNT;
    final int BLOCK_SPAWN_X_BASE = GROUND_LEFT_X+GROUND_LEFT_WIDTH;
    final int BLOCK_SPAWN_X_SECTION = (GROUND_RIGHT_X-BLOCK_SPAWN_X_BASE)/3;
    final int BLOCK_SPAWN_X_1 = BLOCK_SPAWN_X_BASE+BLOCK_SPAWN_X_SECTION*0+((BLOCK_SPAWN_X_SECTION-BLOCK_WIDTH)/2);
    final int BLOCK_SPAWN_X_2 = BLOCK_SPAWN_X_BASE+BLOCK_SPAWN_X_SECTION*1+((BLOCK_SPAWN_X_SECTION-BLOCK_WIDTH)/2);
    final int BLOCK_SPAWN_X_3 = BLOCK_SPAWN_X_BASE+BLOCK_SPAWN_X_SECTION*2+((BLOCK_SPAWN_X_SECTION-BLOCK_WIDTH)/2);

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
        Platform platform = null;
        final float spawnX,  spawnY;

        Player(int id, float x, float y)
        {
            super(PlAYER_WIDTH, PLAYER_HEIGHT);
            this.id = id;
            spawnX = this.x = x;
            spawnY= this.y = y;
            ModelHandler.instance.onModelEvent(new G2_NewObject(id, true, x, y));
        }

        final int JUMP_DURATION = 60;
        final double JUMP_HEIGHT = 200;
        int jumpTicks = JUMP_DURATION;

        private double heightAt(double x)
        {
            return Math.sin(x/(JUMP_DURATION/Math.PI))*JUMP_HEIGHT;
            // TODO fix the fancy curve
            //if (x < JUMP_DURATION/2.0) return Math.sin(x/(JUMP_DURATION/Math.PI))*JUMP_HEIGHT;
            //else return (Math.sin((x-(JUMP_DURATION/4.0))/(JUMP_DURATION/Math.PI))*(JUMP_HEIGHT/2))+JUMP_HEIGHT/2;
        }

        private boolean isGoodIntersect(Rectangle platform) // assumes the rectangle already intercepts
        {
            Rectangle self = getBounds();
            if (platform.y+platform.height-self.y > 20) return false; // we are to high
            if (self.x + self.width < platform.x) return false; // we are to far left
            if (platform.x+platform.width < self.x) return false; // we are to far right
            return true;
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
                        if (isGoodIntersect(platform.getBounds()) && platform.state == PlatformState.FALLING) {
                            this.platform = platform;
                            state = PlayerState.ON_PLATFORM;
                            ModelHandler.instance.onModelEvent(new G2_PlayerStateChange(G2_PlayerStateChange.State.WALK, id));
                            y = platform.y + platform.height;
                            break;
                        }
                        else if (platform.canMoveTrough == false)
                        {
                            Rectangle self = getBounds();
                            Rectangle platformBounds = platform.getBounds();
                            if (self.x + self.width < platformBounds.x + platformBounds.width/2)
                            {
                                x = platformBounds.x - self.width - 1;
                            }
                            else
                            {
                                x = platformBounds.x + platformBounds.width + 1;
                            }
                        }
                    }
            }
            else
            {
                if (platform.state == PlatformState.REMOVE)
                {
                    state = PlayerState.JUMPING;
                    update();
                }
                else {
                    x += movX * 5;
                    y = platform.y + platform.height;
                    if (x + width < platform.x || platform.x + platform.width < x) {
                        state = PlayerState.JUMPING;
                        ModelHandler.instance.onModelEvent(new G2_PlayerStateChange(G2_PlayerStateChange.State.JUMP, id));
                        jumpTicks = JUMP_DURATION / 2;
                    }
                }
            }

            x = MathExtended.clamp(x, 0, WORLD_WIDTH_BOUND-PlAYER_WIDTH);
            y = MathExtended.clamp(y, WORLD_HEIGHT_LOW_BOUND*2, WORLD_HEIGHT_BOUND-PLAYER_HEIGHT/2);
            if (y <= WORLD_HEIGHT_LOW_BOUND)
            {
                state = PlayerState.JUMPING;
                x = spawnX;
                y = spawnY;
            }

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
        float fallingSpeed;
        public PlatformState state = PlatformState.FALLING;
        final boolean canMoveTrough;

        Platform(float x, float y, float width, float height, float fallingSpeed, boolean canMoveTrough) {
            super(width, height);
            this.x = x;
            this.y = y;
            this.fallingSpeed = fallingSpeed;
            this.canMoveTrough = canMoveTrough;
        }

        public void update() {
            if (state == PlatformState.REMOVE)
            {
                state = PlatformState.FALLING;
                y = BLOCK_SPAWN_Y;
            }

            y += fallingSpeed;
            if (y <= WORLD_HEIGHT_LOW_BOUND)
                state = PlatformState.REMOVE;
        }
    }


    int idCounter = 0;
    protected class Woodblock extends Platform
    {
        int id = idCounter++;
        Woodblock(float x, float y) {
            super(x, y, BLOCK_WIDTH, BLOCK_HEIGHT, BLOCK_FALL_SPEED , true);
            ModelHandler.instance.onModelEvent(new G2_NewObject(id, false, x, y));
        }

        public void update()
        {
            super.update();
            ModelHandler.instance.onModelEvent(new G2_ObjectMove(id, false, x, y));
        }
    }

    Player[] players = new Player[PLAYER_COUNT];

    public Game_2_Model()
    {
        platforms = new ArrayList<>();
    }

    @Override
    public void start() {
        players[0] = new Player(0, PLAYER_SPAWN_X_1, PLAYER_SPAWN_Y);
        players[1] = new Player(1, PLAYER_SPAWN_X_2, PLAYER_SPAWN_Y);

        platforms.add(new Platform(GROUND_LEFT_X, GROUND_LEFT_Y, GROUND_LEFT_WIDTH, GROUND_LEFT_HEIGHT, 0, false));
        platforms.add(new Platform(GROUND_RIGHT_X, GROUND_RIGHT_Y, GROUND_RIGHT_WIDTH, GROUND_RIGHT_HEIGHT, 0, false));

        platforms.add(new Woodblock(BLOCK_SPAWN_X_2, WORLD_HEIGHT_LOW_BOUND+BLOCK_SPAWN_Y_SECTION*9));
        platforms.add(new Woodblock(BLOCK_SPAWN_X_3, WORLD_HEIGHT_LOW_BOUND+BLOCK_SPAWN_Y_SECTION*8));
        platforms.add(new Woodblock(BLOCK_SPAWN_X_1, WORLD_HEIGHT_LOW_BOUND+BLOCK_SPAWN_Y_SECTION*7));
        platforms.add(new Woodblock(BLOCK_SPAWN_X_2, WORLD_HEIGHT_LOW_BOUND+BLOCK_SPAWN_Y_SECTION*7));
        platforms.add(new Woodblock(BLOCK_SPAWN_X_2, WORLD_HEIGHT_LOW_BOUND+BLOCK_SPAWN_Y_SECTION*6));
        platforms.add(new Woodblock(BLOCK_SPAWN_X_3, WORLD_HEIGHT_LOW_BOUND+BLOCK_SPAWN_Y_SECTION*6));
        platforms.add(new Woodblock(BLOCK_SPAWN_X_2, WORLD_HEIGHT_LOW_BOUND+BLOCK_SPAWN_Y_SECTION*5));
        platforms.add(new Woodblock(BLOCK_SPAWN_X_1, WORLD_HEIGHT_LOW_BOUND+BLOCK_SPAWN_Y_SECTION*4));
        platforms.add(new Woodblock(BLOCK_SPAWN_X_3, WORLD_HEIGHT_LOW_BOUND+BLOCK_SPAWN_Y_SECTION*4));
        platforms.add(new Woodblock(BLOCK_SPAWN_X_2, WORLD_HEIGHT_LOW_BOUND+BLOCK_SPAWN_Y_SECTION*3));
        platforms.add(new Woodblock(BLOCK_SPAWN_X_2, WORLD_HEIGHT_LOW_BOUND+BLOCK_SPAWN_Y_SECTION*2));
        platforms.add(new Woodblock(BLOCK_SPAWN_X_3, WORLD_HEIGHT_LOW_BOUND+BLOCK_SPAWN_Y_SECTION*2));
        platforms.add(new Woodblock(BLOCK_SPAWN_X_1, WORLD_HEIGHT_LOW_BOUND+BLOCK_SPAWN_Y_SECTION));
        platforms.add(new Woodblock(BLOCK_SPAWN_X_3, WORLD_HEIGHT_LOW_BOUND+BLOCK_SPAWN_Y_SECTION));
    }

    Random rand = new Random(System.currentTimeMillis());

    @Override
    public void update() {
        platforms.forEach(platform -> platform.update());
        players[0].update();
        players[1].update();
    }

    @Override
    public void close() {

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
