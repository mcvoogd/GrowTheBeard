package nl.avans.a3.game_2;

/**
 * Created by Thijs on 3-6-2016.
 */
public class G2_PlatformStateChange extends Game_2_Event {
    enum State {FALLING, REMOVE}
    State state;

    G2_PlatformStateChange (State state) {
        this.state = state;
    }
}
