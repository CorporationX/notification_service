package faang.school.notificationservice.exception;

public class MessageSendingException extends RuntimeException {

    public MessageSendingException() {
    }
    public MessageSendingException(String message) {
        super(message);
    }
//////////
    public MessageSendingException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageSendingException(Throwable cause) {
        super(cause);
    }

    public MessageSendingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
