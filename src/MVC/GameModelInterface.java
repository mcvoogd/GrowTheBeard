package MVC;

import java.awt.*;

public interface GameModelInterface {

    public enum States
    {
        BOOT_SCREEN, MAIN_MENU, GAME_1, GAME_2, Game_3, SCOREBOARD, SINGLE_GAME_MENU, SELECT_PLAYER
    }

    public void update(); //central timer calls this logic update every N times.

}
