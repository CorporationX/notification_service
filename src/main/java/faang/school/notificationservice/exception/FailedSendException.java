package faang.school.notificationservice.exception;

public class FailedSendException extends RuntimeException{
    public FailedSendException(String message) {
        super(message);
    }
}
