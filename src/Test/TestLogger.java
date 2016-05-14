package Test;

import Support.Logger;

/**
 * Created by flori on 12-May-16.
 */
public class TestLogger {
    public static void main(String[] args)
    {
        System.out.println("the main");
        Logger.instance.log("TC001","Hello World", Logger.LogType.ERROR);
        Logger.instance.log(null, "testLog", Logger.LogType.LOG);
        Logger.instance.log("TC002", "10", Logger.LogType.WARNING);
        Logger.instance.log("TC003", "10.0", Logger.LogType.WARNING);
        System.out.println("hey, how are you");
        Logger.instance.log(new IndexOutOfBoundsException("use the size of your array fuckers"));
        Logger.instance.log(null);
        Logger.instance.log("TC004", "    ", Logger.LogType.ERROR);
        Logger.instance.log("TC005", "", Logger.LogType.ERROR);
        Logger.instance.log("TC006", null, Logger.LogType.ERROR);
        Logger.instance.log("TC007", "I think I can break the rules", Logger.LogType.NOTHING);

        Logger.instance.log("TC008", "stack trace test", Logger.LogType.WARNING);
        System.out.print(true);
        System.out.print("heyhey");
    }
}
