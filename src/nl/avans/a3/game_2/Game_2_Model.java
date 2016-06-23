package nl.avans.a3.game_2;
import javafx.util.Pair;
import nl.avans.a3.event.NewModel;
import nl.avans.a3.mvc_handlers.ModelHandler;
import nl.avans.a3.mvc_interfaces.Model;
import nl.avans.a3.util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Game_2_Model implements Model {
    final int PLAYER_COUNT = 2;
    final int WORLD_WIDTH_BOUND = 1920;
    final int WORLD_HEIGHT_BOUND = 1080;

    final int PLAYER_HEIGHT = 267;
    final int PLAYER_WIDTH = 80;

    final int PLAYER_RESPAWN_TICKS = 3*60;

    final int BLOCK_WIDTH = 160;
    final int BLOCK_HEIGHT = 50;
    final float BLOCK_FALL_SPEED = -3;

    final int WORLD_HEIGHT_LOW_BOUND = (-PLAYER_HEIGHT-BLOCK_HEIGHT)*1;

    final int GAME_DURATION = 45;

    final int GROUND_LEFT_X = -500;
    final int GROUND_LEFT_Y = -245;
    final int GROUND_LEFT_WIDTH = 1000;
    final int GROUND_LEFT_HEIGHT = 470;

    final int PLAYER_SPAWN_Y = GROUND_LEFT_Y+GROUND_LEFT_HEIGHT+1;
    final int PLAYER_SPAWN_X_1 = PLAYER_WIDTH;
    final int PLAYER_SPAWN_X_2 = (int)(PLAYER_WIDTH*3);

    final int GROUND_RIGHT_X = 1400;
    final int GROUND_RIGHT_Y = -275;
    final int GROUND_RIGHT_WIDTH = 1000;
    final int GROUND_RIGHT_HEIGHT = 500;

    final int BLOCK_SPAWN_Y = WORLD_HEIGHT_BOUND+BLOCK_HEIGHT;
    final int BLOCK_SPAWN_Y_SECTION_COUNT = 9;
    final int BLOCK_SPAWN_Y_SECTION = (BLOCK_SPAWN_Y-WORLD_HEIGHT_LOW_BOUND)/BLOCK_SPAWN_Y_SECTION_COUNT;
    final int BLOCK_SPAWN_X_BASE = GROUND_LEFT_X+GROUND_LEFT_WIDTH;
    final int BLOCK_SPAWN_X_SECTION = (GROUND_RIGHT_X-BLOCK_SPAWN_X_BASE)/3;
    final int BLOCK_SPAWN_X_1 = BLOCK_SPAWN_X_BASE + ((BLOCK_SPAWN_X_SECTION - BLOCK_WIDTH) / 2);
    final int BLOCK_SPAWN_X_2 = BLOCK_SPAWN_X_BASE+ BLOCK_SPAWN_X_SECTION +((BLOCK_SPAWN_X_SECTION-BLOCK_WIDTH)/2);
    final int BLOCK_SPAWN_X_3 = BLOCK_SPAWN_X_BASE+BLOCK_SPAWN_X_SECTION*2+((BLOCK_SPAWN_X_SECTION-BLOCK_WIDTH)/2);


    final int BASKET_WIDTH = 125;
    final int BASKET_HEIGHT = 70;
    final int BASKET_X = GROUND_RIGHT_X+GROUND_RIGHT_WIDTH/4-BASKET_WIDTH/2;
    final int BASKET_Y = 225;


    final int WOODSTACK_WIDTH = BASKET_WIDTH;
    final int WOODSTACk_HEIGHT = BASKET_HEIGHT;
    final int WOODSTACK_X = GROUND_LEFT_X+(GROUND_LEFT_WIDTH/4)*3-WOODSTACK_WIDTH/2;
    final int WOODSTACK_Y = BASKET_Y;

    int time = GAME_DURATION;

    boolean inGame = true;

    public enum PlayerState{JUMPING, ON_PLATFORM, RESPAWN_WAIT}
    public enum PlatformState{FALLING, REMOVE}

    public enum ModelState{PRE_SCREEN, GAME, WINSCREEN}
    private ModelState state = ModelState.PRE_SCREEN;
    public ModelState getState() {return state;}
    private SoundPlayer scoreMusic;

    private class Collidable{
        float x, y;
        final float width, height;
        Collidable(float width, float height){
            this.width = width; this.height = height;
        }

        Rectangle getBounds() {return new Rectangle((int)x, (int)y, (int)width, (int)height);}
    }

    private class Player extends Collidable{
        PlayerState state = PlayerState.JUMPING;
        final int id;
        boolean jump = false;
        int score;
        float movX = 0;
        Platform platform = null;
        final float spawnX,  spawnY;
        boolean hasBlock = false;
        int repawnTicksLeft = 0;

        Player(int id, float x, float y){
            super(PLAYER_WIDTH, PLAYER_HEIGHT);
            this.id = id;
            spawnX = this.x = x;
            spawnY= this.y = y;
            ModelHandler.instance.onModelEvent(new G2_NewObject(id, true, x, y));
        }

        final int JUMP_DURATION = 60;
        final double JUMP_HEIGHT = 200;
        int jumpTicks = JUMP_DURATION;

        private double heightAt(double x){
            return Math.sin(x/(JUMP_DURATION/Math.PI))*JUMP_HEIGHT;
        }

        private boolean isGoodIntersect(Rectangle platform) // assumes the rectangle already intercepts
        {
            Rectangle self = getBounds();
            if (platform.y+platform.height-self.y > 20) return false; // we are to high
            if (self.x + self.width < platform.x) return false; // we are to far left
            if (platform.x+platform.width < self.x) return false; // we are to far right
            return true; // otherwise we have a good intersect
        }

        public void update()
        {
            if (state == PlayerState.RESPAWN_WAIT) {
                if (repawnTicksLeft-- <= 0) {
                    state = PlayerState.JUMPING;
                    x = spawnX;
                    y = spawnY;
                }
                else return;
            }
            if (jump && state != PlayerState.JUMPING){
                state = PlayerState.JUMPING;
                jumpTicks = 1;
                ModelHandler.instance.onModelEvent(new G2_PlayerStateChange(G2_PlayerStateChange.State.JUMP, id));
            }
            if (state == PlayerState.JUMPING){
                x += movX*4;
                y += heightAt(jumpTicks)-heightAt(jumpTicks-1);
                ModelHandler.instance.onModelEvent(new G2_ObjectMove(id, true, x, y));
                if (++jumpTicks > JUMP_DURATION) {
                    jumpTicks--;
                }
                for (Platform platform : platforms){
                    if(getBounds().intersects(platform.getBounds())){
                        if(isGoodIntersect(platform.getBounds()) && platform.state == PlatformState.FALLING){
                            this.platform = platform;
                            state = PlayerState.ON_PLATFORM;
                            ModelHandler.instance.onModelEvent(new G2_PlayerStateChange(G2_PlayerStateChange.State.WALK, id));
                            y = platform.y + platform.height;
                            break;
                        }else if(!platform.canMoveTrough){
                            Rectangle self = getBounds();
                            Rectangle platformBounds = platform.getBounds();
                            if(self.x + self.width < platformBounds.x + platformBounds.width / 2){
                                x = platformBounds.x - self.width - 1;
                            }else{
                                x = platformBounds.x + platformBounds.width + 1;
                            }
                        }
                    }
                }
            }else{
                if (platform.state == PlatformState.REMOVE){
                    state = PlayerState.JUMPING;
                    update();
                }else{
                    x += movX * 5;
                    y = platform.y + platform.height;
                    if (x + width < platform.x || platform.x + platform.width < x) {
                        state = PlayerState.JUMPING;
                        ModelHandler.instance.onModelEvent(new G2_PlayerStateChange(G2_PlayerStateChange.State.JUMP, id));
                        jumpTicks = JUMP_DURATION / 2;
                    }
                }
            }


            if (basket.getBounds().intersects(getBounds()))
            {
                hasBlock = true;
                ModelHandler.instance.onModelEvent(new G2_Player_Block(id, true));
            }
            if (woodStack.getBounds().intersects(getBounds()) && hasBlock) {
                ModelHandler.instance.onModelEvent(new G2_PointScored());
                ModelHandler.instance.onModelEvent(new G2_Player_Block(id, false));
                score++;
                hasBlock = false;
                Logger.instance.log("G2001", getScores().toString());
            }

            x = MathExtended.clamp(x, 0, WORLD_WIDTH_BOUND-PLAYER_WIDTH);
            y = MathExtended.clamp(y, WORLD_HEIGHT_LOW_BOUND*2, WORLD_HEIGHT_BOUND-PLAYER_HEIGHT/2);
            if (y <= WORLD_HEIGHT_LOW_BOUND){
                ModelHandler.instance.onModelEvent(new G2_PlayerFallen());
                state = PlayerState.RESPAWN_WAIT;
                repawnTicksLeft = PLAYER_RESPAWN_TICKS;
                hasBlock = false;
                ModelHandler.instance.onModelEvent(new G2_Player_Block(id, false));
            }

            ModelHandler.instance.onModelEvent(new G2_ObjectMove(id, true, x, y));
            jump = false;
        }

        public Rectangle getBounds() {
            return new Rectangle((int)x, (int)y, PLAYER_WIDTH, PLAYER_HEIGHT);
        }
        public boolean getHasBlock() { return hasBlock; }
        public void setHasBlock(boolean hasBlock) { this.hasBlock = hasBlock; }
        public int getScore() { return score; }
    }

    ArrayList<Platform> platforms;

    protected class Platform extends Collidable{
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
    protected class Woodblock extends Platform{
        int id = idCounter++;
        Woodblock(float x, float y) {
            super(x, y, BLOCK_WIDTH, BLOCK_HEIGHT, BLOCK_FALL_SPEED , true);
            this.x = x;
            this.y = y;
            ModelHandler.instance.onModelEvent(new G2_NewObject(id, false, x, y));
        }

        public void update()
        {
            super.update();
            ModelHandler.instance.onModelEvent(new G2_ObjectMove(id, false, x, y));
        }
    }

    Player[] players = new Player[PLAYER_COUNT];

    protected class Basket extends Collidable{

        Basket() {
            super(BASKET_WIDTH, BASKET_HEIGHT);
            this.x = BASKET_X;
            this.y = BASKET_Y;
        }
    }
    Basket basket;

    protected class WoodStack extends Collidable{

        WoodStack() {
            super(WOODSTACK_WIDTH, WOODSTACk_HEIGHT);
            this.x = WOODSTACK_X;
            this.y = WOODSTACK_Y;
        }
    }
    WoodStack woodStack;
    
    public Game_2_Model()
    {
        platforms = new ArrayList<>();
        scoreMusic = new SoundPlayer("res/music/theme_song.wav");
    }

    private boolean hasStarted = false;

    @Override
    public void start() {
        if (state != ModelState.GAME) return;
        hasStarted = true;

        new Timer(time * 1000, e -> {
            inGame = false;
            state = ModelState.WINSCREEN;
            scoreMusic.loop(20);
            if (getScores().getKey() > getScores().getValue()) Beard.beardPlayer1++;
            else if (getScores().getKey() < getScores().getValue()) Beard.beardPlayer2 ++;
        }).start();
        Timer viewTimer = new Timer(1000, e -> time--);
        viewTimer.start();

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


        basket = new Basket();
        woodStack = new WoodStack();
    }

    @Override
    public void update() {
        if (state == ModelState.GAME) {
            platforms.forEach(Platform::update);
            players[0].update();
            players[1].update();
        }
    }

    @Override
    public void close() {
            scoreMusic.stop();
    }

    public void setGameStart()
    {
        if (state == ModelState.PRE_SCREEN)
            state = ModelState.GAME;
        ModelHandler.instance.onModelEvent(new NewModel(this, this));
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

    public int getTime() {
        return time;
    }

    public boolean getInGame() {
        return inGame;
    }

    public Pair<Integer, Integer> getScores()
    {
        return new Pair<>(players[0].getScore(), players[1].getScore());
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }
}
