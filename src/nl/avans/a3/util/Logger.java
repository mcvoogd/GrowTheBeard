package nl.avans.a3.util;

import com.opencsv.CSVReader;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Logger
{
    public enum LogType
    {
        DEBUG(0), LOG(0), WARNING(1), ERROR(2), EXCEPTION(3), NOTHING(4); // NOTE: the nothing type is used for the filter and should not used as a log type

        protected final int typeN; // A number to filter logs the higher te number to higher it's priority is
        LogType(int n) {typeN = n;}
    }

    public static final Logger instance = new Logger();

    /**
     * needs to be called to initialize the instance
     */
    public static void init() { if (!initialized) setupLogBuilders(); initialized = true; } // to ensure the instance gets initialized
    private static boolean initialized = false; // a boolean to ensure a warning can be logged when init isn't called on startup

    private static final int LOG_TYPE_LENGTH = 9; // the amount of char's used to display the log's type, should never change
    private static final int CODE_LENGTH = 5; // the amount of char's used to display the log's LogType, should never change
    private static final int CODE_PATH_LENGTH = 70; // the amount of char's used to display the code path
    private static final int DATE_TIME_LENGTH = 23; // the amount of char's used to display the date and the time

    private PrintStream consoleStream, logStream; // the logger's outputs
    private LogType consoleLevel, logLevel; // the logger's outputs' filters

    public Logger()
    {
        consoleStream = System.out;
        //noinspection ResultOfMethodCallIgnored
        new File("runtime_data/").mkdir(); // ensuring the runtime_data folder exits
        try {
            logStream = new PrintStream(new File("runtime_data/log_" + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date())) + ".txt"); // creating a file for the log
        } catch (FileNotFoundException e) {log(e);}
        setConsoleLevel(LogType.DEBUG);
        setLogLevel(LogType.DEBUG);

        // creating a new sysout that redirects to the logger
        synchronized (System.out)
        {
          System.setOut(new PrintStream(new OutputStream() {
                @Override
                public void write(byte[] b) { }

                public void write(byte[] b, int off, int len) // put rhe sysout messageConstruct into a log
                {
                    if (b[0] != '\r' && b[0] != '\n')
                    {
                        Logger.instance.log(null, "sysout", new String(Arrays.copyOfRange(b, off, len)), LogType.DEBUG);
                    }
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
        checkInitialisation();
        if (e == null) log("LO001");
        else log(null, e.getStackTrace()[0].toString(), e.getClass().getCanonicalName() + ", " + e.getMessage(), LogType.EXCEPTION);
    }

    /**
     * logs a messageConstruct
     * @param code the unique code that identifies the log (maybe left empty)
     * @param message the messageConstruct that will be logged
     * @param type the type of the log
     */
    public void log(final String code, final String message, final LogType type)
    {
        checkInitialisation();
        // NOTE: even tough this method is deprecated in order to prevent code duplication we still use it
        // logs the log with the cale's code path
        log(code, Thread.currentThread().getStackTrace()[2].toString(), message, type);
    }
    
    /**
     * @param code the unique code that identifies the log (maybe left empty)
     * @param codePath a code path to the log (used to find the code that calls the log)
     * @param message the messageConstruct that will be logged
     * @param type the type of the log
     */
    private void log(final String code, final String codePath, final String message, final LogType type)
    {
        // logs a warning if the messageConstruct is empty
        if (message == null || message.trim().equals("")) log("LO002");
        // logs an error if the log if invalid and returns
        if (type == LogType.NOTHING) {log("LC003", code ,message); return;}

        // start of the log string the current date and time
        String combinedMessage = toSize(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SS").format(new Date()), DATE_TIME_LENGTH) + " ";

        // adds the log type
        combinedMessage += toSize(type.toString(), LOG_TYPE_LENGTH) + " ";
        // adds the log code
        combinedMessage += toSize(code, CODE_LENGTH) + " ";
        // adds the code path
        combinedMessage += toSize(codePath, CODE_PATH_LENGTH, ')') + " ";
        // adds the messageConstruct
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
    private synchronized void checkInitialisation()
    {
        if (initialized) return;
        init();
        log("LO004");
    }

    private static HashMap<String, LogMessageBuilder> messageMap;

    /**
     *
     * @param code the unique code that identifies the log (maybe left empty)
     * @param arguments the arguments the code expects
     */
    public void log(final String code, final String... arguments)
    {
        if (code != null && code.trim().equals("sysout")) {
            String message = "";
            for (String argument : arguments)
                message += argument;
            log(code, message, LogType.DEBUG);
            return;
        }
        if (messageMap == null) return;
        if (messageMap.containsKey(code) == false) {log("LO005", code); return;}
        LogMessageBuilder builder = messageMap.get(code);
        log(code, Thread.currentThread().getStackTrace()[2].toString(), builder.argumentsToMessage(arguments), builder.logType);
    }

    private static void setupLogBuilders()
    {
        File file = new File("Supporting Documents/Testing/error and warning codes.csv");
        if (file.exists() == false) {Logger.instance.log("LO006"); return;}
        messageMap = new HashMap<>();

        try {
            CSVReader reader = new CSVReader(new FileReader(file));
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null)
            {
                if (nextLine[0].trim().equals("") || nextLine[3].trim().equals("")) continue;
                LogType logType = LogType.DEBUG;
                switch (nextLine[1].trim().charAt(0))
                {
                    case 'L':
                        logType = LogType.LOG;
                        break;
                    case 'W':
                        logType = logType.WARNING;
                        break;
                    case 'E':
                        logType = logType.ERROR;
                        break;
                }
                messageMap.put(nextLine[0].trim(), new LogMessageBuilder(nextLine[3].trim(), logType, nextLine[4].trim()));
            }
        } catch (IOException e) {
            Logger.instance.log(e);
        }
    }

    private static class LogMessageBuilder
    {
        class LogMessageBuildException extends RuntimeException
        {
            LogMessageBuildException(String message)
            {
                super(message);
            }
        }

        final LogType logType;
        final String comment;
        private ArrayList<String> messageConstruct = new ArrayList<>();
        boolean argumentAtStart = false;
        int correctArgumentCount =0;

        LogMessageBuilder(String message, LogType logType, String comment) throws LogMessageBuildException
        {
            this.logType = logType;
            this.comment = comment;
            StringTokenizer tokenizer = new StringTokenizer(message, "<>", true);
            boolean inArgument = false;
            boolean first = true;
            while (tokenizer.hasMoreTokens())
            {
                String token = tokenizer.nextToken();
                switch (token) {
                    case "<":
                        if (first) argumentAtStart = true;
                        if (inArgument) throw new LogMessageBuildException("previus argument was not called");
                        inArgument = true;
                        break;
                    case ">":
                        if (inArgument == false) throw new LogMessageBuildException("no argument started");
                        correctArgumentCount++;
                        inArgument = false;
                        break;
                    default:
                        if (inArgument == false) messageConstruct.add(token);
                        break;
                }
                first = false;
            }
        }

        String argumentsToMessage(final String... arguments) throws LogMessageBuildException
        {
            if (arguments.length != correctArgumentCount) throw new LogMessageBuildException("not all arguments were supplied");

            String message = "";
            int argumentIndex = 0;
            if (argumentAtStart) message = arguments[argumentIndex++];
            for (String aMessageConstruct : messageConstruct) {
                message += aMessageConstruct;
                if (argumentIndex < arguments.length) message += arguments[argumentIndex++];
            }
            return message;
        }
    }

    public static void test()
    {
        try {
            System.out.println("the main");
            Logger.instance.log("TC001", "Hello World", LogType.ERROR);
            Logger.instance.log(null, "testLog", LogType.LOG);
            Logger.instance.log("TC002", "10", LogType.WARNING);
            Logger.instance.log("TC003", "10.0", LogType.WARNING);
            System.out.println("hey, how are you");
            Logger.instance.log(new IndexOutOfBoundsException("use the size of your array fuckers"));
            Logger.instance.log(null);
            Logger.instance.log("TC004", "    ", LogType.ERROR);
            Logger.instance.log("TC005", "", LogType.ERROR);
            Logger.instance.log("TC006", null, LogType.ERROR);
            Logger.instance.log("TC007", "I think I can break the rules", LogType.NOTHING);

            Logger.instance.log("TC009", "debug info", LogType.DEBUG);
            Logger.instance.log("TC008", "stack trace test", LogType.WARNING);
            System.out.print(true);
            System.out.print("heyhey");

            try {
                LogMessageBuilder test = new LogMessageBuilder("<start>some <arg1>l<arg3> simple test, info (<arg2>)   <end>", LogType.LOG, null);
                System.out.println(test.argumentsToMessage("START", "3", "4", "3.14f", "END"));
                LogMessageBuilder test2 = new LogMessageBuilder("<1> fgdfg <2> jgkfgj", LogType.LOG, null);
                LogMessageBuilder test3 = new LogMessageBuilder("gjfhghfhg<1>jgdgd<2>", LogType.LOG, null);
                LogMessageBuilder test4 = new LogMessageBuilder("<fh>fdjdjgjd<jfk>", LogType.LOG, null);
                System.out.println(test2.argumentsToMessage("START", "MIDDLE"));
                System.out.println(test3.argumentsToMessage("MIDDLE", "END"));
                System.out.println(test4.argumentsToMessage("START", "END"));

                Logger.instance.log("GW001");
                Logger.instance.log("RH002", "SomeImageKey");
                Logger.instance.log("sysout", "some sort ", "OF", " message");
            } catch (LogMessageBuilder.LogMessageBuildException e) {
                Logger.instance.log(e);
            }
        }
        catch (Exception e)
        {
            Logger.instance.log(e);
            e.printStackTrace();
        }
    }
}