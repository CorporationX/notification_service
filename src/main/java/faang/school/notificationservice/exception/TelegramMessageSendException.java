package faang.school.notificationservice.exception;

public class TelegramMessageSendException extends RuntimeException {
    public TelegramMessageSendException(String message, Throwable cause) {
        super(message, cause);
    }
}
