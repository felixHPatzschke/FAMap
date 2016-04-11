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

    public static void logOut(Object... msg)
    {
        if( console_output || file_output )
        {
            String smsg = "";
            boolean exc = false;
            for(Object x : msg)
            {
                if(x instanceof Throwable)
                {
                    exc = true;
                }
            }
            if(exc)
            {
                smsg = "Info " + getLineInfo() + "\n";
            }
            for(Object x : msg)
            {
                smsg = smsg + x.toString() + "\n";
            }
            LOGGER.log(Level.INFO, smsg);
            if(console_output)
            {
                System.out.print(smsg);
            }
        }
    }

    public static void logErr(Object... msg)
    {
        if( console_output || file_output )
        {
            String smsg = "Error " + getLineInfo() + "\n";
            for(Object x : msg)
            {
                smsg = smsg + x.toString() + "\n";
            }
            LOGGER.log(Level.SEVERE, smsg);
            if(console_output)
            {
                System.err.print(smsg);
            }
        }
    }

    public static void logOutI(Object... msg)
    {
        if( console_output || file_output )
        {
            String smsg = "Info " + getLineInfo() + "\n";

            for(Object x : msg)
            {
                smsg = smsg + x.toString() + "\n";
            }
            LOGGER.log(Level.INFO, smsg);
            if(console_output)
            {
                System.out.println(smsg);
            }
        }
    }

    public static void logErrI(Object... msg)
    {
        if( console_output || file_output )
        {
            String smsg = "Error " + getLineInfo() + "\n";

            for(Object x : msg)
            {
                smsg = smsg + x.toString() + "\n";
            }
            LOGGER.log(Level.SEVERE, smsg);
            if(console_output)
            {
                System.err.println(smsg);
            }
        }
    }
}
