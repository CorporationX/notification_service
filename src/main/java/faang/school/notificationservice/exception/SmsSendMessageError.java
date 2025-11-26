package faang.school.notificationservice.exception;

public class SmsSendMessageError extends RuntimeException {

    public SmsSendMessageError() {
    }

    public SmsSendMessageError(String message) {
        super(message);
    }

    public SmsSendMessageError(String message, Throwable cause) {
        super(message, cause);
    }

    public SmsSendMessageError(Throwable cause) {
        super(cause);
    }
}
