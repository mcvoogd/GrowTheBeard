package nl.avans.a3.game_2;

public class G2_NewObject extends Game_2_Event{
    final int id;
    boolean player;
    final float x, y;
    protected G2_NewObject(int id, boolean player, float x, float y)
    {
        this.id = id;
        this.player = player;
        this.x = x;
        this.y = y;
    }
}
