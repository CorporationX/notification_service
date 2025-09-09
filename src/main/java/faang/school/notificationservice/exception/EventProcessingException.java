package faang.school.notificationservice.exception;

public class EventProcessingException extends RuntimeException {
    public EventProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}