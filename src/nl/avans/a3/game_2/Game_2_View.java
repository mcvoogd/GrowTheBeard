package nl.avans.a3.game_2;

import nl.avans.a3.event.ModelEvent;
import nl.avans.a3.mvc_handlers.ModelHandler;
import nl.avans.a3.mvc_interfaces.View;
import nl.avans.a3.util.Beard;
import nl.avans.a3.util.EasyTransformer;
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
    private BufferedImage banner;
    final int framesPerAnimationFrame = 40;
    private int waterfallIndex = 0;
    private final int WIDTH = 1920;
    private final int HEIGHT = 1080;
    private double textScale = 0.1;
    private static final double CHANGE_SPEED = 0.0005;
    private double change = CHANGE_SPEED;
    private static final double MAX_SCALE = 0.15;
    private static final double MIN_SCALE = 0.1;
    private BufferedImage winScreen;
    private BufferedImage[] winner;
    private BufferedImage winnerImage;
    private BufferedImage text;
    private BufferedImage[] playerImage;
    private BufferedImage playerImages;

    private boolean preScreen = true; //TODO PRE SCREEN


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
        winner = new BufferedImage[3];
        text = ResourceHandler.getImage("res/images_scoreboard/text.png");
        winnerImage = ResourceHandler.getImage("res/images_scoreboard/winner.png");
        winScreen = ResourceHandler.getImage("res/images_scoreboard/background.png");
        playerImage = new BufferedImage[2];
        playerImages = ResourceHandler.getImage("res/images_scoreboard/person.png");
        for(int i = 0; i < 2; i++)
        {
            playerImage[i] = playerImages.getSubimage(311*i, 0, 311, 577);
        }
        for(int i = 0; i < 3; i++){
            winner[i] = winnerImage.getSubimage(0, (242 * i), winnerImage.getWidth(), 726/3);
        }

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

        banner = ResourceHandler.getImage("res/images_game1/banner.png");
    }

    @Override
    public void draw(Graphics2D g) {
        if (model.inGame) {
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
                g.drawImage(player.animation[player.selectedAnimation], (int) player.x+PLAYER_X_OFFSET, 1080 - (int) player.y-model.PLAYER_HEIGHT, null);
                g.drawImage(player.beard, (int) player.x+PLAYER_X_OFFSET, 1080 - (int) player.y-model.PLAYER_HEIGHT, null);
                g.drawImage(player.animationArm[player.selectedAnimation], (int) player.x+PLAYER_X_OFFSET, 1080 - (int) player.y-model.PLAYER_HEIGHT, null);

              }


            g.drawImage(banner, 0, -25, 1920, 180, null);
            g.drawString("" + model.getTime(), 960 - (ft.stringWidth("" + model.getTime()) / 2) + 90, 80);

            for (Basket basket : baskets) {
                g.drawImage(basket.image, (int) basket.x, (int) basket.y, null);
            }

            for (WoodStack woodStack : woodStacks) {
                g.drawImage(woodStack.image, (int) woodStack.x, (int) woodStack.y, null);
            }

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
        }

    }

    @Override
    public void close() {
    }

    private void drawGameEnd(Graphics2D g, int player) {

        g.drawImage(winScreen, 0, 0, WIDTH, HEIGHT, null);

        textScale += change;
        if(textScale > MAX_SCALE){
            change = -CHANGE_SPEED;
        }else if(textScale < MIN_SCALE){
            change = CHANGE_SPEED;
        }

        g.drawImage(text, EasyTransformer.scaleImageFromCenter(text, textScale, (WIDTH/2) - text.getWidth(null)/2, 200), null);

        switch(player)
        {
            case 0 :g.drawImage(winner[2], 500, 100, null); break; //TEKST
            case 1 :g.drawImage(winner[0], 500, 100, null); break; //TEKST
            case 2 :g.drawImage(winner[1], 500, 100, null); break; //TEKST
        }


        g.drawImage(playerImage[0],(WIDTH/2) - (1315/8) - 500, 300, 311, 577,  null);
        g.drawImage(playerImage[1], (WIDTH/2) - (1315/8) + 530, 300, 311, 577, null);

        int oldBeard1 = ((Beard.beardPlayer1 - 2) < 0) ? 0 : Beard.beardPlayer1 - 2;
        int oldBeard2 = ((Beard.beardPlayer2 - 2) < 0) ? 0 : Beard.beardPlayer2 - 2;
        switch (player){
            case 0:
                g.drawImage(model.getBeards(Beard.beardPlayer1),(WIDTH/2) - (1315/8) - 500, 300,  311, 577, null);
                g.drawImage(model.getBeards(Beard.beardPlayer2),(WIDTH/2) - (1315/8) + 530, 300,  311, 577, null);
                break;
            case 1:
                if(model.getBeardCounter() < 15 && model.getSwitchBeardCounter() < 3){
                    g.drawImage(model.getBeards(oldBeard1),(WIDTH/2) - (1315/8) - 500, 300,  311, 577, null);
                }else if(model.getBeardCounter() < 30 && model.getSwitchBeardCounter() < 3){
                    g.drawImage(model.getBeards(Beard.beardPlayer1),(WIDTH/2) - (1315/8) - 500, 300,  311, 577, null);
                }else{
                    g.drawImage(model.getBeards(Beard.beardPlayer1),(WIDTH/2) - (1315/8) - 500, 300,  311, 577, null);
                    model.setBeardCounter(0);
                    model.setSwitchBeardCounter(model.getSwitchBeardCounter() + 1);
                }
                g.drawImage(model.getBeards(Beard.beardPlayer2),(WIDTH/2) - (1315/8) + 530, 300,  311, 577, null);
                break;
            case 2:
                if(model.getBeardCounter() < 15 && model.getSwitchBeardCounter() < 3){
                    g.drawImage(model.getBeards(oldBeard2),(WIDTH/2) - (1315/8) + 530, 300,  311, 577, null);
                }else if(model.getBeardCounter() < 30 && model.getSwitchBeardCounter() < 3){
                    g.drawImage(model.getBeards(Beard.beardPlayer2),(WIDTH/2) - (1315/8) + 530, 300, 311, 577, null);
                }else{
                    g.drawImage(model.getBeards(Beard.beardPlayer2),(WIDTH/2) - (1315/8) + 530, 300, 311, 577, null);
                    model.setBeardCounter(0);
                    model.setSwitchBeardCounter(model.getSwitchBeardCounter() + 1);
                }
                g.drawImage(model.getBeards(Beard.beardPlayer1),(WIDTH/2) - (1315/8) - 500, 300, 311, 577, null);
                break;
        }
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
