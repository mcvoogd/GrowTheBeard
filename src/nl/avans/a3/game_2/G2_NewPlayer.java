package nl.avans.a3.game_2;

import nl.avans.a3.util.ResourceHandler;

import java.awt.image.BufferedImage;

/**
 * Created by FlorisBob on 02-Jun-16.
 */
public class G2_NewPlayer extends Game_2_Event{
    final int playerID;
    final float x, y;

    private BufferedImage playerImage;

    protected G2_NewPlayer(int playerID, float x, float y)
    {
        this.playerID = playerID;
        this.x = x;
        this.y = y;
    }

    private void setPlayerImage() {
        if (playerID == 1) {
            BufferedImage image = (BufferedImage) ResourceHandler.getImage("res/images_game2/person1.png");
            playerImage = image.getSubimage(0, 0, 112, 164);
        }
        else {
            BufferedImage image = (BufferedImage) ResourceHandler.getImage("res/images_game2/person2.png");
            playerImage = image.getSubimage(0, 0, 112, 164);
        }
    }
}
