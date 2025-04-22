package faang.school.notificationservice.exception;

public class EventListenerException extends RuntimeException {
    public EventListenerException(String message) {
        super(message);
    }

    public EventListenerException(Throwable cause) {
        super(cause);
    }
}
