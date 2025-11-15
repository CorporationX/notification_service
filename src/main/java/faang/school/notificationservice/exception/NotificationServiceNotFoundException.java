package faang.school.notificationservice.exception;

public class NotificationServiceNotFoundException extends RuntimeException {
    public NotificationServiceNotFoundException(String message) {
        super(message);
    }
}