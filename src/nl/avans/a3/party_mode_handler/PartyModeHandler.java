package nl.avans.a3.party_mode_handler;

import nl.avans.a3.event.NewGameEvent;
import nl.avans.a3.event.NewModel;
import nl.avans.a3.game_2.Game_2_Model;
import nl.avans.a3.game_3.Game_3_Model;
import nl.avans.a3.mvc_handlers.ModelHandler;
import nl.avans.a3.mvc_interfaces.Model;
import nl.avans.a3.util.WiimoteHandler;

public class PartyModeHandler {


    public enum Mode{
        CHOOSE_PARTY, CHOOSE_SINGLE
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
                    case 1 : ModelHandler.instance.onModelEvent(new NewGameEvent(wiimoteHandler)); break;
                    case 2 : ModelHandler.instance.changeModel(new NewModel(model, new Game_3_Model())); break;
                    case 3 : ModelHandler.instance.changeModel(new NewModel(model, new Game_2_Model())); break;
                }
                break;
            case CHOOSE_SINGLE: break;
        }

    }

    public static void notifyNextGame()
    {
        if(gameCounter <= 2) {
            gameCounter++;
            update();
        }
    }

    public static Mode getCurrentMode(){
        return mode;
    }
}
