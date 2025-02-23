package faang.school.notificationservice.exception;

public class SendNotificationException extends NonRetryableException {
    public SendNotificationException(String message) {
        super(message);
    }
}
