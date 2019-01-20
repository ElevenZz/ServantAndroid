package servant.servantandroid.internal;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Locale;

/**
 * an overridable base logger utilizing the singleton pattern
 */
public class Logger {

    public enum Type { DEBUG, INFO, WARNING, ERROR }
    protected Type m_logLevel = Type.DEBUG;

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
    public void log(Logger.Type type, String message, Object origin, Throwable error) {
        if(type.ordinal() >= m_logLevel.ordinal())
            System.out.println(formatLine(type, message, origin));
    }

    public final void log(Logger.Type type, String message, Object origin) {
        log(type, message, origin, null);
    }

    public final void log(Logger.Type type, String message) {
        log(type, message, null, null);
    }

    public final void logError(String message, Object origin, Throwable error) {
        log(Type.ERROR, message, origin, error);
    }

    public String formatLine(Logger.Type type, String message, Object origin) {
        return String.format(
            "[%s][%s][%s]:::|%s",
            new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ROOT).format(new Date()),
            type.toString(),
            origin == null? "null" : origin.toString(),
            message);
    }

    public static Logger getInstance() { return instance; }

    /**
     * use this to provide your own logger
     * @param logger logger instance inheriting this class
     */
    public static void setLogger(Logger logger) {
        if(logger == null) throw new IllegalArgumentException("logger cannot be null!");
        instance = logger;
    }

    // prevent instantiation of this singleton
    protected Logger() {}
}