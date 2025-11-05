package dataaccess;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends Exception {
    public DataAccessException(String message) {
        super(formatMessage(message));
    }

    public DataAccessException(String message, Throwable ex) {
        super(formatMessage(message), ex);
    }

    private static String formatMessage(String message) {
        if (message == null) {
            return "Error: unspecified database failure";
        }
        if (!message.toLowerCase().contains("error")) {
            return "Error: " + message;
        }
        return message;
    }
}