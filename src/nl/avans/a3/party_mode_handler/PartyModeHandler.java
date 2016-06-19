package nl.avans.a3.party_mode_handler;

import nl.avans.a3.event.NewGameEvent;
import nl.avans.a3.event.NewModel;
import nl.avans.a3.game_1.DummyMVC.DummyModel;
import nl.avans.a3.game_2.Game_2_Model;
import nl.avans.a3.game_3.Game_3_Model;
import nl.avans.a3.main_menu.MainMenuModel;
import nl.avans.a3.mvc_handlers.ModelHandler;
import nl.avans.a3.mvc_interfaces.Model;
import nl.avans.a3.util.WiimoteHandler;
import nl.avans.a3.winScreen.WinScreen_Model;

public class PartyModeHandler {


    public enum Mode{
        CHOOSE_PARTY, CHOOSE_SINGLE, DEFAULT
    }

    private static Mode mode;
    private static int gameCounter;
    private static WiimoteHandler wiimoteHandler;
    private static Model model;

    public PartyModeHandler(Mode mode, WiimoteHandler wiimoteHandler, Model oldmodel)
    {
        this.mode = mode;
        this.model = oldmodel;
        this.wiimoteHandler = wiimoteHandler;
        gameCounter = 1;
    }

    public static void update()
    {
        switch (mode)
        {
            case CHOOSE_PARTY:
                switch(gameCounter)
                {
                    case 1 : ModelHandler.instance.changeModel(new NewModel(model, new DummyModel())); ModelHandler.instance.onModelEvent(new NewGameEvent(wiimoteHandler)); break;
                    case 2 : ModelHandler.instance.changeModel(new NewModel(model, new Game_2_Model())); break;
                    case 3 : ModelHandler.instance.changeModel(new NewModel(model, new Game_3_Model())); break;
                    case 4 : ModelHandler.instance.changeModel(new NewModel(model, new WinScreen_Model())); break;
                }
                break;
            case CHOOSE_SINGLE: break;
            case DEFAULT: break;
        }

    }

    /**
     * use this method to invoke a next game.
     * the method updates all views etc.
     */
    public static void notifyNextGame()
    {
        if(gameCounter <= 4) {
            gameCounter++;
            update();
        }
    }

    public static Mode getCurrentMode(){
        return mode;
    }

    public static void setCurrentMode(Mode mode)
    {
        PartyModeHandler.mode = mode;
    }
}
