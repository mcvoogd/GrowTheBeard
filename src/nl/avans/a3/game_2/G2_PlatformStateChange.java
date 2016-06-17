package nl.avans.a3.game_2;

/**
 * Created by Thijs on 3-6-2016.
 */
public class G2_PlatformStateChange extends Game_2_Event {
    public enum State {FALLING, REMOVE, TRANSLUSENT}
    State state;
    int id;

    public G2_PlatformStateChange (State state, int id) {
        this.state = state;
        this.id = id;
    }
}
