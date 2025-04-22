package ma.ensa.apms.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Utility class for logging.
 * This class provides static methods for different types of logging.
 */
@Component
public class LoggerUtils {

    private static final Logger serviceLogger = LoggerFactory.getLogger("ma.ensa.apms.service.impl");
    private static final Logger authLogger = LoggerFactory.getLogger("ma.ensa.apms.auth");

    /**
     * Log service information
     * 
     * @param message The message to log
     */
    public static void logServiceInfo(String message) {
        serviceLogger.info(message);
    }

    /**
     * Log service information with format
     * 
     * @param format The message format
     * @param args   The arguments
     */
    public static void logServiceInfo(String format, Object... args) {
        serviceLogger.info(format, args);
    }

    /**
     * Log service error
     * 
     * @param message The message to log
     * @param e       The exception
     */
    public static void logServiceError(String message, Throwable e) {
        serviceLogger.error(message, e);
    }

    /**
     * Log service error with format
     * 
     * @param format The message format
     * @param args   The arguments
     */
    public static void logServiceError(String format, Object... args) {
        serviceLogger.error(format, args);
    }

    /**
     * Log service debug message
     * 
     * @param message The message to log
     */
    public static void logServiceDebug(String message) {
        serviceLogger.debug(message);
    }

    /**
     * Log service debug message with format
     * 
     * @param format The message format
     * @param args   The arguments
     */
    public static void logServiceDebug(String format, Object... args) {
        serviceLogger.debug(format, args);
    }

    /**
     * Log authentication related information
     * 
     * @param message The message to log
     */
    public static void logAuthInfo(String message) {
        authLogger.info(message);
    }

    /**
     * Log authentication error
     * 
     * @param message The message to log
     * @param e       The exception
     */
    public static void logAuthError(String message, Throwable e) {
        authLogger.error(message, e);
    }
}
