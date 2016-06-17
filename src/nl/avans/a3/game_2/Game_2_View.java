package nl.avans.a3.game_2;

import nl.avans.a3.event.ModelEvent;
import nl.avans.a3.mvc_handlers.ModelHandler;
import nl.avans.a3.mvc_interfaces.View;
import nl.avans.a3.util.Beard;
import nl.avans.a3.util.ResourceHandler;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Thijs on 2-6-2016.
 */
public class Game_2_View implements View {
    private Game_2_Model model;
    private BufferedImage[] waterfallAnimation;
    final int framesPerAnimationFrame = 40;
    private int waterfallIndex = 0;
    public Game_2_View(Game_2_Model model)
    {
        this.model = model;
    }

    private class Player
    {
        final int ANIMATION_LENGTH = 1;

        float x, y;
        BufferedImage[] animation;
        BufferedImage[] animationArm;
        BufferedImage beard;
        int selectedAnimation = 0;
        int animationTicksLeft = -1;
        Player(float x, float y, BufferedImage playerImage, int id)
        {
            this.x = x;
            this.y = y;
            animation = new BufferedImage[4];
            animationArm = new BufferedImage[4];
            int beardNumber = 0;
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

    private class Basket {
        float x, y;
        BufferedImage image;

        Basket(float x, float y) {
            this.x = x;
            this.y = y;
            image = ResourceHandler.getImage("res/images_game1/wood1.png"); // TODO mandje ipv oversized blokje
        }

        BufferedImage getImage() {
            return image;
        }
    }

    private ArrayList<Basket> baskets = new ArrayList<>();

    private class WoodStack {
        float x, y;
        BufferedImage image;

        WoodStack(float x, float y) {
            this.x = x;
            this.y = y;
            image = ResourceHandler.getImage("res/images_game1/wood2.png"); // TODO houtstapel ipv oversized blokje
        }
    }

    private ArrayList<WoodStack> woodStacks = new ArrayList<>();

    @Override
    public void start() {
        waterfallAnimation = new BufferedImage[3];
        BufferedImage image = ResourceHandler.getImage("res/images_game2/background.png");
        for(int i = 0; i < 3; i++)
        {
            waterfallAnimation[i] = image.getSubimage(0, 1080*i, 1920, 1080);
        }
        image = ResourceHandler.getImage("res/images_game2/wood.png");
        platfrom_images = new BufferedImage[4];
        for (int i = 0; i < 4; i++)
        {
            platfrom_images[i] = image.getSubimage(i*(image.getWidth()/4), 0, image.getWidth()/4, image.getHeight());
        }
    }

    @Override
    public void draw(Graphics2D g) {

        waterfallIndex = (waterfallIndex+1)%(waterfallAnimation.length*framesPerAnimationFrame);
        g.drawImage(waterfallAnimation[waterfallIndex/framesPerAnimationFrame], 0, 0, null);

        g.setColor(Color.RED);
        g.setFont(new Font("Verdana", Font.BOLD, 68));

        for (Platform platform : platforms.values()) {
            final int PLATFORM_X_OFFSET = -20;
            g.drawImage(platform.getImage(), (int) platform.x+PLATFORM_X_OFFSET, 1080-(int)platform.y - model.BLOCK_HEIGHT, null);
        }

        for (Player player : players) {
            if (player.animationTicksLeft-- == 0 && player.selectedAnimation < 3)
            {
                player.animationTicksLeft = player.ANIMATION_LENGTH;
                player.selectedAnimation++;
            }

            final int PLAYER_X_OFFSET = -53;

            g.drawImage(player.animation[player.selectedAnimation], (int) player.x+PLAYER_X_OFFSET, 1080 - (int) player.y-model.PLAYER_HEIGHT, null);
            g.drawImage(player.beard, (int) player.x+PLAYER_X_OFFSET, 1080 - (int) player.y-model.PLAYER_HEIGHT, null);
            g.drawImage(player.animationArm[player.selectedAnimation], (int) player.x+PLAYER_X_OFFSET, 1080 - (int) player.y-model.PLAYER_HEIGHT, null);
        }

        for (Basket basket : baskets) {
            g.drawImage(basket.image, (int) basket.x, (int) basket.y, null);
        }

        for (WoodStack woodStack : woodStacks) {
            g.drawImage(woodStack.image, (int) woodStack.x, (int) woodStack.y, null);
        }

        if (ModelHandler.DEBUG_MODE == false) return;

        g.setColor(Color.YELLOW);
        for (Platform platform : platforms.values())
            g.drawRect((int)platform.x, 1080-(int)platform.y-model.BLOCK_HEIGHT, model.BLOCK_WIDTH, model.BLOCK_HEIGHT);
        g.setColor(Color.PINK);
        for (Player player : players)
            g.drawRect((int)player.x, 1080-(int)player.y-model.PLAYER_HEIGHT, model.PLAYER_WIDTH, model.PLAYER_HEIGHT);
        g.setColor(Color.CYAN);
        g.drawRect(model.GROUND_LEFT_X, 1080-model.GROUND_LEFT_Y-model.GROUND_LEFT_HEIGHT, model.GROUND_LEFT_WIDTH, model.GROUND_LEFT_HEIGHT);
        g.drawRect(model.GROUND_RIGHT_X, 1080-model.GROUND_RIGHT_Y-model.GROUND_RIGHT_HEIGHT, model.GROUND_RIGHT_WIDTH, model.GROUND_RIGHT_HEIGHT);

    }

    @Override
    public void close() {
    }

    Random rand = new Random(System.currentTimeMillis());

    @Override
    public void onModelEvent(ModelEvent event) {
        if (event instanceof Game_2_Event == false)
        {
            nl.avans.a3.util.Logger.instance.log("2V001", "unexcpected message", nl.avans.a3.util.Logger.LogType.WARNING);
            return;
        }
        if (event instanceof G2_NewObject)
        {
            G2_NewObject newObject = (G2_NewObject)event;
            if (newObject.player) {
                BufferedImage image = ResourceHandler.getImage("res/images_game2/person" + (newObject.id + 1) + ".png");
                players.add(new Player(newObject.x, newObject.y, image, newObject.id));
                System.out.println("added a new player to view");
            }
            if (newObject.basket) {
                baskets.add(new Basket(newObject.x, newObject.y));
                System.out.println("added a basket to view");
            }
            if (newObject.woodStack) {
                woodStacks.add(new WoodStack(newObject.x, newObject.y));
                System.out.println("added a woodstack to view");
            }
            else {
                platforms.put(newObject.id, new Platform(newObject.x, newObject.y, (int) (rand.nextFloat() * 3 * framesPerAnimationFrame)));
            }
        }
        else if (event instanceof G2_ObjectMove)
        {
            G2_ObjectMove objectMove = (G2_ObjectMove)event;
            if (objectMove.player) {
                players.get(objectMove.id).x = objectMove.newX;
                players.get(objectMove.id).y = objectMove.newY;
            }
            else
            {
                platforms.get(objectMove.id).x = objectMove.newX;
                platforms.get(objectMove.id).y = objectMove.newY;
            }
        }
        else if (event instanceof G2_PlayerStateChange)
        {
            G2_PlayerStateChange playerStateChange = (G2_PlayerStateChange)event;
            Player player = players.get(playerStateChange.id);
            if (playerStateChange.state == G2_PlayerStateChange.State.JUMP)
            {
                player.selectedAnimation = 1;
                player.animationTicksLeft = player.ANIMATION_LENGTH;
            }
            else
            {
                player.selectedAnimation = 0;
                player.animationTicksLeft = -1;
            }
        }
    }
}
