package nl.avans.a3.game_2;

public class G2_ObjectMove extends Game_2_Event{
    int id;
    boolean player;
    float newX, newY;
    G2_ObjectMove(int id, boolean player, float newX, float newY)
    {
        this.id = id;
        this.player = player;
        this.newX = newX;
        this.newY = newY;
    }
}
