package Support;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger
{
    public enum LogType
    {
        LOG(0), WARNING(1), ERROR(2), EXCEPTION(3), NOTHING(4); // NOTE: the nothing type is used for the filter and should not used as a log type

        protected final int typeN; // A number to filter logs the higher te number to higher it's priority is
        LogType(int n) {typeN = n;}
    }

    public static final Logger instance = new Logger();
    public static void init() {initialized = true;} // to ensure the instance gets initialized
    private static boolean initialized = false; // a boolean to ensure a warning can be logged when init isn't called on startup

    private static final int LOG_TYPE_LENGTH = 9; // the amount of char's used to display the log's type, should never change
    private static final int CODE_LENGTH = 5; // the amount of char's used to display the log's LogType, should never change
    private static final int CODE_PATH_LENGTH = 70; // the amount of char's used to display the code path

    private PrintStream consoleStream, logStream; // the logger's outputs
    private LogType consoleLevel, logLevel; // the logger's outputs' filters


    private Logger()
    {
        consoleStream = System.out;
        new File("runtime_data/").mkdir(); // ensuring the runtime_data folder exits
        try {
            logStream = new PrintStream(new File("runtime_data/log.txt")); // creating a file for the log
        } catch (FileNotFoundException e) {log(e);}
        setConsoleLevel(LogType.LOG);
        setLogLevel(LogType.LOG);

        // creating a new sysout that redirects to the logger
        synchronized (System.out)
        {
            System.setOut(new PrintStream(new OutputStream() {
            @Override
            public void write(byte[] b) { }

            public void write(byte[] b, int off, int len) // put rhe sysout message into a log
            {
                if (b[0] != '\r' && b[0] != '\n') Logger.instance.log(null, "sysout", new String(b), LogType.LOG);
            }

            @Override
            public void write(int b) throws IOException {}
        }));
        }
    }

    public void setConsoleLevel(LogType type) {consoleLevel = type;}
    public void setLogLevel(LogType type) {logLevel = type;}

    private String toSize(final String s, final int size)
    {
        return  toSize(s, size, (char)0);
    }

    private String toSize(final String s, final int size, final char lastChar)
    {
        if (s != null && s.length() >= size) return (lastChar == 0 || s.length() == size) ? s.substring(0, size) : s.substring(0, size-1) + lastChar;
        String returnValue = "";
        if (s != null) returnValue += s;
        while (returnValue.length() < size)
            returnValue += " ";
        return returnValue;
    }

    public void log(Exception e)
    {
        checkInitilazation();
        if (e == null) log("LO001", "Exception can't be null", LogType.ERROR);
        else log(null, e.getStackTrace()[0].toString(), e.getMessage(), LogType.EXCEPTION);
    }

    public void log(final String code, final String message, final LogType type)
    {
        checkInitilazation();
        // even tough this method is deprecated in order to prevent code duplication we still use it
        log(code, Thread.currentThread().getStackTrace()[2].toString(), message, type);
    }

    @Deprecated()
    /**
     * @Deprecated the log method has been replaced by a log method that generates the code path itself, just remove the codePath argument and you're good to go
    **/

    public void log(final String code, final String codePath, final String message, final LogType type)
    {
        if (message == null || message.trim().equals("")) log("LO002", "are you sure you want to log an empty message", LogType.WARNING);
        if (type == LogType.NOTHING) {log("LC003", "the log type cannot be NOTHING, code("+code+") message("+message+")", LogType.ERROR); return;}

        String combinedMessage = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SS").format(new Date()) + " ";

        combinedMessage += toSize(type.toString(), LOG_TYPE_LENGTH) + " ";
        combinedMessage += toSize(code, CODE_LENGTH) + " ";
        combinedMessage += toSize(codePath, CODE_PATH_LENGTH, ')') + " ";
        combinedMessage += message;

        synchronized (this)
        {
            if (consoleStream != null && consoleLevel.typeN <= type.typeN) consoleStream.print(combinedMessage + "\n");
            if (logStream != null && logLevel.typeN <= type.typeN) logStream.print(combinedMessage + "\n");
        }
    }

    private void checkInitilazation()
    {
        if (initialized == true) return;
        init();
        log("LO004", "the logger is not properly initialized", LogType.WARNING);
    }
}