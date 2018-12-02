package servant.servantandroid.internal;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * an overridable base logger in the singleton pattern
 */
public class Logger {

    public enum Type { DEBUG, INFO, WARNING, ERROR }

    // setting the default logger
    private static Logger instance = new Logger();

    /**
     * Logs the specified message
     * Should be overridden in subclass
     * @param type the type of the message. see Logger.Type for available types
     * @param message the actual string to log
     * @param origin from where did this log entry come from?
     *               most likely a this or string literal describing the current object
     */
    public void Log(Logger.Type type, String message, Object origin) {
        System.out.println(
            String.format("[%s][%s][%s]:::|%s",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                type.toString(),
                origin.toString(),
                message)
        );
    }

    public static Logger getInstance() { return instance; }
    public static void   setInstance(Logger logger) {
        if(logger == null) throw new IllegalArgumentException("logger cannot be null!");
        instance = logger;
    }

    // prevent instantiation of this singleton
    private Logger() {}
}