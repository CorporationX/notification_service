package faang.school.notificationservice.exception.handler;

public class SmsSendingException extends RuntimeException {
    public SmsSendingException(String message, Throwable cause) {
        super(message, cause);
    }

    public SmsSendingException(String message) {
        super(message);
    }
}
