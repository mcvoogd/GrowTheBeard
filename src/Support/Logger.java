package Support;

/**
 * Created by flori on 12-May-16.
 */
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger
{
    public enum LogType
    {
        LOG(0), WARNING(1), ERROR(2), EXCEPTION(3), NOTHING(4);

        protected final int typeN;
        LogType(int n) {typeN = n;}
    }

    public static final Logger instance = new Logger();

    private PrintStream consoleStream, logStream;
    private LogType consoleLevel, logLevel;


    private Logger()
    {
        consoleStream = System.out;
        new File("runtime_data/").mkdir();
        try {
            logStream = new PrintStream(new File("runtime_data/log.txt"));
        } catch (FileNotFoundException e) {}
        consoleLevel = LogType.LOG;
        logLevel = LogType.LOG;
        System.setOut(new PrintStream(new OutputStream() {
            @Override
            public void write(byte[] b)
            {
                if (b[0] != '\r' && b[0] != '\n') Logger.instance.log(null, "System.out.print", new String(b), LogType.LOG);
            }

            public void write(byte[] b, int off, int len)
            {
                write(b);
            }

            @Override
            public void write(int b) throws IOException {
                byte[] b2 = new byte[1];
                b2[0] = (byte)b;
                write(b2);
            }
        }));
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
        if (e == null) log("LO001", "Logger::log(Exception)", "Exception can't be null", LogType.ERROR);
        else
        {
            String codePath = "";
            StackTraceElement cause = e.getStackTrace()[0];
            codePath += cause.getClassName() + "::" + cause.getMethodName();
            log(null, codePath, e.getClass().getCanonicalName() + ", " + e.getMessage(), LogType.EXCEPTION);
        }
    }

    public void log(final String code, final String codePath, final String message, final LogType type)
    {
        final int LOG_TYPE_LENGTH = 9;
        final int CODE_LENGTH = 5;
        final int CODE_PATH_LENGTH = 40;

        if (message == null || message.trim().equals("")) log("LO002", "Logger::log(String, ...)", "are you sure you want to log an empty message", LogType.WARNING);
        if (type == LogType.NOTHING) {log("LC003", "Logger::(String, ...)", "the log type cannot be NOTHING, code("+code+") message("+message+")", LogType.ERROR); return;}

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
}