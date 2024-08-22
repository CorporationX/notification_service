package faang.school.notificationservice.exception.notification;

public class TelegramBotInitializationException extends RuntimeException{
    public TelegramBotInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}