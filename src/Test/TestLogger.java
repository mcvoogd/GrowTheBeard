package Test;

import Support.Logger;

/**
 * Created by flori on 12-May-16.
 */
public class TestLogger {
    public static void main(String[] args)
    {
        System.out.println("the main");
        Logger.instance.log("TC001", "Logger::main", "Hello World", Logger.LogType.ERROR);
        Logger.instance.log(null, null, "testLog", Logger.LogType.LOG);
        Logger.instance.log("TC002", "Logger::method(int)", "10", Logger.LogType.WARNING);
        Logger.instance.log("TC003", "Logger::method(float)", "10.0", Logger.LogType.WARNING);
        System.out.println("hey, how are you");
        Logger.instance.log(new IndexOutOfBoundsException("use the size of your array fuckers"));
        Logger.instance.log(null);
        Logger.instance.log("TC004", "Logger::main", "    ", Logger.LogType.ERROR);
        Logger.instance.log("TC005", "Logger::main", "", Logger.LogType.ERROR);
        Logger.instance.log("TC006", "Logger::main", null, Logger.LogType.ERROR);
        Logger.instance.log("TC007", "Logger::main", "I think I can break the rules", Logger.LogType.NOTHING);
    }
}
