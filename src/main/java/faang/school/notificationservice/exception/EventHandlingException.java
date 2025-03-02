package faang.school.notificationservice.exception;

public class EventHandlingException extends RuntimeException {
    public EventHandlingException(String message, Throwable cause) {
        super(message, cause);
    }
}
