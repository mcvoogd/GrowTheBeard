package nl.avans.a3.game_2;

/**
 * Created by FlorisBob on 02-Jun-16.
 */
public class G2_NewObject extends Game_2_Event{
    final int id;
    boolean player;
    boolean basket;
    boolean woodStack;
    final float x, y;
    protected G2_NewObject(int id, boolean player, boolean basket, boolean woodStack, float x, float y)
    {
        this.id = id;
        this.player = player;
        this.basket = basket;
        this.woodStack = woodStack;
        this.x = x;
        this.y = y;
    }
}
