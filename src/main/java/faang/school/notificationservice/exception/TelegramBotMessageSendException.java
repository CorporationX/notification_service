package faang.school.notificationservice.exception;

public class TelegramBotMessageSendException extends RuntimeException {
    public TelegramBotMessageSendException(String message) {
        super(message);
    }
}
