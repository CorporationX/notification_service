package faang.school.notificationservice.service.telegram;

public class NotificationFailedException extends RuntimeException {
    public NotificationFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}