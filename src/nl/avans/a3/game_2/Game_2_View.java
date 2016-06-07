package nl.avans.a3.game_2;

import nl.avans.a3.event.ModelEvent;
import nl.avans.a3.mvc_interfaces.View;
import nl.avans.a3.util.ResourceHandler;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by Thijs on 2-6-2016.
 */
public class Game_2_View implements View {

    private Game_2_Model model;

    public Game_2_View(Game_2_Model model)
    {
        this.model = model;
    }

    private class Player
    {
        final int ANIMATION_LENGTH = 1;

        float x, y;
        BufferedImage[] animation;
        int selectedAnimation = 0;
        int animationTicksLeft = -1;
        Player(float x, float y, BufferedImage playerImage)
        {
            this.x = x;
            this.y = y;
            animation = new BufferedImage[4];
            for (int i =0 ; i < 4; i++)
                animation[i] = playerImage.getSubimage(playerImage.getWidth()/4*i, 0, playerImage.getWidth()/4, playerImage.getHeight());
        }
    }

    private ArrayList<Player> players = new ArrayList<>();

    private class Platform {
        float x, y;
        BufferedImage image;

        Platform(float x, float y) {
            this.x = x;
            this.y = y;
            image = ResourceHandler.getImage("res/images_game2/wood.png");
        }
    }

    private ArrayList<Platform> platforms = new ArrayList<>();

    @Override
    public void start() {

    }

    @Override
    public void draw(Graphics2D g) {
        BufferedImage image = ResourceHandler.getImage("res/images_game2/background.png");
        g.drawImage(image.getSubimage(0, 0, 1920, 1080), 0, 0, null);

        g.setColor(Color.RED);
        g.setFont(new Font("Verdana", Font.BOLD, 68));
        g.drawString("" + model.getTime(), 960, 100); //TODO logischer maken

        for (Player player : players) {
            if (player.animationTicksLeft-- == 0 && player.selectedAnimation < 3)
            {
                player.animationTicksLeft = player.ANIMATION_LENGTH;
                player.selectedAnimation++;
            }

            g.drawImage(player.animation[player.selectedAnimation], (int) player.x, 1080 - (int) player.y+model.PLAYER_HEIGHT, null);
        }
        for (int i = 0; i <= 2; i++)
            g.drawImage(model.platforms.get(i).image, (int) model.platforms.get(i).x, (int) model.platforms.get(i).y, null);
    }

    @Override
    public void close() {
    }

    @Override
    public void onModelEvent(ModelEvent event) {
        if (event instanceof Game_2_Event == false)
        {
            nl.avans.a3.util.Logger.instance.log("2V001", "unexcpected message", nl.avans.a3.util.Logger.LogType.WARNING);
            return;
        }
        if (event instanceof G2_NewObject)
        {
            G2_NewObject newPlayer = (G2_NewObject)event;
            BufferedImage image = ResourceHandler.getImage("res/images_game2/person" + (newPlayer.id +1) + ".png");
            players.add(new Player(newPlayer.x, newPlayer.y, image));
            System.out.println("added a new player to view");
        }
        else if (event instanceof G2_ObjectMove)
        {
            G2_ObjectMove objectMove = (G2_ObjectMove)event;
            players.get(objectMove.id).x = objectMove.newX;
            players.get(objectMove.id).y = objectMove.newY;
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
