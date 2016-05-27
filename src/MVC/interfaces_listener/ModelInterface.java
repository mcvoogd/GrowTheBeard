package MVC.interfaces_listener;

public interface ModelInterface{

    enum States
    {
        BOOT_SCREEN, MAIN_MENU, GAME_1, GAME_2, Game_3, SCOREBOARD, SINGLE_GAME_MENU, SELECT_PLAYER
    }

    void update(); //central timer calls this logic update every N times.
    void setListener(ModelListener listener);

}
