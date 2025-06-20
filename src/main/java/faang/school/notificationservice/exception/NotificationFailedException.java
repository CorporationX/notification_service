package faang.school.notificationservice.exception;

public class NotificationFailedException extends RuntimeException {
    public NotificationFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}