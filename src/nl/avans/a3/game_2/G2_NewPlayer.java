package nl.avans.a3.game_2;

/**
 * Created by FlorisBob on 02-Jun-16.
 */
public class G2_NewPlayer extends Game_2_Event{
    final int playerID;
    final float x, y;
    protected G2_NewPlayer(int playerID, float x, float y)
    {
        this.playerID = playerID;
        this.x = x;
        this.y = y;
    }
}
