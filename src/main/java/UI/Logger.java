package UI;

import java.io.IOException;
import java.util.logging.*;

/**
 * Created by felix on 21.02.2016.
 */
public abstract class Logger {

    private static FileHandler logFile;
    private static SimpleFormatter formatter;
    private static java.util.logging.Logger LOGGER;
    private static final int STACK_TRACE_INDEX = 3;
    private static boolean console_output, file_output;

    public static void logInit(boolean consoleOutput, boolean fileOutput) throws IOException
    {
        console_output = consoleOutput;
        file_output = fileOutput;
        LOGGER = java.util.logging.Logger.getLogger(java.util.logging.Logger.GLOBAL_LOGGER_NAME);
        java.util.logging.Logger rootLogger = java.util.logging.Logger.getLogger("");

        Handler[] handler = rootLogger.getHandlers();
        if(handler[0] instanceof ConsoleHandler)
        {
            rootLogger.removeHandler(handler[0]);
        }
        LOGGER.setLevel(Level.INFO);
        if(fileOutput)
        {
            logFile = new FileHandler("Output.log");
            formatter = new SimpleFormatter();
            logFile.setFormatter(formatter);
            LOGGER.addHandler(logFile);
        }
    }

    private static String getLineInfo()
    {
        String res = "";
        StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        res = (res + "in Thread \"" + Thread.currentThread().getName() + "\":");
        for(int c=STACK_TRACE_INDEX; c<ste.length; c++)
        {
            res = (res + "\n    at " + ste[c].getClassName() + "::" + ste[c].getMethodName() + "()  (" + ste[c].getFileName() + ":" + ste[c].getLineNumber() + ")");
        }
        return res;
    }

    public static void logOut(String msg)
    {
        if( console_output || file_output )
        {
            msg = ("Info " + getLineInfo() + "\n" + msg + "\n");
            LOGGER.log(Level.INFO, msg);
            if(console_output)
            {
                System.out.println(msg);
            }
        }
    }

    public static void logErr(String msg)
    {
        if( console_output || file_output )
        {
            msg = ("Error " + getLineInfo() + "\n" + msg + "\n");
            LOGGER.log(Level.SEVERE, msg);
            if(console_output)
            {
                System.err.println(msg);
            }
        }
    }

    public static void logOut(String msg, boolean lineInfo)
    {
        if( console_output || file_output )
        {
            if(lineInfo)
            {
                msg = ("Info " + getLineInfo() + "\n" + msg + "\n");
            }
            LOGGER.log(Level.INFO, msg);
            if(console_output)
            {
                System.out.println(msg);
            }
        }
    }

    public static void logErr(String msg, boolean lineInfo)
    {
        if( console_output || file_output )
        {
            if(lineInfo)
            {
                msg = ("Error " + getLineInfo() + "\n" + msg + "\n");
            }
            LOGGER.log(Level.SEVERE, msg);
            if(console_output)
            {
                System.err.println(msg);
            }
        }
    }

}
