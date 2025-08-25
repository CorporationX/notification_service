package faang.school.notificationservice.exception;

public class TelegramNotificationException extends RuntimeException {
    public TelegramNotificationException(String message, Throwable cause) {
        super(message, cause);
    }
}