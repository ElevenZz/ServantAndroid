package servant.servantandroid.internal;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Locale;

/**
 * an overridable base logger utilizing dependency injection
 */
public class Logger {
    public enum Type { DEBUG, INFO, WARNING, ERROR }
    protected Type m_logLevel = Type.DEBUG;

    /**
     * setting an instance of this as the default logger
     * should be overridden by the GUI
     */
    private static Logger instance = new Logger();

    /**
     * Logs the specified message
     * Should be overridden in subclass
     * @param type the type of the message. see Logger.Type for available types
     * @param message the actual string to log
     * @param origin from where did this log entry come from?
     *               most likely a this or string literal describing the current object
     * @param error an optional exception in care of logging an error
     */
    public void log(Logger.Type type, String message, Object origin, Throwable error) {
        if(type.ordinal() >= m_logLevel.ordinal())
            System.out.println(formatLine(type, message, origin));
    }

    /**
     * overload for not specifying an exception making it an optional parameter
     * @param type the type of the message. see Logger.Type for available types
     * @param message the actual string to log
     * @param origin from where did this log entry come from?
     *               most likely a this or string literal describing the current object
     */
    public final void log(Logger.Type type, String message, Object origin) {
        log(type, message, origin, null);
    }

    /**
     * overload for not specifying an exception and origin making them optional parameters
     * @param type the type of the message. see Logger.Type for available types
     * @param message the actual string to log
     */
    public final void log(Logger.Type type, String message) {
        log(type, message, null, null);
    }

    /**
     * calls log with type error
     * @param message the actual string to log
     * @param origin from where did this log entry come from?
     *               most likely a this or string literal describing the current object
     * @param error an optional exception in care of logging an error
     */
    public final void logError(String message, Object origin, Throwable error) {
        log(Type.ERROR, message, origin, error);
    }

    /**
     * formats a line for logging it to the console
     * @param type the type of the message. see Logger.Type for available types
     * @param message the actual string to log
     * @param origin from where did this log entry come from?
     *               most likely a this or string literal describing the current object
     * @return the formatted string
     */
    private String formatLine(Logger.Type type, String message, Object origin) {
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

    /**
     * protected constructor to prevent instantiation from outside
     */
    protected Logger() {}
}