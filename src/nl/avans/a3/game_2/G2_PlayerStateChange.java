package nl.avans.a3.game_2;

public class G2_PlayerStateChange extends Game_2_Event {
    enum State{JUMP, WALK}
    State state;
    int id;
    G2_PlayerStateChange(State state, int id)
    {
        this.state = state;
        this.id = id;
    }
}
