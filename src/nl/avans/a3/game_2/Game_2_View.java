package nl.avans.a3.game_2;

import nl.avans.a3.event.ModelEvent;
import nl.avans.a3.event.NewModel;
import nl.avans.a3.mvc_handlers.ModelHandler;
import nl.avans.a3.mvc_interfaces.View;
import nl.avans.a3.util.Beard;
import nl.avans.a3.util.EasyTransformer;
import nl.avans.a3.util.ResourceHandler;
import nl.avans.a3.util.SoundPlayer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.sound.sampled.*;

public class Game_2_View implements View {
    private Game_2_Model model;
    private BufferedImage[] waterfallAnimation;
    private BufferedImage banner;
    final int framesPerAnimationFrame = 40;
    private int waterfallIndex = 0;
    private BufferedImage[] winner;
    private BufferedImage winnerImage;
    private BufferedImage text;
    private BufferedImage[] playerImage;
    private BufferedImage playerImages;
    private SoundPlayer scoredPointSound;
    private SoundPlayer playerFallenSounds;
    private BufferedImage woodStack;
    private Clip backgroundMusicAsClip;

    private int preScreen = 2; //TODO PRE SCREEN

    public Game_2_View(Game_2_Model model){
        this.model = model;
        scoredPointSound = new SoundPlayer("res/music/game2/wood_drop.wav");
        playerFallenSounds = new SoundPlayer(new String[]{
                "res/music/game2/fall_1.wav",
                "res/music/game2/fall_2.wav",
                "res/music/game2/fall_3.wav"
        });
    }

    private class Player{
        final int ANIMATION_LENGTH = 1;

        float x, y;
        BufferedImage[] animation;
        BufferedImage[] animationArm;
        BufferedImage beard;
        int selectedAnimation = 0;
        int animationTicksLeft = -1;
        boolean hasBlock = false;

        Player(float x, float y, BufferedImage playerImage, int id){
            this.x = x;
            this.y = y;
            animation = new BufferedImage[4];
            animationArm = new BufferedImage[4];
            int beardNumber;
            if(id == 0){
                beardNumber = Beard.beardPlayer1;
            }else{
                beardNumber = Beard.beardPlayer2;
            }
            beard = ResourceHandler.getImage("res/images_game2/beard.png").getSubimage(playerImage.getWidth() / 8 * beardNumber, 0, playerImage.getWidth() / 8, playerImage.getHeight());
            for (int i = 0 ; i < 4; i++) {
                animation[i] = playerImage.getSubimage(playerImage.getWidth() / 8 * (i * 2), 0, playerImage.getWidth() / 8, playerImage.getHeight());
                animationArm[i] = playerImage.getSubimage(playerImage.getWidth() / 8 * ((i * 2) + 1), 0, playerImage.getWidth() / 8, playerImage.getHeight());
            }
        }
    }

    private ArrayList<Player> players = new ArrayList<>();

    BufferedImage[] platfrom_images;
    private class Platform {
        float x, y;
        int animationCount;

        Platform(float x, float y, int animationCount) {
            this.x = x;
            this.y = y;
            this.animationCount = animationCount;
        }

        BufferedImage getImage()
        {
            animationCount = ((animationCount+1)%(4*framesPerAnimationFrame));
            return platfrom_images[animationCount/framesPerAnimationFrame];
        }
    }

    private HashMap<Integer, Platform> platforms = new HashMap<>();

    @Override
    public void start() {
        winner = new BufferedImage[3];
        text = ResourceHandler.getImage("res/images_scoreboard/text.png");
        winnerImage = ResourceHandler.getImage("res/images_scoreboard/winner.png");
        playerImage = new BufferedImage[2];
        playerImages = ResourceHandler.getImage("res/images_scoreboard/person.png");

        try {
            backgroundMusicAsClip = AudioSystem.getClip();
            backgroundMusicAsClip.open(AudioSystem.getAudioInputStream(new File("res/music/game2/waterfall.wav")));
            backgroundMusicAsClip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < 2; i++)
        {
            playerImage[i] = playerImages != null ? playerImages.getSubimage(311 * i, 0, 311, 577) : null;
        }
        for(int i = 0; i < 3; i++){
            winner[i] = winnerImage.getSubimage(0, (242 * i), winnerImage.getWidth(), 726/3);
        }

        waterfallAnimation = new BufferedImage[3];
        BufferedImage image = ResourceHandler.getImage("res/images_game2/background.png");
        for(int i = 0; i < 3; i++)
        {
            waterfallAnimation[i] = image != null ? image.getSubimage(0, 1080 * i, 1920, 1080) : null;
        }
        image = ResourceHandler.getImage("res/images_game2/wood.png");
        platfrom_images = new BufferedImage[4];
        for (int i = 0; i < 4; i++)
        {
            if(image != null){
                platfrom_images[i] = image.getSubimage(i*(image.getWidth()/4), 0, image.getWidth()/4, image.getHeight());
            }
        }

        banner = ResourceHandler.getImage("res/images_game1/banner.png");
        woodStack = ResourceHandler.getImage("res/images_game2/item.png");
    }

    @Override
    public void draw(Graphics2D g) {
        if (model.getInGame()) {
            waterfallIndex = (waterfallIndex + 1) % (waterfallAnimation.length * framesPerAnimationFrame);
            g.drawImage(waterfallAnimation[waterfallIndex / framesPerAnimationFrame], 0, 0, null);

            Font tf = new Font("Verdana", Font.BOLD, 68);
            FontMetrics ft = g.getFontMetrics(tf);

            g.setColor(new Color(159, 44, 22));
            g.setFont(tf);

            for (Platform platform : platforms.values()) {
                final int PLATFORM_X_OFFSET = -20;
                g.drawImage(platform.getImage(), (int) platform.x + PLATFORM_X_OFFSET, 1080 - (int) platform.y - model.BLOCK_HEIGHT, null);
            }

            for (Player player : players) {
                if (player.animationTicksLeft-- == 0 && player.selectedAnimation < 3) {
                    player.animationTicksLeft = player.ANIMATION_LENGTH;
                    player.selectedAnimation++;
                }

                final int PLAYER_X_OFFSET = -53;

                g.drawImage(player.animation[player.selectedAnimation], (int) player.x + PLAYER_X_OFFSET, 1080 - (int) player.y - model.PLAYER_HEIGHT, null);
                g.drawImage(player.animation[player.selectedAnimation], (int) player.x + PLAYER_X_OFFSET, 1080 - (int) player.y - model.PLAYER_HEIGHT, null);
                g.drawImage(player.beard, (int) player.x + PLAYER_X_OFFSET, 1080 - (int) player.y - model.PLAYER_HEIGHT, null);
                g.drawImage(player.animationArm[player.selectedAnimation], (int) player.x + PLAYER_X_OFFSET, 1080 - (int) player.y - model.PLAYER_HEIGHT, null);
                if (player.hasBlock)
                    g.drawImage(woodStack, (int)(player.x - (model.WOODSTACK_WIDTH/6)), (int)(1080-player.y-model.PLAYER_HEIGHT-model.WOODSTACk_HEIGHT), null);
            }


            g.drawImage(banner, 0, -25, 1920, 180, null);
            g.drawString("" + model.getTime(), 960 - (ft.stringWidth("" + model.getTime()) / 2) + 90, 80);

            g.drawImage(woodStack, model.BASKET_X, 1080-model.BASKET_Y-model.BASKET_HEIGHT, null);
            g.drawImage(woodStack, model.WOODSTACK_X, 1080-model.WOODSTACK_Y-model.WOODSTACk_HEIGHT, null);

            if (ModelHandler.DEBUG_MODE == false) return;

            g.setColor(Color.YELLOW);
            for (Platform platform : platforms.values())
                g.drawRect((int) platform.x, 1080 - (int) platform.y - model.BLOCK_HEIGHT, model.BLOCK_WIDTH, model.BLOCK_HEIGHT);
            g.setColor(Color.PINK);
            for (Player player : players)
                g.drawRect((int) player.x, 1080 - (int) player.y - model.PLAYER_HEIGHT, model.PLAYER_WIDTH, model.PLAYER_HEIGHT);
            g.setColor(Color.CYAN);
            g.drawRect(model.GROUND_LEFT_X, 1080 - model.GROUND_LEFT_Y - model.GROUND_LEFT_HEIGHT, model.GROUND_LEFT_WIDTH, model.GROUND_LEFT_HEIGHT);
            g.drawRect(model.GROUND_RIGHT_X, 1080 - model.GROUND_RIGHT_Y - model.GROUND_RIGHT_HEIGHT, model.GROUND_RIGHT_WIDTH, model.GROUND_RIGHT_HEIGHT);
            g.setColor(Color.GREEN);
            g.drawRect(model.BASKET_X,1080- model.BASKET_Y-model.BASKET_HEIGHT, model.BASKET_WIDTH, model.BASKET_HEIGHT);
            g.drawRect(model.WOODSTACK_X, 1080-model.WOODSTACK_Y-model.WOODSTACk_HEIGHT, model.WOODSTACK_WIDTH, model.WOODSTACk_HEIGHT);
        }
        else
        {
            backgroundMusicAsClip.stop();
            if(playerFallenSounds.isPlaying())
                playerFallenSounds.stop();
            scoredPointSound.stop();
            ModelHandler.instance.onModelEvent(new NewModel(model, model));
        }
    }


    @Override
    public void close() {
    }

    @Override
    public void onModelEvent(ModelEvent event) {
        if(!(event instanceof Game_2_Event)){
            nl.avans.a3.util.Logger.instance.log("2V001", "unexpected message", nl.avans.a3.util.Logger.LogType.WARNING);
            return;
        }
        if(event instanceof G2_NewObject){
            G2_NewObject newObject = (G2_NewObject)event;
            if (newObject.player) {
                BufferedImage image = ResourceHandler.getImage("res/images_game2/person" + (newObject.id + 1) + ".png");
                players.add(new Player(newObject.x, newObject.y, image, newObject.id));
            }
            else {
                // put new random here, FloBo worries that it will not be equally random anymore, I just don't care about such minimal errors.
                platforms.put(newObject.id, new Platform(newObject.x, newObject.y, (int) (new Random(System.currentTimeMillis()).nextFloat() * 3 * framesPerAnimationFrame)));
            }
        }
        else if(event instanceof G2_ObjectMove){
            G2_ObjectMove objectMove = (G2_ObjectMove)event;
            if (objectMove.player) {
                players.get(objectMove.id).x = objectMove.newX;
                players.get(objectMove.id).y = objectMove.newY;
            }else{
                platforms.get(objectMove.id).x = objectMove.newX;
                platforms.get(objectMove.id).y = objectMove.newY;
            }
        }else if(event instanceof G2_PlayerStateChange){
            G2_PlayerStateChange playerStateChange = (G2_PlayerStateChange)event;
            Player player = players.get(playerStateChange.id);
            if (playerStateChange.state == G2_PlayerStateChange.State.JUMP){
                player.selectedAnimation = 1;
                player.animationTicksLeft = player.ANIMATION_LENGTH;
            }else{
                player.selectedAnimation = 0;
                player.animationTicksLeft = -1;
            }
        }else if(event instanceof G2_PointScored){
            scoredPointSound.playOnce();
        }else if(event instanceof G2_PlayerFallen){
            playerFallenSounds.playRandomOnceWithSoundReduction(20); // Removed to keep little children in mind
        }
        else if (event instanceof G2_Player_Block)
        {
            players.get(((G2_Player_Block) event).id).hasBlock = ((G2_Player_Block) event).hasBlock;
        }
    }
}
