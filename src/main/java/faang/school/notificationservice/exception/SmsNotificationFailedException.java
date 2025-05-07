package faang.school.notificationservice.exception;

public class SmsNotificationFailedException extends RuntimeException {
    public SmsNotificationFailedException(String message) {
        super(message);
    }
}
