package faang.school.notificationservice.exception;

public class SmsSendingException extends RuntimeException {
    public SmsSendingException(String message) {
        super(message);
    }
}
