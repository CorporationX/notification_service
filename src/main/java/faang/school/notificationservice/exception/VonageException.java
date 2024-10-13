package faang.school.notificationservice.exception;

public class VonageException extends RuntimeException {
    public VonageException(String message) {
        super(message);
    }

    public VonageException(String message, Object... args) {
        super(String.format(message, args));
    }
}
