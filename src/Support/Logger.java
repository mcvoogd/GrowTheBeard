package Support;

/**
 * Created by flori on 12-May-16.
 */
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

class Logger
{
    public static void main(String[] args)
    {
        Logger.instance.log("TC001", "Logger::main", "Hello World", LogType.ERROR);
        Logger.instance.log(null, null, "testLog", LogType.LOG);
        Logger.instance.log("TC002", "Logger::method(int)", "10", LogType.WARNING);
        Logger.instance.log("TC003", "Logger::method(float)", "10.0", LogType.WARNING);
        System.out.println("hey, how are you");
        Logger.instance.log(new IndexOutOfBoundsException("use the size of your array fuckers"));
    }

    public enum LogType
    {
        LOG(0), WARNING(1), ERROR(2), EXCEPTION(3), NOTTHING(4);

        protected final int typeN;
        private LogType(int n) {typeN = n;}
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
        String codePath = "";
        StackTraceElement cause = e.getStackTrace()[0];
        codePath += cause.getClassName() + "::" + cause.getMethodName();
        log(null, codePath, e.getClass().getCanonicalName() + ", " + e.getMessage(), LogType.EXCEPTION);
    }

    public void log(final String code, final String codePath, final String message, final LogType type)
    {
        final int LOG_TYPE_LENGTH = 9;
        final int CODE_LENGTH = 5;
        final int CODE_PATH_LENGTH = 40;

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