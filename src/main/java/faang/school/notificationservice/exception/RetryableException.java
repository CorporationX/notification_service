package faang.school.notificationservice.exception;

public class RetryableException extends RuntimeException {
    public RetryableException(String message) {
        super(message);
    }
}
