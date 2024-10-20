package faang.school.notificationservice.exception;

public class DataValidationException extends RuntimeException {
    public DataValidationException(String message, Throwable exception) {
        super(message, exception);
    }
}