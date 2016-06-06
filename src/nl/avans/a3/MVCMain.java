package nl.avans.a3;

import nl.avans.a3.mvc_handlers.ControllerHandler;
import nl.avans.a3.mvc_handlers.ModelHandler;
import nl.avans.a3.mvc_handlers.ViewHandler;
import nl.avans.a3.util.Logger;

public class MVCMain {
    public static void main(String[] args){
            Logger.init();
            Logger.instance.log("MV001", "program start", Logger.LogType.LOG);
            ControllerHandler controllerHandler = new ControllerHandler();
            Logger.instance.log("MV002", "controller created", Logger.LogType.DEBUG);
            ViewHandler viewHandler = new ViewHandler(controllerHandler, ViewHandler.DisplayMode2.FULLSCREEN);
            Logger.instance.log("MV003", "view created", Logger.LogType.DEBUG);
            ModelHandler.instance.start();
            Logger.instance.log("MV004", "model started", Logger.LogType.DEBUG);
            viewHandler.startTimer();
    }
}
