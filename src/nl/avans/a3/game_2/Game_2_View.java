package nl.avans.a3.game_2;

import nl.avans.a3.event.ModelEvent;
import nl.avans.a3.mvc_interfaces.View;
import nl.avans.a3.util.ResourceHandler;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.logging.Logger;

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
        float x, y;
        BufferedImage playerImage;
        Player(float x, float y, BufferedImage playerImage)
        {
            this.x = x;
            this.y = y;
            this.playerImage = playerImage;
        }
    }

    private ArrayList<Player> players = new ArrayList<>();

    @Override
    public void start() {

    }

    @Override
    public void draw(Graphics2D g) {
        for (Player player : players)
            g.drawImage(player.playerImage, (int)player.x, (int)player.y, null);
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
        if (event instanceof G2_NewPlayer)
        {
            G2_NewPlayer newPlayer = (G2_NewPlayer)event;
            BufferedImage image = ResourceHandler.getImage("res/images_game2/person" + (newPlayer.playerID+1) + ".png");
            players.add(new Player(newPlayer.x, newPlayer.y, image.getSubimage(0, 0, image.getWidth()>>2, image.getHeight())));
            System.out.println("added a new player to view");
        }
        else if (event instanceof G2_ObjectMove)
        {
            G2_ObjectMove objectMove = (G2_ObjectMove)event;
            players.get(objectMove.id).x = objectMove.newX;
            players.get(objectMove.id).y = objectMove.newY;
        }
    }
}
