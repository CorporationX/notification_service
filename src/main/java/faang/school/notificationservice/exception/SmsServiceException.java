package faang.school.notificationservice.exception;

public class SmsServiceException extends RuntimeException {
    public SmsServiceException(String message) {
        super(message);
    }
}