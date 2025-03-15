package ma.ensa.apms.exception;

public class EmptyResourcesException extends RuntimeException {
    public EmptyResourcesException(String message) {
        super(message);
    }
}