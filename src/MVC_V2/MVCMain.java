package MVC_V2;

import nl.avans.a3.Logger;

/**
 * Created by FlorisBob on 27-May-16.
 */
public class MVCMain {
    public static void main(String[] args)
    {
        try
        {
            Logger.init();
            ControllerHandler controllerHandler = new ControllerHandler();
            ViewHandler viewHandler = new ViewHandler(controllerHandler);
            ModelHandler.instance.start();
            viewHandler.startTimer();
        }catch (Exception e)
        {
            Logger.instance.log(e);
        }
    }
}
