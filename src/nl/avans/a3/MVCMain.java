package nl.avans.a3;

import nl.avans.a3.mvc_handlers.ControllerHandler;
import nl.avans.a3.mvc_handlers.ModelHandler;
import nl.avans.a3.mvc_handlers.ViewHandler;
import nl.avans.a3.util.Logger;

public class MVCMain {
    public static void main(String[] args){
        try{
            Logger.init();
            Logger.instance.log("MV001", "program start", Logger.LogType.LOG);
            ControllerHandler controllerHandler = new ControllerHandler();
            ViewHandler viewHandler = new ViewHandler(controllerHandler);
            ModelHandler.instance.start();
            viewHandler.startTimer();
        }catch (Exception e){
            Logger.instance.log(e);
        }
    }
}
