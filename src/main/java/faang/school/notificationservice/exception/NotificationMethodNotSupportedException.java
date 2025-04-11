package faang.school.notificationservice.exception;

public class NotificationMethodNotSupportedException extends RuntimeException {
    public NotificationMethodNotSupportedException(String message) {
        super(message);
    }
}
