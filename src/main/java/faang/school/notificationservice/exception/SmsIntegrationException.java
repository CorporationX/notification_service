package faang.school.notificationservice.exception;

public class SmsIntegrationException extends RuntimeException {
    public SmsIntegrationException(String message) {
        super(message);
    }

    public SmsIntegrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
