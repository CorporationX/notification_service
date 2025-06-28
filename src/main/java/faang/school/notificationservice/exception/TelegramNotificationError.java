package faang.school.notificationservice.exception;

public class TelegramNotificationError extends RuntimeException {
    public TelegramNotificationError(String message) {
        super(message);
    }
}
