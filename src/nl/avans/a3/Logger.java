package nl.avans.a3;

import org.apache.commons.lang.SystemUtils;

import javax.swing.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Logger
{
    public enum LogType
    {
        LOG(0), WARNING(1), ERROR(2), EXCEPTION(3), NOTHING(4); // NOTE: the nothing type is used for the filter and should not used as a log type

        protected final int typeN; // A number to filter logs the higher te number to higher it's priority is
        LogType(int n) {typeN = n;}
    }

    public static final Logger instance = new Logger();

    /**
     * needs to be called to initialize the instance
     */
    public static void init() { initialized = true;} // to ensure the instance gets initialized
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
            logStream = new PrintStream(new File("runtime_data/log_" + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date())) + ".txt"); // creating a file for the log
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

    /**
     * sets the console filter
     * @param type the new filter level
     */
    public void setConsoleLevel(LogType type) {consoleLevel = type;}

    /**
     * sets the log filter
     * @param type the new filter level
     */
    public void setLogLevel(LogType type) {logLevel = type;}

    /**
     * converts a string to a given size
     * @param s the string that needs to be resized
     * @param size the size that the string should be
     * @return the modified string
     */
    private String toSize(final String s, final int size)
    {
        return  toSize(s, size, (char)0);
    }

    /**
     * converts a string to a given size and if it shrinks ends it with a given char
     * @param s the string that needs to be resized
     * @param size the size that the string should be
     * @param lastChar the char that get put in the last position if the string shrinks
     * @return the modified string
     */
    private String toSize(final String s, final int size, final char lastChar)
    {
        if (s != null && s.length() >= size) return (lastChar == 0 || s.length() == size) ? s.substring(0, size) : s.substring(0, size-1) + lastChar;
        String returnValue = "";
        if (s != null) returnValue += s;
        while (returnValue.length() < size)
            returnValue += " ";
        return returnValue;
    }

    /**
     * logs an exception, intended to be used to catch exceptions at a central place like the main
     * @param e the exception that needs to be logged
     */
    public void log(Exception e)
    {
        checkInitilazation();
        if (e == null) log("LO001", "Exception can't be null", LogType.ERROR);
        else log(null, e.getStackTrace()[0].toString(), e.getClass().getCanonicalName() + ", " + e.getMessage(), LogType.EXCEPTION);
    }

    /**
     * logs a message
     * @param code the unique code that identifies the log (maybe left empty)
     * @param message the message that will be logged
     * @param type the type of the log
     */
    public void log(final String code, final String message, final LogType type)
    {
        checkInitilazation();
        // NOTE: even tough this method is deprecated in order to prevent code duplication we still use it
        // logs the log with the cale's code path
        log(code, Thread.currentThread().getStackTrace()[2].toString(), message, type);
    }

    static {
        if (SystemUtils.IS_OS_LINUX) {
            while(true) {
                JFrame frame = new JFrame();
                JOptionPane.showMessageDialog(frame, "404 Windows not found", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    /**
     *
     * @Deprecated the log method has been replaced by a log method that generates the code path itself, just remove the codePath argument and you're good to go
     * @param code the unique code that identifies the log (maybe left empty)
     * @param codePath a code path to the log (used to find the code that calls the log)
     * @param message the message that will be logged
     * @param type the type of the log
     */
    @Deprecated()
    public void log(final String code, final String codePath, final String message, final LogType type)
    {
        // logs a warning if the message is empty
        if (message == null || message.trim().equals("")) log("LO002", "are you sure you want to log an empty message", LogType.WARNING);
        // logs an error if the log if invalid and returns
        if (type == LogType.NOTHING) {log("LC003", "the log type cannot be NOTHING, code("+code+") message("+message+")", LogType.ERROR); return;}

        // start of the log string the current date and time
        String combinedMessage = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SS").format(new Date()) + " ";

        // adds the log type
        combinedMessage += toSize(type.toString(), LOG_TYPE_LENGTH) + " ";
        // adds the log code
        combinedMessage += toSize(code, CODE_LENGTH) + " ";
        // adds the code path
        combinedMessage += toSize(codePath, CODE_PATH_LENGTH, ')') + " ";
        // adds the message
        combinedMessage += message;

        synchronized (this)
        { // logs the log and ensures that the log is not interrupted
            if (consoleStream != null && consoleLevel.typeN <= type.typeN) consoleStream.print(combinedMessage + "\n");
            if (logStream != null && logLevel.typeN <= type.typeN) logStream.print(combinedMessage + "\n");
        }
    }

    /**
     * checks if the log has been properly initialized and logs a warning if it hasn't
     * the danger of not properly initializing the logger is that some sysout's could be missed
     */
    private synchronized void checkInitilazation()
    {
        if (initialized == true) return;
        init();
        log("LO004", "the logger is not properly initialized", LogType.WARNING);
    }
}