package faang.school.notificationservice.exception;

public class SmsServiceException extends RuntimeException {
    public SmsServiceException(String message) {
        super(message);
    }

    public SmsServiceException(String message, Exception e) {
        super(message, e);
    }
}