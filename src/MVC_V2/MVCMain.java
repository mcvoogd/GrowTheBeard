package MVC_V2;


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
